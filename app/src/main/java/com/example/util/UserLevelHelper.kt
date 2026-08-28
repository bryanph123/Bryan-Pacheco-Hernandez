package com.example.util

import com.example.data.local.entities.ExerciseAttemptEntity
import com.example.data.local.entities.SavedVocabItemEntity
import com.example.data.local.entities.StudySessionEntity
import com.example.data.local.entities.UserBadgeEntity

data class UserLevelInfo(
    val levelNumber: Int,
    val title: String,
    val titleSpanish: String,
    val iconEmoji: String,
    val minXp: Int,
    val maxXp: Int,
    val currentXp: Int,
    val xpInCurrentLevel: Int,
    val xpNeededForNextLevel: Int,
    val progressFraction: Float,
    val perkDescription: String
)

object UserLevelHelper {

    val allLevels = listOf(
        UserLevelDefinition(1, "Curious Beginner", "Principiante Curioso", "🌱", 0, 150, "Acceso a vocabulario A1-A2 y modo Pomodoro básico"),
        UserLevelDefinition(2, "Steady Scholar", "Estudiante Constante", "📘", 150, 400, "Desbloqueo de ejercicios modulares B1 y Flashcards SRS"),
        UserLevelDefinition(3, "Intermediate Explorer", "Explorador Intermedio", "🧭", 400, 800, "Desbloqueo de simulacros Use of English y glosarios ampliados"),
        UserLevelDefinition(4, "B2 First Candidate", "Aspirante Cambridge B2", "🎯", 800, 1400, "Práctica de Key Word Transformations y audio nativo UK/US"),
        UserLevelDefinition(5, "Vocabulary Master", "Maestro del Léxico", "📚", 1400, 2200, "Capacidad ilimitada de guardado SRS y filtros avanzados"),
        UserLevelDefinition(6, "Grammar Specialist", "Especialista en Gramática", "⚡", 2200, 3200, "Desbloqueo de inversiones enfáticas y condicionales mixtos"),
        UserLevelDefinition(7, "Fluent Practitioner", "Comunicador Fluido", "🗣️", 3200, 4500, "Análisis de expresiones idiomáticas y colocaciones formales"),
        UserLevelDefinition(8, "Cambridge Expert", "Experto en Exámenes", "🏆", 4500, 6000, "Simulaciones oficiales B2 cronometradas con puntuación Cambridge"),
        UserLevelDefinition(9, "Distinguished Scholar", "Candidato Distinguido", "🌟", 6000, 8000, "Nivel de precisión >85% en Use of English Part 1-4"),
        UserLevelDefinition(10, "B2+ Grandmaster", "Gran Maestro Cambridge B2+", "👑", 8000, 15000, "Dominio integral del Marco Común Europeo B2/C1")
    )

    data class UserLevelDefinition(
        val levelNumber: Int,
        val title: String,
        val titleSpanish: String,
        val iconEmoji: String,
        val minXp: Int,
        val maxXp: Int,
        val perkDescription: String
    )

    fun calculateLevelInfo(totalXp: Int): UserLevelInfo {
        val safeXp = totalXp.coerceAtLeast(0)
        val currentDef = allLevels.find { safeXp in it.minXp until it.maxXp }
            ?: allLevels.last()

        val range = (currentDef.maxXp - currentDef.minXp).coerceAtLeast(1)
        val xpInLevel = (safeXp - currentDef.minXp).coerceAtLeast(0)
        val xpNeeded = (currentDef.maxXp - safeXp).coerceAtLeast(0)
        val fraction = (xpInLevel.toFloat() / range.toFloat()).coerceIn(0f, 1f)

        return UserLevelInfo(
            levelNumber = currentDef.levelNumber,
            title = currentDef.title,
            titleSpanish = currentDef.titleSpanish,
            iconEmoji = currentDef.iconEmoji,
            minXp = currentDef.minXp,
            maxXp = currentDef.maxXp,
            currentXp = safeXp,
            xpInCurrentLevel = xpInLevel,
            xpNeededForNextLevel = xpNeeded,
            progressFraction = fraction,
            perkDescription = currentDef.perkDescription
        )
    }

    /**
     * Evaluates current user telemetry and updates badges progress accordingly
     */
    fun evaluateBadgesUpdates(
        currentBadges: List<UserBadgeEntity>,
        streakDays: Int,
        totalXp: Int,
        sessions: List<StudySessionEntity>,
        vocabItems: List<SavedVocabItemEntity>,
        attempts: List<ExerciseAttemptEntity>
    ): List<UserBadgeEntity> {
        val now = System.currentTimeMillis()
        val totalStudyMinutes = (sessions.sumOf { it.durationSeconds } / 60).toInt()
        val pomodoroSessionsCount = sessions.count { it.mode.contains("POMODORO", ignoreCase = true) || it.durationSeconds >= 1200 }
        val masteredVocabCount = vocabItems.count { it.masteryLevel >= 5 }
        val savedVocabCount = vocabItems.size
        val attemptsCount = attempts.size
        val perfectQuizCount = attempts.count { it.maxScore > 0 && it.score == it.maxScore }
        val distinctCategoriesPracticed = attempts.map { it.exerciseType }.distinct().size

        return currentBadges.map { badge ->
            val newProgress = when (badge.badgeId) {
                "badge_first_pomodoro" -> pomodoroSessionsCount.coerceAtLeast(badge.currentProgress)
                "badge_streak_3" -> streakDays.coerceAtLeast(badge.currentProgress)
                "badge_streak_7" -> streakDays.coerceAtLeast(badge.currentProgress)
                "badge_streak_14" -> streakDays.coerceAtLeast(badge.currentProgress)
                "badge_vocab_10" -> savedVocabCount.coerceAtLeast(badge.currentProgress)
                "badge_vocab_30" -> savedVocabCount.coerceAtLeast(badge.currentProgress)
                "badge_vocab_master_10" -> masteredVocabCount.coerceAtLeast(badge.currentProgress)
                "badge_exercise_10" -> attemptsCount.coerceAtLeast(badge.currentProgress)
                "badge_exercise_50" -> attemptsCount.coerceAtLeast(badge.currentProgress)
                "badge_perfect_quiz" -> perfectQuizCount.coerceAtLeast(badge.currentProgress)
                "badge_study_100m" -> totalStudyMinutes.coerceAtLeast(badge.currentProgress)
                "badge_all_skills" -> distinctCategoriesPracticed.coerceAtLeast(badge.currentProgress)
                "badge_cambridge_master" -> totalXp.coerceAtLeast(badge.currentProgress)
                else -> badge.currentProgress
            }

            val unlocked = badge.isUnlocked || (newProgress >= badge.targetGoal)
            val unlockedTimestamp = if (unlocked && badge.unlockedAtTimestamp == null) now else badge.unlockedAtTimestamp

            badge.copy(
                currentProgress = newProgress,
                isUnlocked = unlocked,
                unlockedAtTimestamp = unlockedTimestamp
            )
        }
    }
}
