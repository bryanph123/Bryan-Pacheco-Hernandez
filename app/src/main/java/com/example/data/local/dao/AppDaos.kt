package com.example.data.local.dao

import androidx.room.*
import com.example.data.local.entities.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TopicDao {
    @Query("SELECT * FROM topics ORDER BY orderIndex ASC")
    fun getAllTopics(): Flow<List<TopicEntity>>

    @Query("SELECT * FROM topics WHERE id = :id LIMIT 1")
    fun getTopicById(id: String): Flow<TopicEntity?>

    @Query("SELECT * FROM topics WHERE id = :id LIMIT 1")
    suspend fun getTopicByIdSync(id: String): TopicEntity?

    @Query("SELECT * FROM topics WHERE category = :category ORDER BY orderIndex ASC")
    fun getTopicsByCategory(category: String): Flow<List<TopicEntity>>

    @Query("SELECT * FROM topics WHERE status = :status ORDER BY orderIndex ASC")
    fun getTopicsByStatus(status: String): Flow<List<TopicEntity>>

    @Query("SELECT COUNT(*) FROM topics")
    suspend fun getTopicCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTopics(topics: List<TopicEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTopic(topic: TopicEntity)

    @Update
    suspend fun updateTopic(topic: TopicEntity)

    @Query("UPDATE topics SET status = :status, lastActivityTimestamp = :timestamp WHERE id = :topicId")
    suspend fun updateTopicStatus(topicId: String, status: String, timestamp: Long)

    @Query("DELETE FROM topics WHERE id = :id AND isCustom = 1")
    suspend fun deleteCustomTopic(id: String)
}

@Dao
interface TopicNoteDao {
    @Query("SELECT * FROM topic_notes WHERE topicId = :topicId ORDER BY timestamp DESC")
    fun getNotesForTopic(topicId: String): Flow<List<TopicNoteEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: TopicNoteEntity)

    @Delete
    suspend fun deleteNote(note: TopicNoteEntity)
}

@Dao
interface StudySessionDao {
    @Query("SELECT * FROM study_sessions ORDER BY timestamp DESC")
    fun getAllSessions(): Flow<List<StudySessionEntity>>

    @Query("SELECT COUNT(*) FROM study_sessions")
    suspend fun getSessionCount(): Int

    @Query("SELECT * FROM study_sessions WHERE topicId = :topicId ORDER BY timestamp DESC")
    fun getSessionsForTopic(topicId: String): Flow<List<StudySessionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSession(session: StudySessionEntity)

    @Query("SELECT SUM(durationSeconds) FROM study_sessions")
    fun getTotalStudySeconds(): Flow<Long?>

    @Query("SELECT SUM(durationSeconds) FROM study_sessions WHERE timestamp >= :sinceTimestamp")
    fun getStudySecondsSince(sinceTimestamp: Long): Flow<Long?>
}

@Dao
interface SavedVocabDao {
    @Query("SELECT * FROM saved_vocab_items ORDER BY nextReviewTimestamp ASC")
    fun getAllVocabItems(): Flow<List<SavedVocabItemEntity>>

    @Query("SELECT * FROM saved_vocab_items WHERE nextReviewTimestamp <= :currentTimestamp ORDER BY nextReviewTimestamp ASC")
    fun getDueVocabItems(currentTimestamp: Long): Flow<List<SavedVocabItemEntity>>

    @Query("SELECT COUNT(*) FROM saved_vocab_items WHERE nextReviewTimestamp <= :currentTimestamp")
    fun getDueVocabCount(currentTimestamp: Long): Flow<Int>

    @Query("SELECT * FROM saved_vocab_items WHERE id = :id LIMIT 1")
    suspend fun getVocabItemById(id: String): SavedVocabItemEntity?

    @Query("SELECT * FROM saved_vocab_items WHERE LOWER(TRIM(sourceText)) = LOWER(TRIM(:sourceText)) LIMIT 1")
    suspend fun getVocabItemBySourceText(sourceText: String): SavedVocabItemEntity?

    @Query("SELECT * FROM saved_vocab_items")
    suspend fun getAllVocabItemsSync(): List<SavedVocabItemEntity>

