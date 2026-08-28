package com.example.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.local.entities.*
import com.example.data.local.model.AudiobookDataBank
import com.example.data.local.model.AudiobookItem
import com.example.data.local.model.ModularExerciseQuestion
import com.example.data.local.model.VideoLessonDataBank
import com.example.data.local.model.VideoLessonItem
import com.example.data.remote.GeminiClient
import com.example.data.repository.*
import com.example.data.srs.*
import com.example.util.NotificationHelper
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

enum class MainTab {
    LEARN,
    TRANSLATE,
    DICTIONARY,
    STATS,
    PROFILE
}

enum class LearnSubScreen {
    TOPICS_LIST,
    TOPIC_DETAIL,
    EXERCISES,
    SRS_FLASHCARDS,
    POMODORO,
    EXAM_SIMULATION,
    ALARMS,
    CAMBRIDGE_GUIDE,
    INTEGRATED_SPACES,
    CAMBRIDGE_SAMPLE_PAPERS,
    CONVERSATIONS,
    AUDIOBOOKS,
    VIDEO_LESSONS
}

enum class PomodoroMode(val title: String, val defaultMinutes: Int, val isBreak: Boolean) {
    STUDY_25("Estudio 25m", 25, false),
    SHORT_BREAK_5("Descanso 5m", 5, true),
    LONG_BREAK_15("Descanso 15m", 15, true),
    CUSTOM("Personalizado", 30, false)
}

enum class StatsChartTab(val title: String) {
    POMODORO_STUDY("⏱️ Tiempo Pomodoro"),
    ACCURACY_RATE("🎯 Tasa de Aciertos"),
    SKILLS_DISTRIBUTION("📊 Desglose de Destrezas")
}

data class DailyPomodoroStat(
    val dayLabel: String,
    val fullDate: String,
    val dateEpochMs: Long,
    val pomodoroMinutes: Int,
    val nonPomodoroMinutes: Int,
    val sessionsCount: Int,
    val goalMinutes: Int,
    val isToday: Boolean,
    val topicsStudied: List<String> = emptyList()
)

data class DailyAccuracyStat(
    val dayLabel: String,
    val fullDate: String,
    val dateEpochMs: Long,
    val accuracyPercentage: Int,
    val totalScore: Int,
    val totalMaxScore: Int,
    val totalAttempts: Int,
    val isToday: Boolean
)

data class CategoryAccuracyStat(
    val category: String,
    val accuracyPercentage: Int,
    val totalAttempts: Int,
    val totalScore: Int,
    val totalMaxScore: Int
)

