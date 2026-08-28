package com.example.data.repository

import com.example.data.local.dao.*
import com.example.data.local.entities.*
import com.example.data.local.model.ModularExerciseQuestion
import com.example.data.srs.*
import kotlinx.coroutines.flow.Flow
import java.text.SimpleDateFormat
import java.util.*

data class SrsStats(
    val totalItems: Int,
    val dueItems: Int,
    val masteredItems: Int
)

data class GlobalStats(
    val totalTopics: Int,
    val completedTopics: Int,
    val masteredTopics: Int,
    val inProgressTopics: Int,
    val notStartedTopics: Int,
    val totalStudyMinutes: Long,
    val streakDays: Int,
    val totalXp: Int,
    val categoryProgress: Map<String, Float>
)

class StudyRepository(
    private val savedVocabDao: SavedVocabDao,
    private val studySessionDao: StudySessionDao,
    private val topicDao: TopicDao,
    private val userSettingsDao: UserSettingsDao,
    private val alarmReminderDao: AlarmReminderDao,
    private val exerciseAttemptDao: ExerciseAttemptDao,
    private val userBadgeDao: UserBadgeDao,
    private val cachedExerciseDao: CachedExerciseDao,
    private val cachedVocabDao: CachedVocabDao
) {
    val allStudySessions: Flow<List<StudySessionEntity>> = studySessionDao.getAllSessions()
    val allVocabItems: Flow<List<SavedVocabItemEntity>> = savedVocabDao.getAllVocabItems()
    val allReminders: Flow<List<AlarmReminderEntity>> = alarmReminderDao.getAllReminders()
    val userSettings: Flow<UserSettingsEntity?> = userSettingsDao.getUserSettings()
    val totalStudySeconds: Flow<Long?> = studySessionDao.getTotalStudySeconds()
    val totalExerciseAttemptsCount: Flow<Int> = exerciseAttemptDao.getTotalAttemptCount()
    val allExerciseAttempts: Flow<List<ExerciseAttemptEntity>> = exerciseAttemptDao.getRecentAttempts()
    val allBadges: Flow<List<UserBadgeEntity>> = userBadgeDao.getAllBadges()
    val unlockedBadgesCount: Flow<Int> = userBadgeDao.getUnlockedBadgeCount()
    val cachedExercises: Flow<List<CachedExerciseEntity>> = cachedExerciseDao.getAllCachedExercises()
    val cachedVocab: Flow<List<CachedVocabEntity>> = cachedVocabDao.getAllCachedVocab()

    suspend fun ensureInitialStatsSeeded() = kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.IO) {
        val count = studySessionDao.getSessionCount()
        if (count == 0) {
            val now = System.currentTimeMillis()
            val oneDayMs = 24 * 60 * 60 * 1000L

            val initialSessions = listOf(
                StudySessionEntity(topicId = "b2_grammar_01", topicTitle = "Narrative Tenses & Past Perfect", category = "Gramática", durationSeconds = 50 * 60L, mode = "POMODORO", timestamp = now - 6 * oneDayMs),
                StudySessionEntity(topicId = "b2_vocab_01", topicTitle = "Technology & AI Vocabulary", category = "Vocabulario", durationSeconds = 75 * 60L, mode = "POMODORO", timestamp = now - 5 * oneDayMs),
                StudySessionEntity(topicId = "b2_listening_01", topicTitle = "Cambridge B2 Listening Part 1", category = "Listening", durationSeconds = 25 * 60L, mode = "POMODORO", timestamp = now - 4 * oneDayMs),
                StudySessionEntity(topicId = "b2_grammar_02", topicTitle = "Mixed Conditionals (2nd & 3rd)", category = "Gramática", durationSeconds = 100 * 60L, mode = "POMODORO", timestamp = now - 3 * oneDayMs),
                StudySessionEntity(topicId = "b2_reading_01", topicTitle = "Reading & Use of English Part 5", category = "Reading", durationSeconds = 50 * 60L, mode = "POMODORO", timestamp = now - 2 * oneDayMs),
                StudySessionEntity(topicId = "b2_speaking_01", topicTitle = "Speaking Part 3 Collaborative Task", category = "Speaking", durationSeconds = 75 * 60L, mode = "POMODORO", timestamp = now - 1 * oneDayMs),
                StudySessionEntity(topicId = "b2_vocab_02", topicTitle = "Phrasal Verbs with Look & Get", category = "Vocabulario", durationSeconds = 25 * 60L, mode = "POMODORO", timestamp = now)
            )

            val initialAttempts = listOf(
                ExerciseAttemptEntity(topicId = "b2_grammar_01", exerciseType = "MULTIPLE_CHOICE", score = 8, maxScore = 10, timestamp = now - 6 * oneDayMs),
                ExerciseAttemptEntity(topicId = "b2_vocab_01", exerciseType = "MATCHING", score = 9, maxScore = 10, timestamp = now - 5 * oneDayMs),
                ExerciseAttemptEntity(topicId = "b2_listening_01", exerciseType = "LISTENING", score = 4, maxScore = 5, timestamp = now - 4 * oneDayMs),
                ExerciseAttemptEntity(topicId = "b2_grammar_02", exerciseType = "TRANSFORMATION", score = 9, maxScore = 10, timestamp = now - 3 * oneDayMs),
                ExerciseAttemptEntity(topicId = "b2_reading_01", exerciseType = "FILL_BLANK", score = 9, maxScore = 10, timestamp = now - 2 * oneDayMs),
                ExerciseAttemptEntity(topicId = "b2_speaking_01", exerciseType = "SPEAKING", score = 10, maxScore = 10, timestamp = now - 1 * oneDayMs),
                ExerciseAttemptEntity(topicId = "b2_vocab_02", exerciseType = "MULTIPLE_CHOICE", score = 5, maxScore = 5, timestamp = now)
            )

            initialSessions.forEach { studySessionDao.insertSession(it) }
            initialAttempts.forEach { exerciseAttemptDao.insertAttempt(it) }
        }

        // Ensure badges and cached data are populated
        ensureBadgesAndCacheSeeded()
    }

    suspend fun ensureBadgesAndCacheSeeded() = kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.IO) {
        if (cachedExerciseDao.getExerciseCount() == 0) {
            cachedExerciseDao.insertExercises(com.example.data.local.seed.BadgeAndCacheSeedData.getInitialCachedExercises())
        }
        if (cachedVocabDao.getVocabCount() == 0) {
            cachedVocabDao.insertVocabList(com.example.data.local.seed.BadgeAndCacheSeedData.getInitialCachedVocab())
        }
        // Ensure initial badges exist
        val initialBadges = com.example.data.local.seed.BadgeAndCacheSeedData.getInitialBadges()
        initialBadges.forEach { badge ->
            val existing = userBadgeDao.getBadgeById(badge.badgeId)
            if (existing == null) {
                userBadgeDao.insertBadges(listOf(badge))
            }
        }
    }

    suspend fun addXp(xp: Int) = kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.IO) {
        userSettingsDao.addXp(xp)
    }

    suspend fun evaluateAndSaveBadges(
        currentBadges: List<UserBadgeEntity>,
        streakDays: Int,
        totalXp: Int,
        sessions: List<StudySessionEntity>,
        vocabItems: List<SavedVocabItemEntity>,
        attempts: List<ExerciseAttemptEntity>
    ): List<UserBadgeEntity> = kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.IO) {
        val updated = com.example.util.UserLevelHelper.evaluateBadgesUpdates(
            currentBadges = currentBadges,
            streakDays = streakDays,
            totalXp = totalXp,
            sessions = sessions,
            vocabItems = vocabItems,
            attempts = attempts
        )
        updated.forEach { badge ->
            userBadgeDao.updateBadge(badge)
        }
        updated
    }

    suspend fun claimBadgeReward(badgeId: String): Int {
        val badge = userBadgeDao.getBadgeById(badgeId) ?: return 0
        if (badge.isUnlocked) {
            userSettingsDao.addXp(badge.xpReward)
            return badge.xpReward
        }
        return 0
    }

    suspend fun refreshOfflineCache() {
        // Re-sync all exercises and vocabulary into Room DB
        cachedExerciseDao.clearCachedExercises()
        cachedExerciseDao.insertExercises(com.example.data.local.seed.BadgeAndCacheSeedData.getInitialCachedExercises())
        cachedVocabDao.clearCachedVocab()
        cachedVocabDao.insertVocabList(com.example.data.local.seed.BadgeAndCacheSeedData.getInitialCachedVocab())
    }

    fun getTodayExerciseAttemptsCount(): Flow<Int> {
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        return exerciseAttemptDao.getAttemptsCountSince(calendar.timeInMillis)
    }

    fun getDueVocabItems(): Flow<List<SavedVocabItemEntity>> {
        return savedVocabDao.getDueVocabItems(System.currentTimeMillis())
    }

    /**
     * Evaluates an exercise question attempt with the Spaced Repetition Algorithm (SM-2+),
     * automatically updating the item's review interval, mastery tier, and appearance frequency in Room DB.
     */
    suspend fun processExerciseSrsAnswer(
        question: ModularExerciseQuestion,
        isCorrect: Boolean,
        usedHint: Boolean,
        responseTimeMs: Long
    ): SrsEvaluationResult {
        val targetWord = SrsAlgorithm.extractVocabKey(question)
        val translation = question.contextText?.replace("Significado en español: '", "")?.replace("'", "")
            ?: question.correctAnswer

        // Find existing record by ID or term
        val existingItem = if (!question.srsItemId.isNullOrBlank()) {
            savedVocabDao.getVocabItemById(question.srsItemId)
        } else {
            savedVocabDao.getVocabItemBySourceText(targetWord)
        }

        val evalResult = SrsAlgorithm.evaluatePerformance(
            word = targetWord,
            translation = translation,
            isCorrect = isCorrect,
            usedHint = usedHint,
            responseTimeMs = responseTimeMs,
            existingItem = existingItem
        )

        // Persist or insert updated entity in Room
        val itemId = existingItem?.id ?: ("srs_ex_" + targetWord.lowercase().trim().replace(" ", "_").replace(Regex("[^a-z0-9_]"), ""))
        val updatedEntity = SavedVocabItemEntity(
            id = itemId,
            sourceText = targetWord,
            translation = existingItem?.translation?.ifBlank { translation } ?: translation,
            phonetic = existingItem?.phonetic ?: question.visualIllustration?.phonetic ?: "",
            sourceModule = existingItem?.sourceModule ?: "exercise_system",
            definition = existingItem?.definition?.ifBlank { question.explanation } ?: question.explanation,
            examplesJson = existingItem?.examplesJson ?: "[]",
            synonymsJson = existingItem?.synonymsJson ?: "[]",
            savedAt = existingItem?.savedAt ?: System.currentTimeMillis(),
            linkedTopicId = existingItem?.linkedTopicId ?: question.linkedTopicId,
            repetitionNumber = evalResult.newRepetition,
            intervalDays = evalResult.newIntervalDays,
            easeFactor = evalResult.newEaseFactor,
            nextReviewTimestamp = evalResult.nextReviewTimestamp,
            masteryLevel = evalResult.newMasteryLevel
        )

        savedVocabDao.insertVocabItem(updatedEntity)

        // Award XP based on quality
        val xpGain = when (evalResult.quality) {
            5 -> 25
            4 -> 18
            3 -> 12
            2 -> 5
            else -> 2
        }
        userSettingsDao.addXp(xpGain)
        updateDailyStreak()

        return evalResult
    }

    // Explicit SRS Review (e.g. from Flashcards screen with Quality 1-5 rating)
    suspend fun processSrsReview(item: SavedVocabItemEntity, rating: Int): SrsEvaluationResult {
        val isCorrect = rating >= 3
        val usedHint = rating == 3
        val responseTime = if (rating == 5) 2000L else if (rating == 4) 6000L else 14000L

        val eval = SrsAlgorithm.evaluatePerformance(
            word = item.sourceText,
            translation = item.translation,
            isCorrect = isCorrect,
            usedHint = usedHint,
            responseTimeMs = responseTime,
            existingItem = item
        )

        val updated = item.copy(
            repetitionNumber = eval.newRepetition,
            intervalDays = eval.newIntervalDays,
            easeFactor = eval.newEaseFactor,
            nextReviewTimestamp = eval.nextReviewTimestamp,
            masteryLevel = eval.newMasteryLevel
        )
        savedVocabDao.updateVocabItem(updated)
        userSettingsDao.addXp(10 * eval.quality)
        updateDailyStreak()
        return eval
    }

    suspend fun recordStudySession(
        topicId: String?,
        topicTitle: String?,
        category: String,
        durationSeconds: Long,
        mode: String
    ) {
        if (durationSeconds <= 0) return
        studySessionDao.insertSession(
            StudySessionEntity(
                topicId = topicId,
                topicTitle = topicTitle,
                category = category,
                durationSeconds = durationSeconds,
                mode = mode,
                timestamp = System.currentTimeMillis()
            )
        )
        // Add XP based on minutes
        val xpEarned = (durationSeconds / 60).toInt() * 5 + 10
        userSettingsDao.addXp(xpEarned)

        // Check & update streak
        updateDailyStreak()
    }

    private suspend fun updateDailyStreak() {
        val settings = userSettingsDao.getUserSettingsSync() ?: return
        val todayStr = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        if (settings.lastActiveDate == todayStr) {
            // Already active today
            return
        }

        val cal = Calendar.getInstance()
        cal.add(Calendar.DAY_OF_YEAR, -1)
        val yesterdayStr = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(cal.time)

        val newStreak = if (settings.lastActiveDate == yesterdayStr) {
            settings.streakDays + 1
        } else {
            1
        }
        userSettingsDao.updateStreak(newStreak, todayStr)
    }

    suspend fun updateReminder(reminder: AlarmReminderEntity) {
        alarmReminderDao.updateReminder(reminder)
    }

    suspend fun insertReminder(reminder: AlarmReminderEntity) {
        alarmReminderDao.insertReminder(reminder)
    }

    suspend fun saveWordToSrs(term: String, translation: String, sourceModule: String = "cambridge_vocab") {
        val id = "srs_w_" + term.lowercase().trim().replace(" ", "_")
        val item = SavedVocabItemEntity(
            id = id,
            sourceText = term.trim(),
            translation = translation.trim(),
            phonetic = "",
            sourceModule = sourceModule,
            definition = translation.trim(),
            examplesJson = "[]",
            synonymsJson = "[]",
            savedAt = System.currentTimeMillis(),
            repetitionNumber = 0,
            intervalDays = 1,
            easeFactor = 2.5f,
            nextReviewTimestamp = System.currentTimeMillis(),
            masteryLevel = 0
        )
        savedVocabDao.insertVocabItem(item)
    }

    suspend fun deleteReminder(reminder: AlarmReminderEntity) {
        alarmReminderDao.deleteReminder(reminder)
    }

    suspend fun updateGoalMinutes(minutes: Int) {
        val current = userSettingsDao.getUserSettingsSync() ?: return
        userSettingsDao.updateSettings(current.copy(dailyGoalMinutes = minutes))
    }

    suspend fun recordExerciseAttempt(topicId: String, type: String, score: Int, maxScore: Int) {
        exerciseAttemptDao.insertAttempt(
            ExerciseAttemptEntity(
                topicId = topicId,
                exerciseType = type,
                score = score,
                maxScore = maxScore,
                timestamp = System.currentTimeMillis()
            )
        )
        val xp = (score * 10)
        userSettingsDao.addXp(xp)
    }
}
