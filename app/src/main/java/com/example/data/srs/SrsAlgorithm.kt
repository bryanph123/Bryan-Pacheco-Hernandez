package com.example.data.srs

import com.example.data.local.entities.SavedVocabItemEntity
import com.example.data.local.model.ModularExerciseQuestion
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.max
import kotlin.math.roundToInt

/**
 * SRS Quality Rating enum for review grading
 */
enum class SrsReviewQuality(
    val grade: Int,
    val title: String,
    val description: String,
    val colorHex: Long
) {
    AGAIN(1, "Repetir (Olvidado)", "No recordé el término o fallé el ejercicio", 0xFFE53935),
    HARD(2, "Difícil (Con Fallos)", "Cometí errores o tardé mucho en responder", 0xFFFB8C00),
    PASS_WITH_HINT(3, "Regular (Con Pista)", "Respondí bien pero necesité la pista", 0xFF0288D1),
    GOOD(4, "Bien (Correcto)", "Recordé el término con confianza normal", 0xFF43A047),
    EASY(5, "Fácil (Inmediato)", "Respuesta instantánea y perfecta sin dudar", 0xFF2E7D32)
}

/**
 * Spaced Repetition Review Priority for Question Selection
 */
enum class SrsFrequencyPriority(
    val title: String,
    val badgeLabel: String,
    val colorHex: Long,
    val baseWeight: Float
) {
    URGENT_DUE("Repaso Urgente", "🔴 Vencido", 0xFFE53935, 120f),
    HIGH_FREQUENCY_LEARNING("En Aprendizaje", "🟡 Frecuencia Alta", 0xFFFB8C00, 85f),
    MEDIUM_FREQUENCY_REVIEW("En Consolidación", "🔵 Frecuencia Media", 0xFF1E88E5, 45f),
    LOW_FREQUENCY_MASTERED("Dominado", "🟢 Frecuencia Baja", 0xFF43A047, 15f),
    NEW_UNGRADED("Nuevo Término", "✨ Nuevo", 0xFF8E24AA, 50f)
}

/**
 * Comprehensive result of evaluating an exercise answer with SRS
 */
data class SrsEvaluationResult(
    val targetWord: String,
    val translation: String,
    val quality: Int,
    val qualityLabel: String,
    val isCorrect: Boolean,
    val previousRepetition: Int,
    val newRepetition: Int,
    val previousIntervalDays: Int,
    val newIntervalDays: Int,
    val intervalDeltaDays: Int,
    val previousEaseFactor: Float,
    val newEaseFactor: Float,
    val previousMasteryLevel: Int,
    val newMasteryLevel: Int,
    val nextReviewTimestamp: Long,
    val humanReadableNextReview: String,
    val frequencyPriority: SrsFrequencyPriority,
    val frequencyWeight: Float,
    val explanation: String
)

/**
 * Global SRS Statistics
 */
data class SrsSummaryStats(
    val totalVocabularyCount: Int,
    val dueForReviewCount: Int,
    val learningCount: Int,
    val masteredCount: Int,
    val averageEaseFactor: Float,
    val estimatedRetentionRate: Int,
    val nextScheduledReviewTimestamp: Long?
)

/**
 * Spaced Repetition System (SRS) Scheduling Algorithm Engine
 *
 * Implements an advanced SuperMemo SM-2+ algorithm that continuously adapts
 * review intervals and appearance frequency in the exercise system based on user performance.
 */
object SrsAlgorithm {

    const val DEFAULT_EASE_FACTOR = 2.5f
    const val MIN_EASE_FACTOR = 1.3f
    const val MAX_EASE_FACTOR = 3.2f

    /**
     * Maps exercise metrics (correctness, hint usage, response time) to an SRS quality grade (1-5)
     */
    fun computeQualityFromExercise(
        isCorrect: Boolean,
        usedHint: Boolean,
        responseTimeMs: Long
    ): SrsReviewQuality {
        return when {
            !isCorrect -> {
                if (responseTimeMs > 15000L || usedHint) {
                    SrsReviewQuality.AGAIN
                } else {
                    SrsReviewQuality.HARD
                }
            }
            usedHint -> {
                // Correct, but used hint
                SrsReviewQuality.PASS_WITH_HINT
            }
            responseTimeMs <= 4000L -> {
                // Rapid, fluent correct answer
                SrsReviewQuality.EASY
            }
            responseTimeMs <= 10000L -> {
                // Confident correct answer
                SrsReviewQuality.GOOD
            }
            else -> {
                // Correct but took considerable time thinking
                SrsReviewQuality.PASS_WITH_HINT
            }
        }
    }

