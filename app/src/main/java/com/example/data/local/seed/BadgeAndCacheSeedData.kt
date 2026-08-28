package com.example.data.local.seed

import com.example.data.local.ExerciseLocalData
import com.example.data.local.VocabularyBankData
import com.example.data.local.entities.CachedExerciseEntity
import com.example.data.local.entities.CachedVocabEntity
import com.example.data.local.entities.UserBadgeEntity
import org.json.JSONArray

object BadgeAndCacheSeedData {

    fun getInitialBadges(): List<UserBadgeEntity> = listOf(
        UserBadgeEntity(
            badgeId = "badge_first_pomodoro",
            title = "First Focus Session",
            titleSpanish = "Enfoque Inicial",
            description = "Completa tu primera sesión de estudio Pomodoro sin interrupciones.",
            category = "POMODORO",
            iconEmoji = "⏱️",
            tier = "BRONZE",
            currentProgress = 1,
            targetGoal = 1,
            isUnlocked = true,
            unlockedAtTimestamp = System.currentTimeMillis() - 86400000L,
            xpReward = 50
        ),
        UserBadgeEntity(
            badgeId = "badge_streak_3",
            title = "Habit Builder",
            titleSpanish = "Hábito en Marcha",
            description = "Mantén una racha activa de 3 días consecutivos de estudio.",
            category = "STREAK",
            iconEmoji = "🔥",
            tier = "BRONZE",
            currentProgress = 3,
            targetGoal = 3,
            isUnlocked = true,
            unlockedAtTimestamp = System.currentTimeMillis() - 43200000L,
            xpReward = 75
        ),
        UserBadgeEntity(
            badgeId = "badge_streak_7",
            title = "Weekly Devotion",
            titleSpanish = "Dedicación Semanal",
            description = "Alcanza una racha perfecta de 7 días seguidos repasando inglés B2.",
            category = "STREAK",
            iconEmoji = "⚡",
            tier = "SILVER",
            currentProgress = 3,
            targetGoal = 7,
            isUnlocked = false,
            xpReward = 150
        ),
        UserBadgeEntity(
            badgeId = "badge_streak_14",
            title = "Unstoppable Warrior",
            titleSpanish = "Guerrero Imparable",
            description = "Consigue 14 días seguidos de constancia diaria en tu preparación.",
            category = "STREAK",
            iconEmoji = "🛡️",
            tier = "GOLD",
            currentProgress = 3,
            targetGoal = 14,
            isUnlocked = false,
            xpReward = 300
        ),
        UserBadgeEntity(
            badgeId = "badge_vocab_10",
            title = "Word Collector",
            titleSpanish = "Recolector de Palabras",
            description = "Guarda al menos 10 términos clave en tu cuaderno de repetición SRS.",
            category = "VOCABULARY",
            iconEmoji = "📚",
            tier = "BRONZE",
            currentProgress = 2,
            targetGoal = 10,
            isUnlocked = false,
            xpReward = 50
        ),
        UserBadgeEntity(
            badgeId = "badge_vocab_30",
            title = "Lexicon Expander",
            titleSpanish = "Léxico Ampliado",
            description = "Acumula 30 palabras o phrasal verbs en tu banco personal offline.",
            category = "VOCABULARY",
            iconEmoji = "📖",
            tier = "SILVER",
            currentProgress = 2,
            targetGoal = 30,
            isUnlocked = false,
            xpReward = 150
        ),
        UserBadgeEntity(
            badgeId = "badge_vocab_master_10",
            title = "Photographic Memory",
            titleSpanish = "Memoria Fotográfica",
            description = "Domina con nivel máximo 5 al menos 10 tarjetas de vocabulario.",
            category = "VOCABULARY",
            iconEmoji = "🧠",
            tier = "GOLD",
            currentProgress = 0,
            targetGoal = 10,
            isUnlocked = false,
            xpReward = 250
        ),
        UserBadgeEntity(
            badgeId = "badge_exercise_10",
            title = "Challenge Seeker",
            titleSpanish = "Primeros Retos",
            description = "Supera 10 ejercicios interactivos de gramática, audio o vocabulario.",
            category = "EXERCISES",
            iconEmoji = "🎯",
            tier = "BRONZE",
            currentProgress = 5,
            targetGoal = 10,
            isUnlocked = false,
            xpReward = 60
        ),
        UserBadgeEntity(
            badgeId = "badge_exercise_50",
            title = "Practice Specialist",
            titleSpanish = "Especialista en Práctica",
            description = "Resuelve 50 preguntas y tests modulares Cambridge B2.",
            category = "EXERCISES",
            iconEmoji = "🏹",
            tier = "SILVER",
            currentProgress = 5,
            targetGoal = 50,
            isUnlocked = false,
            xpReward = 200
        ),
        UserBadgeEntity(
            badgeId = "badge_perfect_quiz",
            title = "Cambridge Precision",
            titleSpanish = "Puntería Cambridge",
            description = "Completa un bloque de ejercicios con 100% de precisión y aciertos.",
            category = "EXERCISES",
            iconEmoji = "🎯",
            tier = "GOLD",
            currentProgress = 0,
            targetGoal = 1,
            isUnlocked = false,
            xpReward = 150
        ),
        UserBadgeEntity(
            badgeId = "badge_study_100m",
            title = "Time Centurion",
            titleSpanish = "Centurión del Tiempo",
            description = "Acumula más de 100 minutos de estudio enfocado en la aplicación.",
            category = "POMODORO",
            iconEmoji = "⌛",
            tier = "SILVER",
            currentProgress = 45,
            targetGoal = 100,
            isUnlocked = false,
            xpReward = 120
        ),
        UserBadgeEntity(
            badgeId = "badge_all_skills",
            title = "Complete Polyglot",
            titleSpanish = "Políglota Integral",
            description = "Practica las 8 destrezas del currículo (Grammar, Vocab, Listening, Speaking, etc.).",
            category = "CAMBRIDGE",
            iconEmoji = "🌟",
            tier = "PLATINUM",
            currentProgress = 4,
            targetGoal = 8,
            isUnlocked = false,
            xpReward = 350
        ),
        UserBadgeEntity(
            badgeId = "badge_cambridge_master",
            title = "B2 First Grandmaster",
            titleSpanish = "Gran Maestro B2 First",
            description = "Alcanza el Nivel 5 de usuario y acumula más de 1000 XP en tu progreso.",
            category = "LEVEL",
            iconEmoji = "👑",
            tier = "DIAMOND",
            currentProgress = 340,
            targetGoal = 1000,
            isUnlocked = false,
            xpReward = 500
        )
    )

