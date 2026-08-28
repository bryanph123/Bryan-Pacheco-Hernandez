package com.example.util

import android.content.Context
import androidx.work.*
import com.example.data.local.entities.StudySessionEntity
import java.util.Calendar
import java.util.concurrent.TimeUnit

object StudyReminderScheduler {

    private const val WORK_TAG_DAILY_REMINDER = "work_daily_study_reminder"
    private const val WORK_TAG_IMMEDIATE_TEST = "work_test_study_reminder"

    /**
     * Schedules a daily recurring push notification at the specified hour & minute (e.g. 20:00)
     * using Android WorkManager.
     */
    fun scheduleDailyPushReminder(
        context: Context,
        targetHour: Int = 20,
        targetMinute: Int = 0
    ) {
        val now = Calendar.getInstance()
        val target = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, targetHour)
            set(Calendar.MINUTE, targetMinute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
            // If the time already passed today, schedule for tomorrow
            if (before(now)) {
                add(Calendar.DAY_OF_YEAR, 1)
            }
        }

        val initialDelayMillis = target.timeInMillis - now.timeInMillis

        val constraints = Constraints.Builder()
            .setRequiresBatteryNotLow(false)
            .build()

        val dailyWorkRequest = PeriodicWorkRequestBuilder<DailyStudyReminderWorker>(
            24, TimeUnit.HOURS,
            15, TimeUnit.MINUTES // 15 min flex interval
        )
            .setInitialDelay(initialDelayMillis, TimeUnit.MILLISECONDS)
            .setConstraints(constraints)
            .addTag(WORK_TAG_DAILY_REMINDER)
            .build()

        WorkManager.getInstance(context.applicationContext).enqueueUniquePeriodicWork(
            WORK_TAG_DAILY_REMINDER,
            ExistingPeriodicWorkPolicy.UPDATE,
            dailyWorkRequest
        )
    }

    /**
     * Immediately fires a test push notification via WorkManager so the user can verify it works.
     */
    fun triggerImmediateTestPushReminder(context: Context) {
        val testWorkRequest = OneTimeWorkRequestBuilder<DailyStudyReminderWorker>()
            .addTag(WORK_TAG_IMMEDIATE_TEST)
            .build()

        WorkManager.getInstance(context.applicationContext).enqueue(testWorkRequest)
    }

    /**
     * Cancels scheduled daily study reminders
     */
    fun cancelReminders(context: Context) {
        WorkManager.getInstance(context.applicationContext).cancelUniqueWork(WORK_TAG_DAILY_REMINDER)
    }

    /**
     * Determines user's habitual study hour based on their recorded study sessions.
     * Defaults to 20:00 (8 PM) if no history is present.
     */
    fun detectHabitualStudyHour(sessions: List<StudySessionEntity>): Pair<Int, Int> {
        if (sessions.isEmpty()) return 20 to 0

        val calendar = Calendar.getInstance()
        val hourFrequencies = mutableMapOf<Int, Int>()

        sessions.forEach { session ->
            calendar.timeInMillis = session.timestamp
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            hourFrequencies[hour] = (hourFrequencies[hour] ?: 0) + 1
        }

        val mostFrequentHour = hourFrequencies.maxByOrNull { it.value }?.key ?: 20
        return mostFrequentHour to 0
    }
}
