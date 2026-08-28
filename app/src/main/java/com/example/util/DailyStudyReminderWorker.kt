package com.example.util

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.data.local.AppDatabase

class DailyStudyReminderWorker(
    private val context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return try {
            val db = AppDatabase.getInstance(context)
            val settings = db.userSettingsDao().getUserSettingsSync()
            val now = System.currentTimeMillis()
            val dueVocab = db.savedVocabDao().getDueVocabItemsSync(now)
            val streak = settings?.streakDays ?: 1
            val goalMinutes = settings?.dailyGoalMinutes ?: 25

            val title = "🇬🇧 ¡Momento de tu inglés B2 diario! (Racha: $streak 🔥)"
            val message = if (dueVocab.isNotEmpty()) {
                "Tienes ${dueVocab.size} palabra(s) en cola de repaso SRS y tu meta de hoy es de $goalMinutes minutos. ¡Entra a practicar!"
            } else {
                "Dedica unos minutos a resolver tus ejercicios Cambridge B2 y no pierdas tu racha de $streak días consecutivos."
            }

            NotificationHelper.showNotification(
                context = context,
                notificationId = 1001,
                title = title,
                message = message
            )

            Result.success()
        } catch (e: Exception) {
            e.printStackTrace()
            Result.retry()
        }
    }
}