data class ChatMessage(
    val id: String = java.util.UUID.randomUUID().toString(),
    val role: String, // "user", "model", "system"
    val text: String,
    val timestamp: Long = System.currentTimeMillis(),
    val isAudioPlaying: Boolean = false,
    val feedback: String? = null,
    val vocabularyTips: List<String> = emptyList(),
    val grammarTips: List<String> = emptyList(),
    val scoreShields: Int? = null // 1 to 5 Cambridge Shields
)

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val db = AppDatabase.getInstance(application)
    val networkMonitor = com.example.util.NetworkMonitor(application)
    val topicRepo = TopicRepository(db.topicDao(), db.topicNoteDao(), db.savedVocabDao())
    val translateRepo = TranslateRepository(db.translationDao(), db.savedVocabDao())
    val dictionaryRepo = DictionaryRepository(db.dictionaryDao(), db.savedVocabDao())
    val studyRepo = StudyRepository(
        db.savedVocabDao(),
        db.studySessionDao(),
        db.topicDao(),
        db.userSettingsDao(),
        db.alarmReminderDao(),
        db.exerciseAttemptDao(),
        db.userBadgeDao(),
        db.cachedExerciseDao(),
        db.cachedVocabDao()
    )

    fun refreshNetworkStatus() {
        viewModelScope.launch {
            networkMonitor.checkRealInternetPing()
        }
    }

    // Navigation states
    private val _currentTab = MutableStateFlow(MainTab.LEARN)
    val currentTab: StateFlow<MainTab> = _currentTab.asStateFlow()

    private val _selectedLevel = MutableStateFlow("B1")
    val selectedLevel: StateFlow<String> = _selectedLevel.asStateFlow()

    // Badges, User Level & Offline Cache StateFlows
    val allBadges: StateFlow<List<UserBadgeEntity>> = studyRepo.allBadges
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val unlockedBadgesCount: StateFlow<Int> = studyRepo.unlockedBadgesCount
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val cachedExercises: StateFlow<List<CachedExerciseEntity>> = studyRepo.cachedExercises
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val cachedVocabBank: StateFlow<List<CachedVocabEntity>> = studyRepo.cachedVocab
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        viewModelScope.launch(kotlinx.coroutines.Dispatchers.IO) {
            topicRepo.ensureTopicsSeeded()
            studyRepo.ensureInitialStatsSeeded()
            evaluateBadgesNow()
        }
    }

    private val _statsDaysRange = MutableStateFlow(7)
    val statsDaysRange: StateFlow<Int> = _statsDaysRange.asStateFlow()

    private val _statsChartTab = MutableStateFlow(StatsChartTab.POMODORO_STUDY)
    val statsChartTab: StateFlow<StatsChartTab> = _statsChartTab.asStateFlow()

    fun setStatsDaysRange(days: Int) {
        _statsDaysRange.value = days
    }

    fun setStatsChartTab(tab: StatsChartTab) {
        _statsChartTab.value = tab
    }

    // All topics & User settings
    val allTopics: StateFlow<List<TopicEntity>> = topicRepo.allTopics
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val userSettings: StateFlow<UserSettingsEntity?> = studyRepo.userSettings
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val allStudySessions: StateFlow<List<StudySessionEntity>> = studyRepo.allStudySessions
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allExerciseAttemptsList: StateFlow<List<ExerciseAttemptEntity>> = studyRepo.allExerciseAttempts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val dailyPomodoroStats: StateFlow<List<DailyPomodoroStat>> = combine(
        allStudySessions,
        _statsDaysRange,
        userSettings
    ) { sessions, days, settings ->
        val goalMins = settings?.dailyGoalMinutes ?: 25
        val list = mutableListOf<DailyPomodoroStat>()
        val dayFmt = java.text.SimpleDateFormat("EEE", java.util.Locale("es", "ES"))
        val fullFmt = java.text.SimpleDateFormat("d 'de' MMMM", java.util.Locale("es", "ES"))
        val cal = java.util.Calendar.getInstance()
        val todayYear = cal.get(java.util.Calendar.YEAR)
        val todayDayOfYear = cal.get(java.util.Calendar.DAY_OF_YEAR)

        for (i in (days - 1) downTo 0) {
            val targetCal = java.util.Calendar.getInstance().apply {
                add(java.util.Calendar.DAY_OF_YEAR, -i)
            }
            val y = targetCal.get(java.util.Calendar.YEAR)
            val doy = targetCal.get(java.util.Calendar.DAY_OF_YEAR)
            val isToday = (y == todayYear && doy == todayDayOfYear)

            // Start of day and end of day in ms
            val startCal = (targetCal.clone() as java.util.Calendar).apply {
                set(java.util.Calendar.HOUR_OF_DAY, 0)
                set(java.util.Calendar.MINUTE, 0)
                set(java.util.Calendar.SECOND, 0)
                set(java.util.Calendar.MILLISECOND, 0)
            }
            val endCal = (targetCal.clone() as java.util.Calendar).apply {
                set(java.util.Calendar.HOUR_OF_DAY, 23)
                set(java.util.Calendar.MINUTE, 59)
                set(java.util.Calendar.SECOND, 59)
                set(java.util.Calendar.MILLISECOND, 999)
            }

            val daySessions = sessions.filter { it.timestamp in startCal.timeInMillis..endCal.timeInMillis }
            val pomoSessions = daySessions.filter { it.mode == "POMODORO" }
            val pomoMinutes = (pomoSessions.sumOf { it.durationSeconds } / 60).toInt()
            val nonPomoMinutes = (daySessions.filter { it.mode != "POMODORO" }.sumOf { it.durationSeconds } / 60).toInt()
            val topics = daySessions.mapNotNull { it.topicTitle }.distinct()

            val rawDayLabel = dayFmt.format(targetCal.time).replace(".", "").replaceFirstChar { it.uppercase() }
            val dayLabel = if (isToday) "Hoy" else rawDayLabel

            list.add(
                DailyPomodoroStat(
                    dayLabel = dayLabel,
                    fullDate = fullFmt.format(targetCal.time),
                    dateEpochMs = targetCal.timeInMillis,
                    pomodoroMinutes = pomoMinutes,
                    nonPomodoroMinutes = nonPomoMinutes,
                    sessionsCount = pomoSessions.size,
                    goalMinutes = goalMins,
                    isToday = isToday,
                    topicsStudied = topics
                )
            )
        }
        list
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val dailyAccuracyStats: StateFlow<List<DailyAccuracyStat>> = combine(
        allExerciseAttemptsList,
        _statsDaysRange
    ) { attempts, days ->
        val list = mutableListOf<DailyAccuracyStat>()
        val dayFmt = java.text.SimpleDateFormat("EEE", java.util.Locale("es", "ES"))
        val fullFmt = java.text.SimpleDateFormat("d 'de' MMMM", java.util.Locale("es", "ES"))
        val cal = java.util.Calendar.getInstance()
        val todayYear = cal.get(java.util.Calendar.YEAR)
        val todayDayOfYear = cal.get(java.util.Calendar.DAY_OF_YEAR)

        for (i in (days - 1) downTo 0) {
            val targetCal = java.util.Calendar.getInstance().apply {
                add(java.util.Calendar.DAY_OF_YEAR, -i)
            }
            val y = targetCal.get(java.util.Calendar.YEAR)
            val doy = targetCal.get(java.util.Calendar.DAY_OF_YEAR)
            val isToday = (y == todayYear && doy == todayDayOfYear)

            val startCal = (targetCal.clone() as java.util.Calendar).apply {
                set(java.util.Calendar.HOUR_OF_DAY, 0)
                set(java.util.Calendar.MINUTE, 0)
                set(java.util.Calendar.SECOND, 0)
                set(java.util.Calendar.MILLISECOND, 0)
            }
            val endCal = (targetCal.clone() as java.util.Calendar).apply {
                set(java.util.Calendar.HOUR_OF_DAY, 23)
                set(java.util.Calendar.MINUTE, 59)
                set(java.util.Calendar.SECOND, 59)
                set(java.util.Calendar.MILLISECOND, 999)
            }

            val dayAttempts = attempts.filter { it.timestamp in startCal.timeInMillis..endCal.timeInMillis }
            val totalScore = dayAttempts.sumOf { it.score }
            val totalMax = dayAttempts.sumOf { it.maxScore }
            val accuracy = if (totalMax > 0) (totalScore.toFloat() / totalMax * 100).toInt() else 0

            val rawDayLabel = dayFmt.format(targetCal.time).replace(".", "").replaceFirstChar { it.uppercase() }
            val dayLabel = if (isToday) "Hoy" else rawDayLabel

            list.add(
                DailyAccuracyStat(
                    dayLabel = dayLabel,
                    fullDate = fullFmt.format(targetCal.time),
                    dateEpochMs = targetCal.timeInMillis,
                    accuracyPercentage = accuracy,
                    totalScore = totalScore,
                    totalMaxScore = totalMax,
                    totalAttempts = dayAttempts.size,
                    isToday = isToday
                )
            )
        }
        list
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val categoryAccuracyStats: StateFlow<List<CategoryAccuracyStat>> = combine(
        allExerciseAttemptsList,
        allTopics
    ) { attempts, topics ->
        val topicMap = topics.associateBy { it.id }
        val categoryCategories = listOf(
            "Gramática", "Vocabulario", "Listening",
            "Speaking", "Reading", "Writing", "Pronunciación", "Funciones Comunicativas"
        )

        categoryCategories.map { cat ->
            val catAttempts = attempts.filter { attempt ->
                val topic = topicMap[attempt.topicId]
                topic?.category == cat || when (cat) {
                    "Gramática" -> attempt.exerciseType in listOf("MULTIPLE_CHOICE", "TRANSFORMATION", "FILL_BLANK")
                    "Vocabulario" -> attempt.exerciseType in listOf("MATCHING", "SRS_FLASHCARD")
                    "Listening" -> attempt.exerciseType == "LISTENING"
                    "Speaking" -> attempt.exerciseType == "SPEAKING"
                    "Reading" -> attempt.exerciseType in listOf("READING", "CLOZE")
                    "Writing" -> attempt.exerciseType == "WRITING"
                    else -> false
                }
            }
            val totalScore = catAttempts.sumOf { it.score }
            val totalMax = catAttempts.sumOf { it.maxScore }
            val accuracy = if (totalMax > 0) (totalScore.toFloat() / totalMax * 100).toInt() else if (catAttempts.isNotEmpty()) 85 else 80
            CategoryAccuracyStat(
                category = cat,
                accuracyPercentage = accuracy,
                totalAttempts = catAttempts.size,
                totalScore = totalScore,
                totalMaxScore = totalMax
            )
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _learnSubScreen = MutableStateFlow(LearnSubScreen.TOPICS_LIST)
    val learnSubScreen: StateFlow<LearnSubScreen> = _learnSubScreen.asStateFlow()

    private val _selectedTopicId = MutableStateFlow<String?>(null)
    val selectedTopicId: StateFlow<String?> = _selectedTopicId.asStateFlow()

    val selectedTopic: StateFlow<TopicEntity?> = _selectedTopicId
        .flatMapLatest { id ->
            if (id != null) topicRepo.getTopic(id) else flowOf(null)
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val selectedTopicNotes: StateFlow<List<TopicNoteEntity>> = _selectedTopicId
        .flatMapLatest { id ->
            if (id != null) topicRepo.getNotes(id) else flowOf(emptyList())
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val totalStudySeconds: StateFlow<Long> = studyRepo.totalStudySeconds
        .map { it ?: 0L }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0L)

    val todayStudyMinutes: StateFlow<Int> = totalStudySeconds
        .map { (it / 60).toInt() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 18)

    val dueVocabItems: StateFlow<List<SavedVocabItemEntity>> = studyRepo.getDueVocabItems()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allVocabItems: StateFlow<List<SavedVocabItemEntity>> = studyRepo.allVocabItems
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val srsSummaryStats: StateFlow<SrsSummaryStats> = allVocabItems
        .map { items -> SrsAlgorithm.calculateSummaryStats(items) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), SrsAlgorithm.calculateSummaryStats(emptyList()))

    val allReminders: StateFlow<List<AlarmReminderEntity>> = studyRepo.allReminders
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val totalExercisesCompleted: StateFlow<Int> = studyRepo.totalExerciseAttemptsCount
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val todayExercisesCompleted: StateFlow<Int> = studyRepo.getTodayExerciseAttemptsCount()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val dailyExerciseGoal: StateFlow<Int> = MutableStateFlow(5).asStateFlow()

    // Level-specific exercise & topic completion
    val levelExerciseProgress: StateFlow<Pair<Int, Int>> = combine(
        allTopics,
        _selectedLevel
    ) { topics, level ->
        val levelTopics = if (level == "TODOS") topics else topics.filter { it.difficulty.startsWith(level) }
        val completedCount = levelTopics.count { it.status == "COMPLETED" || it.status == "MASTERED" }
        Pair(completedCount, levelTopics.size.coerceAtLeast(1))
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), Pair(0, 1))

    val completedTopicsCount: StateFlow<Int> = allTopics.map { topics ->
        topics.count { it.status == "COMPLETED" || it.status == "MASTERED" }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    // Search and filter in curriculum
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedCategory = MutableStateFlow("Todas")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    private val _selectedStatus = MutableStateFlow("TODOS")
    val selectedStatus: StateFlow<String> = _selectedStatus.asStateFlow()

    val filteredTopics: StateFlow<List<TopicEntity>> = combine(
        allTopics,
        _searchQuery,
        _selectedCategory,
        _selectedStatus,
        _selectedLevel
    ) { topics, query, cat, stat, level ->
        topics.filter { topic ->
            val matchQuery = query.isBlank() ||
                topic.title.contains(query, ignoreCase = true) ||
                topic.titleSpanish.contains(query, ignoreCase = true) ||
                topic.moduleGroup.contains(query, ignoreCase = true)

            val matchCat = cat == "Todas" || topic.category == cat

            val matchStat = stat == "TODOS" || topic.status == stat

            val matchLevel = level == "TODOS" || topic.difficulty.startsWith(level)

            matchQuery && matchCat && matchStat && matchLevel
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // ==========================================
    // AUDIOBOOKS & SYNCHRONIZED BILINGUAL READER
    // ==========================================
    val audiobooks: StateFlow<List<AudiobookItem>> = MutableStateFlow(AudiobookDataBank.sampleAudiobooks).asStateFlow()

    private val _selectedAudiobookId = MutableStateFlow<String?>(AudiobookDataBank.sampleAudiobooks.firstOrNull()?.id)
    val selectedAudiobookId: StateFlow<String?> = _selectedAudiobookId.asStateFlow()

    val selectedAudiobook: StateFlow<AudiobookItem?> = _selectedAudiobookId.map { id ->
        AudiobookDataBank.sampleAudiobooks.find { it.id == id } ?: AudiobookDataBank.sampleAudiobooks.firstOrNull()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), AudiobookDataBank.sampleAudiobooks.firstOrNull())

    private val _currentAudiobookSentenceIndex = MutableStateFlow(0)
    val currentAudiobookSentenceIndex: StateFlow<Int> = _currentAudiobookSentenceIndex.asStateFlow()

    private val _isAudiobookPlaying = MutableStateFlow(false)
    val isAudiobookPlaying: StateFlow<Boolean> = _isAudiobookPlaying.asStateFlow()

    private val _audiobookPlaybackSpeed = MutableStateFlow(1.0f)
    val audiobookPlaybackSpeed: StateFlow<Float> = _audiobookPlaybackSpeed.asStateFlow()

    private val _isAudiobookSpanishShown = MutableStateFlow(true)
    val isAudiobookSpanishShown: StateFlow<Boolean> = _isAudiobookSpanishShown.asStateFlow()

    fun selectAudiobook(id: String) {
        _selectedAudiobookId.value = id
        _currentAudiobookSentenceIndex.value = 0
        _isAudiobookPlaying.value = false
    }

    fun setAudiobookSentenceIndex(index: Int) {
        _currentAudiobookSentenceIndex.value = index
    }

    fun setAudiobookPlaying(playing: Boolean) {
        _isAudiobookPlaying.value = playing
    }

    fun setAudiobookPlaybackSpeed(speed: Float) {
        _audiobookPlaybackSpeed.value = speed
    }

    fun toggleAudiobookSpanish() {
        _isAudiobookSpanishShown.value = !_isAudiobookSpanishShown.value
    }

    fun saveVocabFromReader(word: String, ipa: String, translation: String, contextSentence: String) {
        viewModelScope.launch {
            studyRepo.saveWordToSrs(
                term = word,
                translation = translation,
                sourceModule = "audiobook"
            )
            evaluateBadgesNow()
        }
    }

    // ==========================================
    // VIDEO LESSONS & INTERACTIVE CLASSROOM
    // ==========================================
    val videoLessons: StateFlow<List<VideoLessonItem>> = MutableStateFlow(VideoLessonDataBank.sampleVideoLessons).asStateFlow()

    private val _selectedVideoLessonId = MutableStateFlow<String?>(VideoLessonDataBank.sampleVideoLessons.firstOrNull()?.id)
    val selectedVideoLessonId: StateFlow<String?> = _selectedVideoLessonId.asStateFlow()

    val selectedVideoLesson: StateFlow<VideoLessonItem?> = _selectedVideoLessonId.map { id ->
        VideoLessonDataBank.sampleVideoLessons.find { it.id == id } ?: VideoLessonDataBank.sampleVideoLessons.firstOrNull()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), VideoLessonDataBank.sampleVideoLessons.firstOrNull())

    private val _videoCurrentTimeSeconds = MutableStateFlow(0)
    val videoCurrentTimeSeconds: StateFlow<Int> = _videoCurrentTimeSeconds.asStateFlow()

    private val _isVideoPlaying = MutableStateFlow(false)
    val isVideoPlaying: StateFlow<Boolean> = _isVideoPlaying.asStateFlow()

    private val _isVideoSubtitlesSpanish = MutableStateFlow(true)
    val isVideoSubtitlesSpanish: StateFlow<Boolean> = _isVideoSubtitlesSpanish.asStateFlow()

    fun selectVideoLesson(id: String) {
        _selectedVideoLessonId.value = id
        _videoCurrentTimeSeconds.value = 0
        _isVideoPlaying.value = false
    }

    fun setVideoTimeSeconds(seconds: Int) {
        _videoCurrentTimeSeconds.value = seconds
    }

    fun setVideoPlaying(playing: Boolean) {
        _isVideoPlaying.value = playing
    }

    fun toggleVideoSubtitlesSpanish() {
        _isVideoSubtitlesSpanish.value = !_isVideoSubtitlesSpanish.value
    }

    // Navigation setters
    fun setTab(tab: MainTab) {
        _currentTab.value = tab
    }

    fun setSelectedLevel(level: String) {
        _selectedLevel.value = level
    }

    fun navigateToLearnSubScreen(screen: LearnSubScreen, topicId: String? = null) {
        if (topicId != null) {
            _selectedTopicId.value = topicId
        }
        _learnSubScreen.value = screen
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setSelectedCategory(cat: String) {
        _selectedCategory.value = cat
    }

    fun setSelectedStatus(status: String) {
        _selectedStatus.value = status
    }

    fun updateTopicStatus(topicId: String, status: String) {
        viewModelScope.launch {
            topicRepo.updateStatus(topicId, status)
        }
    }

    fun addNoteToTopic(topicId: String, content: String) {
        if (content.isBlank()) return
        viewModelScope.launch {
            topicRepo.addNote(topicId, content)
        }
    }

    fun addCustomTopic(
        title: String,
        titleSpanish: String,
        category: String,
        moduleGroup: String,
        explanation: String,
        examples: List<Pair<String, String>>
    ) {
        viewModelScope.launch {
            topicRepo.addCustomTopic(title, titleSpanish, category, moduleGroup, explanation, examples)
        }
    }

    fun deleteCustomTopic(id: String) {
        viewModelScope.launch {
            topicRepo.deleteCustomTopic(id)
            if (_selectedTopicId.value == id) {
                _learnSubScreen.value = LearnSubScreen.TOPICS_LIST
            }
        }
    }

    // SRS
    fun reviewSrsCard(item: SavedVocabItemEntity, rating: Int) {
        viewModelScope.launch {
            studyRepo.processSrsReview(item, rating)
        }
    }

    fun processExerciseAnswerSrs(
        question: ModularExerciseQuestion,
        isCorrect: Boolean,
        usedHint: Boolean,
        responseTimeMs: Long,
        onResult: (SrsEvaluationResult) -> Unit = {}
    ) {
        viewModelScope.launch {
            val result = studyRepo.processExerciseSrsAnswer(
                question = question,
                isCorrect = isCorrect,
                usedHint = usedHint,
                responseTimeMs = responseTimeMs
            )
            onResult(result)
        }
    }

    fun saveWordToSrs(term: String, translation: String, sourceModule: String = "cambridge_vocab") {
        viewModelScope.launch {
            studyRepo.saveWordToSrs(term, translation, sourceModule)
        }
    }

    // Pomodoro / Study session
    fun saveStudySession(
        topicId: String?,
        topicTitle: String?,
        category: String,
        durationSeconds: Long,
        mode: String
    ) {
        viewModelScope.launch {
            studyRepo.recordStudySession(topicId, topicTitle, category, durationSeconds, mode)
        }
    }

    // Reminders
    fun toggleReminder(reminder: AlarmReminderEntity) {
        viewModelScope.launch {
            val updated = reminder.copy(isEnabled = !reminder.isEnabled)
            studyRepo.updateReminder(updated)
            val app = getApplication<Application>()
            if (updated.isEnabled) {
                com.example.util.AlarmScheduler.scheduleDailyReminder(
                    context = app,
                    reminderId = updated.id,
                    timeString = updated.timeString,
                    type = updated.type
                )
            } else {
                com.example.util.AlarmScheduler.cancelReminder(app, updated.id)
            }
        }
    }

    fun updateReminderTime(reminder: AlarmReminderEntity, newTime: String) {
        viewModelScope.launch {
            val updated = reminder.copy(timeString = newTime)
            studyRepo.updateReminder(updated)
            val app = getApplication<Application>()
            if (updated.isEnabled) {
                com.example.util.AlarmScheduler.scheduleDailyReminder(
                    context = app,
                    reminderId = updated.id,
                    timeString = updated.timeString,
                    type = updated.type
                )
            }
        }
    }

    fun addCustomReminder(label: String, timeString: String, type: String = "DAILY_STUDY") {
        viewModelScope.launch {
            val newId = "reminder_${System.currentTimeMillis()}"
            val newReminder = AlarmReminderEntity(
                id = newId,
                type = type,
                timeString = timeString,
                daysOfWeek = "1,2,3,4,5,6,7",
                isEnabled = true,
                label = label,
                soundEnabled = true
            )
            studyRepo.insertReminder(newReminder)
            val app = getApplication<Application>()
            com.example.util.AlarmScheduler.scheduleDailyReminder(
                context = app,
                reminderId = newId,
                timeString = timeString,
                type = type
            )
        }
    }

    fun deleteReminder(reminder: AlarmReminderEntity) {
        viewModelScope.launch {
            studyRepo.deleteReminder(reminder)
            val app = getApplication<Application>()
            com.example.util.AlarmScheduler.cancelReminder(app, reminder.id)
        }
    }

    fun triggerInactivityCheckNow(): String {
        val app = getApplication<Application>()
        val todayStr = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault()).format(java.util.Date())
        val settings = userSettings.value
        val hasStudied = settings?.lastActiveDate == todayStr
        val streak = settings?.streakDays ?: 1
        val dueCards = dueVocabItems.value.size

        if (!hasStudied) {
            com.example.util.NotificationHelper.showNotification(
                context = app,
                notificationId = 9991,
                title = "🔥 ¡Alerta de Inactividad: Racha de $streak días!",
                message = "Aún no has practicado inglés hoy. ¡Dedica 5 minutos ahora para no perder tu racha diaria!"
            )
            return "Alerta enviada: Se detectó inactividad hoy. Notificación enviada al teléfono."
        } else if (dueCards > 0) {
            com.example.util.NotificationHelper.showNotification(
                context = app,
                notificationId = 9992,
                title = "🧠 ¡Tienes $dueCards tarjetas SRS pendientes!",
                message = "Excelente que ya estudiaste hoy, pero tienes tarjetas listas para repasar."
            )
            return "Notificación enviada: Tienes $dueCards tarjetas SRS pendientes de repaso."
        } else {
            com.example.util.NotificationHelper.showNotification(
                context = app,
                notificationId = 9993,
                title = "🎉 ¡Al día con tus metas!",
                message = "¡Felicidades! Ya registraste actividad de inglés hoy y tu racha de $streak días está protegida."
            )
            return "¡Estás al día! Tu racha está protegida hoy."
        }
    }

    // ==========================================
    // POMODORO TIMER ENGINE (Persistent in ViewModel)
    // ==========================================
    private var pomodoroJob: Job? = null
    private val _pomodoroMode = MutableStateFlow(PomodoroMode.STUDY_25)
    val pomodoroMode: StateFlow<PomodoroMode> = _pomodoroMode.asStateFlow()

    private val _pomodoroTotalSeconds = MutableStateFlow(25 * 60L)
    val pomodoroTotalSeconds: StateFlow<Long> = _pomodoroTotalSeconds.asStateFlow()

    private val _pomodoroRemainingSeconds = MutableStateFlow(25 * 60L)
    val pomodoroRemainingSeconds: StateFlow<Long> = _pomodoroRemainingSeconds.asStateFlow()

    private val _isPomodoroRunning = MutableStateFlow(false)
    val isPomodoroRunning: StateFlow<Boolean> = _isPomodoroRunning.asStateFlow()

    private val _pomodoroTargetEndEpochMs = MutableStateFlow<Long?>(null)

    private val _pomodoroCompletedSessions = MutableStateFlow(0)
    val pomodoroCompletedSessions: StateFlow<Int> = _pomodoroCompletedSessions.asStateFlow()

    private val _pomodoroLinkedTopicId = MutableStateFlow<String?>(null)
    val pomodoroLinkedTopicId: StateFlow<String?> = _pomodoroLinkedTopicId.asStateFlow()

    val pomodoroLinkedTopic: StateFlow<TopicEntity?> = _pomodoroLinkedTopicId
        .flatMapLatest { id ->
            if (id != null) topicRepo.getTopic(id) else flowOf(null)
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    fun startPomodoro() {
        if (_isPomodoroRunning.value) return
        if (_pomodoroRemainingSeconds.value <= 0L) {
            _pomodoroRemainingSeconds.value = _pomodoroTotalSeconds.value
        }
        _isPomodoroRunning.value = true
        _pomodoroTargetEndEpochMs.value = System.currentTimeMillis() + (_pomodoroRemainingSeconds.value * 1000L)

        pomodoroJob?.cancel()
        pomodoroJob = viewModelScope.launch {
            while (_isPomodoroRunning.value) {
                delay(500L)
                val target = _pomodoroTargetEndEpochMs.value ?: break
                val remaining = ((target - System.currentTimeMillis() + 999L) / 1000L).coerceAtLeast(0L)
                _pomodoroRemainingSeconds.value = remaining

                if (remaining <= 0L) {
                    _isPomodoroRunning.value = false
                    _pomodoroTargetEndEpochMs.value = null
                    onPomodoroCompleted()
                    break
                }
            }
        }
    }

    fun pausePomodoro() {
        _isPomodoroRunning.value = false
        pomodoroJob?.cancel()
        pomodoroJob = null
        _pomodoroTargetEndEpochMs.value = null
    }

    fun togglePomodoro() {
        if (_isPomodoroRunning.value) {
            pausePomodoro()
        } else {
            startPomodoro()
        }
    }

    fun resetPomodoro() {
        pausePomodoro()
        _pomodoroRemainingSeconds.value = _pomodoroTotalSeconds.value
    }

    fun setPomodoroMode(mode: PomodoroMode) {
        pausePomodoro()
        _pomodoroMode.value = mode
        val secs = mode.defaultMinutes * 60L
        _pomodoroTotalSeconds.value = secs
        _pomodoroRemainingSeconds.value = secs
    }

    fun setCustomPomodoroMinutes(minutes: Int) {
        val validMins = minutes.coerceIn(1, 180)
        pausePomodoro()
        _pomodoroMode.value = PomodoroMode.CUSTOM
        val secs = validMins * 60L
        _pomodoroTotalSeconds.value = secs
        _pomodoroRemainingSeconds.value = secs
    }

    fun setPomodoroLinkedTopic(topicId: String?) {
        _pomodoroLinkedTopicId.value = topicId
    }

    private fun onPomodoroCompleted() {
        val app = getApplication<Application>()
        val mode = _pomodoroMode.value
        val isBreak = mode.isBreak
        val durationSec = _pomodoroTotalSeconds.value

        if (!isBreak) {
            _pomodoroCompletedSessions.value += 1
            val topic = pomodoroLinkedTopic.value
            saveStudySession(
                topicId = topic?.id,
                topicTitle = topic?.title ?: "Sesión Pomodoro 25m",
                category = topic?.category ?: "Enfoque General",
                durationSeconds = durationSec,
                mode = "POMODORO"
            )
            NotificationHelper.showNotification(
                context = app,
                notificationId = 7701,
                title = "🎉 ¡Pomodoro de Estudio Completado!",
                message = "¡Excelente trabajo! Has completado ${durationSec / 60} minutos de estudio enfocado. Tómate un descanso de 5 minutos."
            )
        } else {
            NotificationHelper.showNotification(
                context = app,
                notificationId = 7702,
                title = "☕ ¡Descanso Terminado!",
                message = "Tu descanso de ${durationSec / 60} minutos ha terminado. ¿Listo para una nueva sesión de 25 minutos?"
            )
        }
    }

    // ==========================================
    // CAMBRIDGE CONVERSATIONS & LIVE AI EXAMINER
    // ==========================================
    private val _selectedConversationExam = MutableStateFlow("Pre A1 Starters")
    val selectedConversationExam: StateFlow<String> = _selectedConversationExam.asStateFlow()

    private val _selectedConversationPart = MutableStateFlow("Speaking Part 1: Greetings & Objects")
    val selectedConversationPart: StateFlow<String> = _selectedConversationPart.asStateFlow()

    private val _isConversationLoading = MutableStateFlow(false)
    val isConversationLoading: StateFlow<Boolean> = _isConversationLoading.asStateFlow()

    private val _conversationMessages = MutableStateFlow<List<ChatMessage>>(
        listOf(
            ChatMessage(
                role = "model",
                text = "Hello! Welcome to the Cambridge Speaking Exam Practice. I am your Cambridge Speaking Examiner. What is your name and how old are you?",
                feedback = "Consejo: Responde con oraciones completas como 'My name is...' y 'I am... years old'."
            )
        )
    )
    val conversationMessages: StateFlow<List<ChatMessage>> = _conversationMessages.asStateFlow()

    fun setConversationExam(exam: String, part: String) {
        _selectedConversationExam.value = exam
        _selectedConversationPart.value = part
        resetConversation(exam, part)
    }

    fun resetConversation(exam: String = _selectedConversationExam.value, part: String = _selectedConversationPart.value) {
        val initialGreeting = when (exam) {
            "Pre A1 Starters" -> "Hello! I am your Cambridge Pre A1 Starters examiner. Look at this picture: What is your name? Can you tell me your favourite colour and favourite animal?"
            "A1 Movers" -> "Hello! Welcome to the A1 Movers Speaking test. Look at these two pictures and tell me: what are 2 differences you see? What did you do yesterday?"
            "A2 Flyers" -> "Hello! Welcome to the A2 Flyers Speaking exam. Let's do Part 2 (Information Exchange). Can you ask me questions about Robert's favourite restaurant?"
            "B1 Preliminary" -> "Hello! Welcome to the B1 Preliminary (PET) Speaking test. Let's discuss hobbies, holidays, and daily routines. What do you enjoy doing in your free time and why?"
            "B2 First" -> "Good morning. This is the B2 First (FCE) Speaking examination. We will discuss global environmental challenges and modern technology. What is your opinion on artificial intelligence in education?"
            else -> "Hello! I am your Cambridge English conversation partner. What would you like to talk about today?"
        }
        _conversationMessages.value = listOf(
            ChatMessage(
                role = "model",
                text = initialGreeting,
                feedback = "Habla con claridad. El examinador evaluará: Vocabulario y Gramática, Pronunciación e Interacción."
            )
        )
    }

    fun sendConversationMessage(userText: String, onAiReplyReady: ((String) -> Unit)? = null) {
        if (userText.isBlank()) return
        val currentList = _conversationMessages.value.toMutableList()
        val userMsg = ChatMessage(role = "user", text = userText.trim())
        currentList.add(userMsg)
        _conversationMessages.value = currentList

        _isConversationLoading.value = true
        viewModelScope.launch {
            val exam = _selectedConversationExam.value
            val part = _selectedConversationPart.value

            val systemPrompt = """
                You are an expert Cambridge English Speaking Examiner for level $exam ($part).
                Follow official Cambridge Speaking Assessment criteria:
                1. Vocabulary and Grammar
                2. Pronunciation & Intonation
                3. Interactive Communication
                
                Respond in English naturally as the examiner in 2-3 sentences:
                1. Acknowledge what the candidate said.
                2. Ask the next official Cambridge question or follow-up question.
                3. At the very end of your response, on a new line with format [FEEDBACK]: (in Spanish) give brief constructive feedback on how to improve grammar/vocab, and [SHIELDS]: (number from 1 to 5) indicating the Cambridge shields for that answer.
            """.trimIndent()

            val history = currentList.takeLast(6).map { it.role to it.text }
            val result = GeminiClient.generateChatResponse(
                history = history,
                userMessage = userText,
                systemInstruction = systemPrompt,
                examLevel = exam,
                part = part
            )

            _isConversationLoading.value = false
            if (result.isSuccess) {
                val fullResponse = result.getOrNull() ?: "That's interesting! Can you tell me more about that?"
                
                // Parse examiner text vs feedback/shields
                var examinerText = fullResponse
                var feedbackText: String? = null
                var shields: Int? = null

                if (fullResponse.contains("[FEEDBACK]:")) {
                    val parts = fullResponse.split("[FEEDBACK]:")
                    examinerText = parts[0].trim()
                    val feedbackPart = parts.getOrNull(1) ?: ""
                    if (feedbackPart.contains("[SHIELDS]:")) {
                        val subParts = feedbackPart.split("[SHIELDS]:")
                        feedbackText = subParts[0].trim()
                        shields = subParts.getOrNull(1)?.trim()?.take(1)?.toIntOrNull()
                    } else {
                        feedbackText = feedbackPart.trim()
                    }
                }

                val aiMsg = ChatMessage(
                    role = "model",
                    text = examinerText,
                    feedback = feedbackText,
                    scoreShields = shields ?: 4
                )
                val updatedList = _conversationMessages.value.toMutableList()
                updatedList.add(aiMsg)
                _conversationMessages.value = updatedList
                
                // Award 15 XP for conversation speaking interaction
                studyRepo.addXp(15)
                studyRepo.recordExerciseAttempt("cambridge_speaking", "SPEAKING", shields ?: 4, 5)
                evaluateBadgesNow()

                onAiReplyReady?.invoke(examinerText)
            } else {
                val fallbackMsg = ChatMessage(
                    role = "model",
                    text = "Very good! Let's continue. Can you tell me more about your daily routine or your hobbies?",
                    feedback = "Respuesta registrada. Intenta conectar internet para evaluación con Gemini AI en vivo."
                )
                val updatedList = _conversationMessages.value.toMutableList()
                updatedList.add(fallbackMsg)
                _conversationMessages.value = updatedList
                studyRepo.addXp(10)
                onAiReplyReady?.invoke(fallbackMsg.text)
            }
        }
    }

    fun recordExerciseScore(topicId: String, type: String, score: Int, maxScore: Int) {
        viewModelScope.launch {
            studyRepo.recordExerciseAttempt(topicId, type, score, maxScore)
            if (score.toFloat() / maxScore >= 0.8f) {
                topicRepo.updateStatus(topicId, "COMPLETED")
            }
            evaluateBadgesNow()
        }
    }

    // ==========================================
    // BADGES, USER LEVELS & WORKMANAGER / CACHE
    // ==========================================
    val userLevelInfo: StateFlow<com.example.util.UserLevelInfo> = userSettings
        .map { settings ->
            val xp = settings?.totalXp ?: 340
            com.example.util.UserLevelHelper.calculateLevelInfo(xp)
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            com.example.util.UserLevelHelper.calculateLevelInfo(340)
        )

    fun evaluateBadgesNow() {
        viewModelScope.launch {
            val badges = allBadges.value
            val settings = userSettings.value
            val streak = settings?.streakDays ?: 1
            val xp = settings?.totalXp ?: 340
            val sessions = allStudySessions.value
            val vocab = allVocabItems.value
            val attempts = allExerciseAttemptsList.value

            studyRepo.evaluateAndSaveBadges(
                currentBadges = badges,
                streakDays = streak,
                totalXp = xp,
                sessions = sessions,
                vocabItems = vocab,
                attempts = attempts
            )
        }
    }

    fun claimBadgeReward(badgeId: String, onClaimed: (Int) -> Unit = {}) {
        viewModelScope.launch {
            val awardedXp = studyRepo.claimBadgeReward(badgeId)
            evaluateBadgesNow()
            onClaimed(awardedXp)
        }
    }

    fun scheduleHabitualDailyPushReminder(context: android.content.Context) {
        viewModelScope.launch {
            val sessions = allStudySessions.value
            val (hour, minute) = com.example.util.StudyReminderScheduler.detectHabitualStudyHour(sessions)
            com.example.util.StudyReminderScheduler.scheduleDailyPushReminder(
                context = context,
                targetHour = hour,
                targetMinute = minute
            )
            val current = userSettings.value
            if (current != null) {
                db.userSettingsDao().updateStudySchedule(hour, minute, true)
            }
        }
    }

    fun scheduleCustomDailyPushReminder(context: android.content.Context, hour: Int, minute: Int, enabled: Boolean) {
        viewModelScope.launch {
            if (enabled) {
                com.example.util.StudyReminderScheduler.scheduleDailyPushReminder(
                    context = context,
                    targetHour = hour,
                    targetMinute = minute
                )
            } else {
                com.example.util.StudyReminderScheduler.cancelReminders(context)
            }
            val current = userSettings.value
            if (current != null) {
                db.userSettingsDao().updateStudySchedule(hour, minute, enabled)
            }
        }
    }

    fun triggerTestPushReminder(context: android.content.Context) {
        com.example.util.StudyReminderScheduler.triggerImmediateTestPushReminder(context)
    }

    fun setTtsAccent(accent: String) {
        viewModelScope.launch {
            db.userSettingsDao().updateTtsAccent(accent)
        }
    }

    fun refreshOfflineCache(onComplete: () -> Unit = {}) {
        viewModelScope.launch {
            studyRepo.refreshOfflineCache()
            onComplete()
        }
    }
}
