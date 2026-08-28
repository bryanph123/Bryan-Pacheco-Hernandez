package com.example.util

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.example.data.local.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar

object AlarmScheduler {
    private const val TAG = "AlarmScheduler"

    fun scheduleDailyReminder(
        context: Context,
        reminderId: String,
        timeString: String,
        type: String = "DAILY_STUDY"
    ) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager ?: return

        val parts = timeString.split(":")
        if (parts.size != 2) return
        val hour = parts[0].toIntOrNull() ?: 20
        val minute = parts[1].toIntOrNull() ?: 0

        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)

            // If time already passed today, schedule for tomorrow
            if (timeInMillis <= System.currentTimeMillis()) {
                add(Calendar.DAY_OF_YEAR, 1)
            }
        }

        val intent = Intent(context, DailyReminderReceiver::class.java).apply {
            action = "com.example.ACTION_DAILY_STUDY_REMINDER"
            putExtra("reminder_id", reminderId)
            putExtra("reminder_type", type)
            putExtra("time_string", timeString)
        }

        val requestCode = reminderId.hashCode()
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    pendingIntent
                )
            } else {
                alarmManager.setExact(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    pendingIntent
                )
            }
            Log.d(TAG, "Scheduled reminder $reminderId for ${calendar.time}")
        } catch (e: SecurityException) {
            Log.e(TAG, "SecurityException while scheduling exact alarm, fallback to inexact", e)
            alarmManager.set(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                pendingIntent
            )
        } catch (e: Exception) {
            Log.e(TAG, "Error scheduling alarm", e)
        }
    }

    fun cancelReminder(context: Context, reminderId: String) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager ?: return
        val intent = Intent(context, DailyReminderReceiver::class.java).apply {
            action = "com.example.ACTION_DAILY_STUDY_REMINDER"
        }
        val requestCode = reminderId.hashCode()
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.cancel(pendingIntent)
        Log.d(TAG, "Cancelled reminder $reminderId")
    }

    fun rescheduleAllActiveReminders(context: Context) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val db = AppDatabase.getInstance(context)
                val reminders = db.alarmReminderDao().getAllRemindersSync()
                for (reminder in reminders) {
                    if (reminder.isEnabled) {
                        scheduleDailyReminder(
                            context = context,
                            reminderId = reminder.id,
                            timeString = reminder.timeString,
                            type = reminder.type
                        )
                    } else {
                        cancelReminder(context, reminder.id)
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error rescheduling all reminders", e)
            }
        }
    }
}