    @Query("SELECT * FROM saved_vocab_items WHERE nextReviewTimestamp <= :currentTimestamp ORDER BY nextReviewTimestamp ASC")
    suspend fun getDueVocabItemsSync(currentTimestamp: Long): List<SavedVocabItemEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVocabItem(item: SavedVocabItemEntity)

    @Update
    suspend fun updateVocabItem(item: SavedVocabItemEntity)

    @Delete
    suspend fun deleteVocabItem(item: SavedVocabItemEntity)
}

@Dao
interface TranslationDao {
    @Query("SELECT * FROM translation_history ORDER BY createdAt DESC LIMIT 50")
    fun getRecentTranslations(): Flow<List<TranslationHistoryEntity>>

    @Query("SELECT * FROM translation_history WHERE isFavorite = 1 ORDER BY createdAt DESC")
    fun getFavoriteTranslations(): Flow<List<TranslationHistoryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTranslation(item: TranslationHistoryEntity): Long

    @Query("UPDATE translation_history SET isFavorite = :isFavorite WHERE id = :id")
    suspend fun toggleFavorite(id: Long, isFavorite: Boolean)

    @Query("DELETE FROM translation_history WHERE id = :id")
    suspend fun deleteTranslation(id: Long)

    @Query("DELETE FROM translation_history")
    suspend fun clearHistory()
}

@Dao
interface DictionaryDao {
    @Query("SELECT * FROM dictionary_lookups ORDER BY lookedUpAt DESC LIMIT 50")
    fun getRecentLookups(): Flow<List<DictionaryLookupEntity>>

    @Query("SELECT * FROM dictionary_lookups WHERE term = :term COLLATE NOCASE LIMIT 1")
    suspend fun getLookupByTerm(term: String): DictionaryLookupEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLookup(item: DictionaryLookupEntity)

    @Query("UPDATE dictionary_lookups SET isSaved = :isSaved WHERE id = :id")
    suspend fun toggleSaved(id: String, isSaved: Boolean)

    @Query("DELETE FROM dictionary_lookups WHERE id = :id")
    suspend fun deleteLookup(id: String)
}

@Dao
interface ExerciseAttemptDao {
    @Query("SELECT * FROM exercise_attempts ORDER BY timestamp DESC LIMIT 100")
    fun getRecentAttempts(): Flow<List<ExerciseAttemptEntity>>

    @Query("SELECT * FROM exercise_attempts WHERE topicId = :topicId ORDER BY timestamp DESC")
    fun getAttemptsForTopic(topicId: String): Flow<List<ExerciseAttemptEntity>>

    @Query("SELECT COUNT(*) FROM exercise_attempts")
    fun getTotalAttemptCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM exercise_attempts WHERE timestamp >= :sinceTimestamp")
    fun getAttemptsCountSince(sinceTimestamp: Long): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAttempt(attempt: ExerciseAttemptEntity)
}

@Dao
interface AlarmReminderDao {
    @Query("SELECT * FROM alarm_reminders")
    fun getAllReminders(): Flow<List<AlarmReminderEntity>>

    @Query("SELECT * FROM alarm_reminders")
    suspend fun getAllRemindersSync(): List<AlarmReminderEntity>

    @Query("SELECT * FROM alarm_reminders WHERE id = :id LIMIT 1")
    suspend fun getReminderById(id: String): AlarmReminderEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReminder(reminder: AlarmReminderEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReminders(reminders: List<AlarmReminderEntity>)

    @Update
    suspend fun updateReminder(reminder: AlarmReminderEntity)

    @Delete
    suspend fun deleteReminder(reminder: AlarmReminderEntity)
}

@Dao
interface UserSettingsDao {
    @Query("SELECT * FROM user_settings WHERE id = 1 LIMIT 1")
    fun getUserSettings(): Flow<UserSettingsEntity?>

    @Query("SELECT * FROM user_settings WHERE id = 1 LIMIT 1")
    suspend fun getUserSettingsSync(): UserSettingsEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSettings(settings: UserSettingsEntity)

    @Update
    suspend fun updateSettings(settings: UserSettingsEntity)

    @Query("UPDATE user_settings SET totalXp = totalXp + :xpDelta WHERE id = 1")
    suspend fun addXp(xpDelta: Int)

    @Query("UPDATE user_settings SET streakDays = :streak, lastActiveDate = :date WHERE id = 1")
    suspend fun updateStreak(streak: Int, date: String)