    /**
     * Evaluates user performance on an exercise item and computes the new SRS schedule
     */
    fun evaluatePerformance(
        word: String,
        translation: String,
        isCorrect: Boolean,
        usedHint: Boolean,
        responseTimeMs: Long,
        existingItem: SavedVocabItemEntity?
    ): SrsEvaluationResult {
        val qualityEnum = computeQualityFromExercise(isCorrect, usedHint, responseTimeMs)
        val quality = qualityEnum.grade

        val prevRepetition = existingItem?.repetitionNumber ?: 0
        val prevInterval = existingItem?.intervalDays ?: 1
        val prevEase = existingItem?.easeFactor ?: DEFAULT_EASE_FACTOR
        val prevMastery = existingItem?.masteryLevel ?: 0

        // 1. Calculate New Ease Factor:
        // EF' = EF + (0.1 - (5 - q) * (0.08 + (5 - q) * 0.02))
        var newEase = prevEase + (0.1f - (5 - quality) * (0.08f + (5 - quality) * 0.02f))
        if (!isCorrect) {
            // Apply slight penalty on failure to bring back difficult words faster
            newEase -= 0.10f
        }
        newEase = newEase.coerceIn(MIN_EASE_FACTOR, MAX_EASE_FACTOR)

        // 2. Calculate New Repetition Number & Interval in Days
        var newRepetition: Int
        var newInterval: Int

        if (quality < 3) {
            // Failure: reset repetition streak, reschedule for tomorrow or same day
            newRepetition = 0
            newInterval = 1
        } else {
            // Successful recall
            newRepetition = prevRepetition + 1
            newInterval = when (newRepetition) {
                1 -> if (quality == 5) 2 else 1
                2 -> if (quality == 5) 6 else 3
                3 -> if (quality == 5) 12 else 6
                else -> {
                    val factorMultiplier = if (quality == 5) newEase * 1.15f else newEase
                    max((prevInterval * factorMultiplier).roundToInt(), prevInterval + 1)
                }
            }
        }

        // 3. Compute New Mastery Level (0 to 5)
        val newMastery = when {
            newRepetition == 0 -> 0
            newInterval <= 2 -> 1
            newInterval <= 5 -> 2
            newInterval <= 14 -> 3
            newInterval <= 28 -> 4
            else -> 5
        }

        // 4. Compute Next Review Timestamp
        val now = System.currentTimeMillis()
        val nextReviewTimestamp = now + (newInterval.toLong() * 24L * 60L * 60L * 1000L)

        // 5. Frequency Priority & Urgency
        val frequencyPriority = when {
            newInterval <= 1 || newRepetition == 0 -> SrsFrequencyPriority.HIGH_FREQUENCY_LEARNING
            newInterval <= 5 -> SrsFrequencyPriority.HIGH_FREQUENCY_LEARNING
            newInterval <= 14 -> SrsFrequencyPriority.MEDIUM_FREQUENCY_REVIEW
            else -> SrsFrequencyPriority.LOW_FREQUENCY_MASTERED
        }

        val frequencyWeight = calculateWeight(
            nextReviewTimestamp = nextReviewTimestamp,
            masteryLevel = newMastery,
            easeFactor = newEase,
            intervalDays = newInterval,
            now = now
        )

        val humanTime = formatNextReview(newInterval)

        val explanation = when {
            !isCorrect -> "Has fallado esta palabra. El SRS ha aumentado su frecuencia de aparición y la programó para repaso mañana."
            usedHint -> "Respondiste con pista. El intervalo se incrementó a $newInterval días con frecuencia moderada."
            quality == 5 -> "¡Excelente memoria! Intervalo extendido a $newInterval días. Frecuencia reducida para dar prioridad a palabras nuevas."
            else -> "Respuesta correcta. Intervalo ajustado a $newInterval días."
        }

        return SrsEvaluationResult(
            targetWord = word,
            translation = translation,
            quality = quality,
            qualityLabel = qualityEnum.title,
            isCorrect = isCorrect,
            previousRepetition = prevRepetition,
            newRepetition = newRepetition,
            previousIntervalDays = prevInterval,
            newIntervalDays = newInterval,
            intervalDeltaDays = newInterval - prevInterval,
            previousEaseFactor = prevEase,
            newEaseFactor = newEase,
            previousMasteryLevel = prevMastery,
            newMasteryLevel = newMastery,
            nextReviewTimestamp = nextReviewTimestamp,
            humanReadableNextReview = humanTime,
            frequencyPriority = frequencyPriority,
            frequencyWeight = frequencyWeight,
            explanation = explanation
        )
    }

    /**
     * Calculates the dynamic frequency weight for question ordering in the exercise system.
     * Higher weight = higher likelihood/priority to appear in practice sessions.
     */
    fun calculateWeight(
        nextReviewTimestamp: Long,
        masteryLevel: Int,
        easeFactor: Float,
        intervalDays: Int,
        now: Long = System.currentTimeMillis()
    ): Float {
        val isOverdue = nextReviewTimestamp <= now
        val overdueDays = if (isOverdue) ((now - nextReviewTimestamp) / (24L * 60L * 60L * 1000L)).toInt() else 0

        return if (isOverdue) {
            // Overdue items get top priority
            100f + (overdueDays.coerceAtMost(10) * 10f) + (3.0f - easeFactor) * 10f
        } else {
            when (masteryLevel) {
                0 -> 75f // Unlearned / fresh
                1 -> 65f // Fragile
                2 -> 45f // Early learning
                3 -> 30f // Intermediate
                4 -> 15f // Consolidated
                5 -> 5f  // Mastered
                else -> 20f
            }
        }
    }

