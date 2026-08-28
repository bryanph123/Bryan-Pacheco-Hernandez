package com.example.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "topics")
data class TopicEntity(
    @PrimaryKey val id: String,
    val title: String,
    val titleSpanish: String,
    val category: String, // Grammar, Vocabulary, Listening, Speaking, Reading, Writing, Pronunciation, Communicative
    val moduleGroup: String, // e.g. "Tiempos Verbales Avanzados", "Phrasal Verbs de Tecnología", etc.
    val orderIndex: Int,
    val explanation: String, // Rich explanation in Spanish
    val examplesJson: String, // List of English examples with Spanish translations
    val commonMistakesJson: String, // Common errors made by Spanish speakers
    val miniGlossaryJson: String, // Key terms and definitions
    val difficulty: String = "B2", // B1+, B2, B2+
    val estimatedMinutes: Int = 20,
    val status: String = "NOT_STARTED", // NOT_STARTED, IN_PROGRESS, COMPLETED, MASTERED
    val lastActivityTimestamp: Long = 0L,
    val suggestedReviewTimestamp: Long = 0L,
    val isCustom: Boolean = false
)

@Entity(tableName = "topic_notes")
data class TopicNoteEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val topicId: String,
    val noteContent: String,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "study_sessions")
data class StudySessionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val topicId: String?,
    val topicTitle: String?,
    val category: String,
    val durationSeconds: Long,
    val mode: String, // POMODORO, FREE_STOPWATCH, EXAM
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "saved_vocab_items")
data class SavedVocabItemEntity(
    @PrimaryKey val id: String,
    val sourceText: String,
    val translation: String,
    val phonetic: String = "",
    val sourceModule: String, // translator, dictionary, curriculum
    val definition: String = "",
    val examplesJson: String = "[]",
    val synonymsJson: String = "[]",
    val savedAt: Long = System.currentTimeMillis(),
    val linkedTopicId: String? = null,
    // Spaced Repetition (SRS / SM-2)
    val repetitionNumber: Int = 0,
    val intervalDays: Int = 1,
    val easeFactor: Float = 2.5f,
    val nextReviewTimestamp: Long = System.currentTimeMillis(),
    val masteryLevel: Int = 0 // 0 to 5
)

@Entity(tableName = "translation_history")
data class TranslationHistoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val sourceText: String,
    val translatedText: String,
    val phonetic: String = "",
    val sourceLang: String = "es",
    val targetLang: String = "en",
    val createdAt: Long = System.currentTimeMillis(),
    val isFavorite: Boolean = false
)

@Entity(tableName = "dictionary_lookups")
data class DictionaryLookupEntity(
    @PrimaryKey val id: String,
    val term: String,
    val phonetic: String = "",
    val partOfSpeech: String = "",
    val definitionEs: String = "",
    val definitionEn: String = "",
    val examplesJson: String = "[]",
    val synonymsJson: String = "[]",
    val phrasalVerbsJson: String = "[]",
    val usageNotes: String = "",
    val lookedUpAt: Long = System.currentTimeMillis(),
    val isSaved: Boolean = false
)

@Entity(tableName = "exercise_attempts")
data class ExerciseAttemptEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val topicId: String,
    val exerciseType: String, // MULTIPLE_CHOICE, FILL_BLANK, TRANSFORMATION, MATCHING, LISTENING, WRITING, SPEAKING, EXAM_SIMULATION
    val score: Int,
    val maxScore: Int,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "alarm_reminders")
data class AlarmReminderEntity(
    @PrimaryKey val id: String,
    val type: String, // DAILY_STUDY, STUCK_TOPIC, STREAK_WARNING, SRS_REVIEW, ALARM_CLOCK
    val timeString: String = "20:00", // HH:mm
    val daysOfWeek: String = "1,2,3,4,5,6,7",
    val isEnabled: Boolean = true,
    val label: String = "",
    val soundEnabled: Boolean = true
)

@Entity(tableName = "user_settings")
data class UserSettingsEntity(
    @PrimaryKey val id: Int = 1,
    val streakDays: Int = 1,
    val lastActiveDate: String = "",
    val totalXp: Int = 150,
    val dailyGoalMinutes: Int = 30,
    val planWeeks: Int = 12,
    val stuckDaysThreshold: Int = 3,
    val darkMode: Boolean = false,
    val preferredStudyHour: Int = 20, // 20:00 (8:00 PM) habitual reminder time
    val preferredStudyMinute: Int = 0,
    val dailyRemindersEnabled: Boolean = true,
    val ttsAccent: String = "UK" // "UK" or "US"
)

@Entity(tableName = "cached_exercises")
data class CachedExerciseEntity(
    @PrimaryKey val id: String,
    val type: String, // IMAGE_VOCAB_MATCHING, MULTIPLE_CHOICE, FILL_IN_THE_BLANK, KEYWORD_TRANSFORMATION
    val level: String, // A1, A2, B1, B2
    val category: String,
    val title: String,
    val prompt: String,
    val contextText: String? = null,
    val optionsJson: String = "[]",
    val correctAnswer: String,
    val acceptedAlternativesJson: String = "[]",
    val hintSpanish: String = "",
    val explanation: String = "",
    val audioText: String = "",
    val keyWord: String? = null,
    val visualIllustrationId: String? = null,
    val visualEmoji: String? = null,
    val visualAccentColorHex: Long = 0L,
    val visualDescription: String? = null,
    val linkedTopicId: String? = null,
    val isCachedOffline: Boolean = true,
    val lastUpdated: Long = System.currentTimeMillis()
)

@Entity(tableName = "cached_vocab_bank")
data class CachedVocabEntity(
    @PrimaryKey val id: String,
    val word: String,
    val translation: String,
    val phonetic: String,
    val level: String, // A1, A2, B1, B2
    val topic: String, // Work, Travel, Food, Health, etc.
    val definition: String = "",
    val exampleEn: String = "",
    val exampleEs: String = "",
    val isOfflineReady: Boolean = true,
    val lastUpdated: Long = System.currentTimeMillis()
)

@Entity(tableName = "user_badges")
data class UserBadgeEntity(
    @PrimaryKey val badgeId: String,
    val title: String,
    val titleSpanish: String,
    val description: String,
    val category: String, // STREAK, VOCABULARY, POMODORO, EXERCISES, LEVEL, CAMBRIDGE
    val iconEmoji: String,
    val tier: String, // BRONZE, SILVER, GOLD, PLATINUM, DIAMOND
    val currentProgress: Int = 0,
    val targetGoal: Int = 1,
    val isUnlocked: Boolean = false,
    val unlockedAtTimestamp: Long? = null,
    val xpReward: Int = 50
)