    @Query("UPDATE user_settings SET preferredStudyHour = :hour, preferredStudyMinute = :minute, dailyRemindersEnabled = :enabled WHERE id = 1")
    suspend fun updateStudySchedule(hour: Int, minute: Int, enabled: Boolean)

    @Query("UPDATE user_settings SET ttsAccent = :accent WHERE id = 1")
    suspend fun updateTtsAccent(accent: String)
}

@Dao
interface CachedExerciseDao {
    @Query("SELECT * FROM cached_exercises ORDER BY level ASC, title ASC")
    fun getAllCachedExercises(): Flow<List<CachedExerciseEntity>>

    @Query("SELECT * FROM cached_exercises ORDER BY level ASC, title ASC")
    suspend fun getAllCachedExercisesSync(): List<CachedExerciseEntity>

    @Query("SELECT * FROM cached_exercises WHERE level = :level ORDER BY title ASC")
    fun getCachedExercisesByLevel(level: String): Flow<List<CachedExerciseEntity>>

    @Query("SELECT * FROM cached_exercises WHERE type = :type ORDER BY level ASC")
    fun getCachedExercisesByType(type: String): Flow<List<CachedExerciseEntity>>

    @Query("SELECT COUNT(*) FROM cached_exercises")
    suspend fun getExerciseCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExercises(exercises: List<CachedExerciseEntity>)

    @Query("DELETE FROM cached_exercises")
    suspend fun clearCachedExercises()
}

@Dao
interface CachedVocabDao {
    @Query("SELECT * FROM cached_vocab_bank ORDER BY level ASC, word ASC")
    fun getAllCachedVocab(): Flow<List<CachedVocabEntity>>

    @Query("SELECT * FROM cached_vocab_bank ORDER BY level ASC, word ASC")
    suspend fun getAllCachedVocabSync(): List<CachedVocabEntity>

    @Query("SELECT * FROM cached_vocab_bank WHERE level = :level ORDER BY word ASC")
    fun getCachedVocabByLevel(level: String): Flow<List<CachedVocabEntity>>

    @Query("SELECT * FROM cached_vocab_bank WHERE topic = :topic ORDER BY word ASC")
    fun getCachedVocabByTopic(topic: String): Flow<List<CachedVocabEntity>>

    @Query("SELECT COUNT(*) FROM cached_vocab_bank")
    suspend fun getVocabCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVocabList(items: List<CachedVocabEntity>)

    @Query("DELETE FROM cached_vocab_bank")
    suspend fun clearCachedVocab()
}

@Dao
interface UserBadgeDao {
    @Query("SELECT * FROM user_badges ORDER BY isUnlocked DESC, targetGoal ASC")
    fun getAllBadges(): Flow<List<UserBadgeEntity>>

    @Query("SELECT * FROM user_badges WHERE isUnlocked = 1 ORDER BY unlockedAtTimestamp DESC")
    fun getUnlockedBadges(): Flow<List<UserBadgeEntity>>

    @Query("SELECT * FROM user_badges WHERE badgeId = :badgeId LIMIT 1")
    suspend fun getBadgeById(badgeId: String): UserBadgeEntity?

    @Query("SELECT COUNT(*) FROM user_badges WHERE isUnlocked = 1")
    fun getUnlockedBadgeCount(): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBadges(badges: List<UserBadgeEntity>)

    @Update
    suspend fun updateBadge(badge: UserBadgeEntity)

    @Query("UPDATE user_badges SET currentProgress = :progress, isUnlocked = CASE WHEN :progress >= targetGoal THEN 1 ELSE isUnlocked END, unlockedAtTimestamp = CASE WHEN :progress >= targetGoal AND unlockedAtTimestamp IS NULL THEN :nowTimestamp ELSE unlockedAtTimestamp END WHERE badgeId = :badgeId")
    suspend fun updateBadgeProgress(badgeId: String, progress: Int, nowTimestamp: Long = System.currentTimeMillis())

    @Query("UPDATE user_badges SET isUnlocked = 1, unlockedAtTimestamp = :nowTimestamp WHERE badgeId = :badgeId")
    suspend fun unlockBadge(badgeId: String, nowTimestamp: Long = System.currentTimeMillis())
}