    /**
     * Enriches and sorts exercise questions based on the SRS scheduling algorithm.
     * Items that are overdue, fragile, or have low ease factors appear first and more frequently.
     */
    fun prioritizeQuestionsBySrs(
        questions: List<ModularExerciseQuestion>,
        savedItemsMap: Map<String, SavedVocabItemEntity>,
        now: Long = System.currentTimeMillis()
    ): List<ModularExerciseQuestion> {
        val enriched = questions.map { question ->
            val vocabKey = extractVocabKey(question).lowercase().trim()
            val srsItem = savedItemsMap[vocabKey] ?: savedItemsMap.values.find {
                it.sourceText.equals(vocabKey, ignoreCase = true) ||
                it.translation.equals(vocabKey, ignoreCase = true) ||
                (question.correctAnswer.isNotBlank() && it.sourceText.equals(question.correctAnswer.trim(), ignoreCase = true))
            }

            if (srsItem != null) {
                val isDue = srsItem.nextReviewTimestamp <= now
                val weight = calculateWeight(
                    nextReviewTimestamp = srsItem.nextReviewTimestamp,
                    masteryLevel = srsItem.masteryLevel,
                    easeFactor = srsItem.easeFactor,
                    intervalDays = srsItem.intervalDays,
                    now = now
                )
                question.copy(
                    vocabTerm = srsItem.sourceText,
                    srsItemId = srsItem.id,
                    srsMasteryLevel = srsItem.masteryLevel,
                    srsIntervalDays = srsItem.intervalDays,
                    srsEaseFactor = srsItem.easeFactor,
                    srsUrgencyWeight = weight,
                    isSrsDue = isDue
                )
            } else {
                // General question without prior SRS history
                question.copy(
                    srsUrgencyWeight = 25f,
                    isSrsDue = false
                )
            }
        }

        // Sort descending by urgency weight
        return enriched.sortedByDescending { it.srsUrgencyWeight }
    }

    /**
     * Extracts a vocabulary key from a question for database matching
     */
    fun extractVocabKey(question: ModularExerciseQuestion): String {
        return when {
            !question.vocabTerm.isNullOrBlank() -> question.vocabTerm
            question.visualIllustration != null -> question.visualIllustration.englishWord
            question.audioText != null && question.audioText.split(" ").size <= 3 -> question.audioText
            question.correctAnswer.split(" ").size <= 3 -> question.correctAnswer
            else -> question.title.replace("Vocabulario Guardado:", "").replace("Identificación de Vocabulario:", "").trim()
        }
    }

    /**
     * Formats next review days into clear Spanish string
     */
    fun formatNextReview(intervalDays: Int): String {
        return when (intervalDays) {
            0 -> "Hoy mismo"
            1 -> "En 1 día (Mañana)"
            in 2..6 -> "En $intervalDays días"
            7 -> "En 1 semana"
            in 8..13 -> "En $intervalDays días (~1 semana)"
            14 -> "En 2 semanas"
            in 15..29 -> "En $intervalDays días (~3 semanas)"
            30 -> "En 1 mes"
            else -> "En $intervalDays días"
        }
    }

    /**
     * Generates a global summary of SRS retention and progress
     */
    fun calculateSummaryStats(
        items: List<SavedVocabItemEntity>,
        now: Long = System.currentTimeMillis()
    ): SrsSummaryStats {
        if (items.isEmpty()) {
            return SrsSummaryStats(
                totalVocabularyCount = 0,
                dueForReviewCount = 0,
                learningCount = 0,
                masteredCount = 0,
                averageEaseFactor = DEFAULT_EASE_FACTOR,
                estimatedRetentionRate = 100,
                nextScheduledReviewTimestamp = null
            )
        }

        val dueCount = items.count { it.nextReviewTimestamp <= now }
        val learningCount = items.count { it.masteryLevel in 1..3 }
        val masteredCount = items.count { it.masteryLevel >= 4 }
        val avgEase = items.map { it.easeFactor }.average().toFloat()

        // Estimate retention based on average ease factor and mastery levels
        val retention = ((items.sumOf { item ->
            when (item.masteryLevel) {
                0 -> 50
                1 -> 70
                2 -> 80
                3 -> 90
                4 -> 95
                5 -> 99
                else -> 75
            }
        }.toDouble() / items.size)).toInt().coerceIn(50, 99)

        val nextReview = items.filter { it.nextReviewTimestamp > now }
            .minByOrNull { it.nextReviewTimestamp }
            ?.nextReviewTimestamp

        return SrsSummaryStats(
            totalVocabularyCount = items.size,
            dueForReviewCount = dueCount,
            learningCount = learningCount,
            masteredCount = masteredCount,
            averageEaseFactor = if (avgEase.isNaN()) DEFAULT_EASE_FACTOR else avgEase,
            estimatedRetentionRate = retention,
            nextScheduledReviewTimestamp = nextReview
        )
    }
}
