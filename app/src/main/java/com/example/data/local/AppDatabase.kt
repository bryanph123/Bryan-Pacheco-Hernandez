package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.local.dao.*
import com.example.data.local.entities.*
import com.example.data.local.seed.TopicSeedData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        TopicEntity::class,
        TopicNoteEntity::class,
        StudySessionEntity::class,
        SavedVocabItemEntity::class,
        TranslationHistoryEntity::class,
        DictionaryLookupEntity::class,
        ExerciseAttemptEntity::class,
        AlarmReminderEntity::class,
        UserSettingsEntity::class,
        CachedExerciseEntity::class,
        CachedVocabEntity::class,
        UserBadgeEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun topicDao(): TopicDao
    abstract fun topicNoteDao(): TopicNoteDao
    abstract fun studySessionDao(): StudySessionDao
    abstract fun savedVocabDao(): SavedVocabDao
    abstract fun translationDao(): TranslationDao
    abstract fun dictionaryDao(): DictionaryDao
    abstract fun exerciseAttemptDao(): ExerciseAttemptDao
    abstract fun alarmReminderDao(): AlarmReminderDao
    abstract fun userSettingsDao(): UserSettingsDao
    abstract fun cachedExerciseDao(): CachedExerciseDao
    abstract fun cachedVocabDao(): CachedVocabDao
    abstract fun userBadgeDao(): UserBadgeDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "ingles_b2_database.db"
                ).fallbackToDestructiveMigration()
                .addCallback(object : Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        // Seed initial data
                        CoroutineScope(Dispatchers.IO).launch {
                            val database = getInstance(context)
                            database.topicDao().insertTopics(TopicSeedData.getInitialTopics())
                            database.userBadgeDao().insertBadges(com.example.data.local.seed.BadgeAndCacheSeedData.getInitialBadges())
                            database.cachedExerciseDao().insertExercises(com.example.data.local.seed.BadgeAndCacheSeedData.getInitialCachedExercises())
                            database.cachedVocabDao().insertVocabList(com.example.data.local.seed.BadgeAndCacheSeedData.getInitialCachedVocab())
                            database.userSettingsDao().insertSettings(
                                UserSettingsEntity(
                                    id = 1,
                                    streakDays = 3,
                                    lastActiveDate = "2026-08-27",
                                    totalXp = 340,
                                    dailyGoalMinutes = 25,
                                    planWeeks = 12,
                                    stuckDaysThreshold = 3,
                                    darkMode = false,
                                    preferredStudyHour = 20,
                                    preferredStudyMinute = 0,
                                    dailyRemindersEnabled = true,
                                    ttsAccent = "UK"
                                )
                            )
                            database.alarmReminderDao().insertReminders(
                                listOf(
                                    AlarmReminderEntity(
                                        id = "daily_study",
                                        type = "DAILY_STUDY",
                                        timeString = "20:00",
                                        daysOfWeek = "1,2,3,4,5,6,7",
                                        isEnabled = true,
                                        label = "Recordatorio diario de estudio B2",
                                        soundEnabled = true
                                    ),
                                    AlarmReminderEntity(
                                        id = "stuck_topic",
                                        type = "STUCK_TOPIC",
                                        timeString = "14:00",
                                        daysOfWeek = "1,2,3,4,5,6,7",
                                        isEnabled = true,
                                        label = "Alerta de tema estancado (>3 días)",
                                        soundEnabled = false
                                    ),
                                    AlarmReminderEntity(
                                        id = "streak_warning",
                                        type = "STREAK_WARNING",
                                        timeString = "21:30",
                                        daysOfWeek = "1,2,3,4,5,6,7",
                                        isEnabled = true,
                                        label = "Aviso nocturno: ¡Racha en riesgo!",
                                        soundEnabled = true
                                    ),
                                    AlarmReminderEntity(
                                        id = "srs_review",
                                        type = "SRS_REVIEW",
                                        timeString = "08:30",
                                        daysOfWeek = "1,2,3,4,5,6,7",
                                        isEnabled = true,
                                        label = "Tarjetas de vocabulario listas para repasar",
                                        soundEnabled = true
                                    )
                                )
                            )
                            // Initial seed vocab items
                            database.savedVocabDao().insertVocabItem(
                                SavedVocabItemEntity(
                                    id = "vocab_init_1",
                                    sourceText = "troubleshoot",
                                    translation = "diagnosticar y resolver fallas",
                                    phonetic = "/ˈtrʌb.əl.ʃuːt/",
                                    sourceModule = "dictionary",
                                    definition = "To discover why something does not work effectively and help to improve it or fix problems.",
                                    examplesJson = "[{\"en\":\"I need to troubleshoot the Wi-Fi gateway connection.\",\"es\":\"Necesito diagnosticar y resolver la conexión del gateway Wi-Fi.\"}]",
                                    synonymsJson = "[\"diagnose\", \"debug\", \"fix\"]",
                                    linkedTopicId = "vocab_08",
                                    intervalDays = 1,
                                    repetitionNumber = 1,
                                    easeFactor = 2.5f,
                                    nextReviewTimestamp = System.currentTimeMillis() - 1000
                                )
                            )
                            database.savedVocabDao().insertVocabItem(
                                SavedVocabItemEntity(
                                    id = "vocab_init_2",
                                    sourceText = "hit the ground running",
                                    translation = "empezar con gran ritmo y éxito inmediato",
                                    phonetic = "/hɪt ðə ɡraʊnd ˈrʌn.ɪŋ/",
                                    sourceModule = "translator",
                                    definition = "To start something and proceed immediately at full speed and with great enthusiasm.",
                                    examplesJson = "[{\"en\":\"The new teacher hit the ground running on the first school day.\",\"es\":\"El nuevo maestro empezó con todo el ritmo desde el primer día de clases.\"}]",
                                    synonymsJson = "[\"make a fast start\", \"start successfully\"]",
                                    linkedTopicId = "vocab_06",
                                    intervalDays = 2,
                                    repetitionNumber = 2,
                                    easeFactor = 2.6f,
                                    nextReviewTimestamp = System.currentTimeMillis() + 86400000L
                                )
                            )
                        }
                    }
                }).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
