package com.example.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.data.local.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class DailyReminderReceiver : BroadcastReceiver() {
    companion object {
        private const val TAG = "DailyReminderReceiver"
    }

    override fun onReceive(context: Context, intent: Intent) {
        Log.d(TAG, "onReceive action=${intent.action}")

        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            AlarmScheduler.rescheduleAllActiveReminders(context)
            return
        }

        val reminderId = intent.getStringExtra("reminder_id") ?: "daily_study"
        val reminderType = intent.getStringExtra("reminder_type") ?: "DAILY_STUDY"
        val timeString = intent.getStringExtra("time_string") ?: "20:00"

        val pendingResult = goAsync()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val db = AppDatabase.getInstance(context)
                val reminder = db.alarmReminderDao().getReminderById(reminderId)
                if (reminder != null && !reminder.isEnabled) {
                    Log.d(TAG, "Reminder $reminderId is disabled, skipping notification")
                    pendingResult.finish()
                    return@launch
                }

                val settings = db.userSettingsDao().getUserSettingsSync()
                val todayStr = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                val hasStudiedToday = settings?.lastActiveDate == todayStr
                val streak = settings?.streakDays ?: 1

                val notificationId = reminderId.hashCode()

                when (reminderType) {
                    "STREAK_WARNING" -> {
                        // Always warn if not active today
                        if (!hasStudiedToday) {
                            NotificationHelper.showNotification(
                                context = context,
                                notificationId = notificationId,
                                title = "🔥 ¡Protege tu racha de $streak días!",
                                message = "Aún no has realizado ninguna actividad hoy. Entra y completa una lección rápida antes de medianoche."
                            )
                        }
                    }
                    "DAILY_STUDY" -> {
                        if (!hasStudiedToday) {
                            NotificationHelper.showNotification(
                                context = context,
                                notificationId = notificationId,
                                title = "🎯 ¡Hora de tu práctica de inglés!",
                                message = "Mantén tu constancia. Tienes lecciones de gramática, ejercicios y vocabulario esperando por ti."
                            )
                        } else {
                            Log.d(TAG, "User already completed activities today")
                        }
                    }
                    "SRS_REVIEW" -> {
                        NotificationHelper.showNotification(
                            context = context,
                            notificationId = notificationId,
                            title = "🧠 Repaso Espaciado Diario (SRS)",
                            message = "Tienes tarjetas de vocabulario listas para repasar y consolidar tu memoria hoy."
                        )
                    }
                    "STUCK_TOPIC" -> {
                        NotificationHelper.showNotification(
                            context = context,
                            notificationId = notificationId,
                            title = "📚 ¡Avanza con tus temas en curso!",
                            message = "Tienes módulos pendientes de completar. ¡Termina un ejercicio hoy para desbloquear el siguiente nivel!"
                        )
                    }
                    else -> {
                        NotificationHelper.showNotification(
                            context = context,
                            notificationId = notificationId,
                            title = reminder?.label?.ifBlank { "⏰ Recordatorio de Inglés" } ?: "⏰ Recordatorio de Inglés",
                            message = "Es momento de dedicar unos minutos a tu preparación de inglés."
                        )
                    }
                }

                // Reschedule for next day
                AlarmScheduler.scheduleDailyReminder(
                    context = context,
                    reminderId = reminderId,
                    timeString = timeString,
                    type = reminderType
                )
            } catch (e: Exception) {
                Log.e(TAG, "Error in DailyReminderReceiver onReceive", e)
            } finally {
                pendingResult.finish()
            }
        }
    }
}
