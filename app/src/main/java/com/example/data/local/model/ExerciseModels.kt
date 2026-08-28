package com.example.data.local.model

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Exercise Types supported by the modular exercise engine
 */
enum class ModularExerciseType(val label: String, val badgeColor: Long) {
    MULTIPLE_CHOICE("Opción Múltiple", 0xFF1E88E5),
    FILL_IN_THE_BLANK("Completar Espacio", 0xFF43A047),
    IMAGE_VOCAB_MATCHING("Emparejamiento Visual", 0xFFFF8F00),
    KEYWORD_TRANSFORMATION("Transformación Cambridge", 0xFF8E24AA)
}

/**
 * Difficulty level for filtering and progression
 */
enum class ExerciseLevel(val code: String, val title: String) {
    ALL("TODOS", "Todos los Niveles"),
    STARTERS("Starters", "Pre A1 Starters"),
    MOVERS("Movers", "A1 Movers"),
    FLYERS("Flyers", "A2 Flyers"),
    A1("A1", "A1 Principiante"),
    A2("A2", "A2 Elemental"),
    B1("B1", "B1 Intermedio"),
    B2("B2", "B2 First / Avanzado")
}

/**
 * Thematic visual illustration category for image-based vocabulary
 */
enum class VisualVocabCategory(val displayName: String, val primaryEmoji: String) {
    ANIMALS("Animales", "🦁"),
    FOOD_DRINKS("Comida y Bebidas", "🍎"),
    TRANSPORT("Transporte", "🚀"),
    SCHOOL_STUDY("Escuela y Estudio", "📚"),
    PROFESSIONS("Profesiones", "🩺"),
    HOUSE_OBJECTS("Objetos del Hogar", "🛋️"),
    CLOTHES("Ropa y Accesorios", "👟"),
    SPORTS_HOBBIES("Deportes y Hobbies", "⚽"),
    NATURE_WEATHER("Naturaleza y Clima", "🌳"),
    EMOTIONS_FEELINGS("Emociones", "💡"),
    TECHNOLOGY("Tecnología", "💻"),
    PLACES_CITY("Lugares y Ciudad", "🏛️")
}

/**
 * Visual illustration data model for rich image-based questions
 */
data class VisualIllustration(
    val id: String,
    val category: VisualVocabCategory,
    val englishWord: String,
    val spanishTranslation: String,
    val phonetic: String,
    val visualEmoji: String,
    val drawableResName: String? = null,
    val accentColorHex: Long = 0xFF1E88E5,
    val visualDescription: String = "",
    val exampleSentence: String = ""
)

/**
 * Matching pair model for multi-item image matching exercises
 */
data class VisualMatchingPair(
    val id: String,
    val englishTerm: String,
    val spanishTerm: String,
    val illustration: VisualIllustration
)

/**
 * Main Modular Exercise Question Model
 */
data class ModularExerciseQuestion(
    val id: String,
    val type: ModularExerciseType,
    val level: String = "A1",
    val category: String = "Vocabulario",
    val title: String,
    val prompt: String,
    val promptSpanish: String? = null,
    val contextText: String? = null,
    val contextTextSpanish: String? = null,
    val baseEnglishSentence: String? = null,
    val spanishSentence: String? = null,
    val keyWord: String? = null, // for sentence transformations
    val options: List<String> = emptyList(),
    val optionsSpanish: List<String> = emptyList(),
    val correctAnswer: String,
    val acceptedAlternatives: List<String> = emptyList(),
    val hintSpanish: String? = null,
    val explanation: String,
    val visualIllustration: VisualIllustration? = null,
    val matchingPairs: List<VisualMatchingPair> = emptyList(),
    val audioText: String? = null,
    val linkedTopicId: String? = null,
    // Spaced Repetition System (SRS) metadata
    val vocabTerm: String? = null,
    val srsItemId: String? = null,
    val srsMasteryLevel: Int? = null,
    val srsIntervalDays: Int? = null,
    val srsEaseFactor: Float? = null,
    val srsUrgencyWeight: Float = 0f,
    val isSrsDue: Boolean = false
)

/**
 * Summary result of an exercise practice session
 */
data class ModularExerciseSessionResult(
    val totalQuestions: Int,
    val correctCount: Int,
    val scorePercentage: Int,
    val xpEarned: Int,
    val timeSpentSeconds: Long,
    val breakdownByType: Map<ModularExerciseType, Pair<Int, Int>> = emptyMap()
)