    fun getInitialCachedExercises(): List<CachedExerciseEntity> {
        return ExerciseLocalData.localExerciseCatalog.map { q ->
            val optionsJson = JSONArray(q.options).toString()
            val alternativesJson = JSONArray(q.acceptedAlternatives).toString()
            CachedExerciseEntity(
                id = q.id,
                type = q.type.name,
                level = q.level,
                category = q.category,
                title = q.title,
                prompt = q.prompt,
                contextText = q.contextText,
                optionsJson = optionsJson,
                correctAnswer = q.correctAnswer,
                acceptedAlternativesJson = alternativesJson,
                hintSpanish = q.hintSpanish ?: "",
                explanation = q.explanation,
                audioText = q.audioText ?: "",
                keyWord = q.keyWord,
                visualIllustrationId = q.visualIllustration?.id,
                visualEmoji = q.visualIllustration?.visualEmoji,
                visualAccentColorHex = q.visualIllustration?.accentColorHex ?: 0L,
                visualDescription = q.visualIllustration?.visualDescription,
                linkedTopicId = q.linkedTopicId,
                isCachedOffline = true,
                lastUpdated = System.currentTimeMillis()
            )
        }
    }

    fun getInitialCachedVocab(): List<CachedVocabEntity> {
        // Collect from staticVocabularyList in VocabularyBankData
        val allStatic = VocabularyBankData.getAllVocabulary(emptyList())
        return allStatic.map { v ->
            CachedVocabEntity(
                id = v.id,
                word = v.word,
                translation = v.translation,
                phonetic = v.phonetic,
                level = v.level,
                topic = v.topic,
                definition = v.definition,
                exampleEn = v.exampleEn,
                exampleEs = v.exampleEs,
                isOfflineReady = true,
                lastUpdated = System.currentTimeMillis()
            )
        }
    }
}
