package com.example.data.repository

import com.example.data.local.dao.SavedVocabDao
import com.example.data.local.dao.TranslationDao
import com.example.data.local.entities.SavedVocabItemEntity
import com.example.data.local.entities.TranslationHistoryEntity
import com.example.data.remote.GeminiClient
import com.example.data.remote.MyMemoryTranslateClient
import kotlinx.coroutines.flow.Flow
import org.json.JSONObject
import java.util.UUID

data class TranslationResult(
    val translatedText: String,
    val phonetic: String,
    val notes: String = "",
    val isOffline: Boolean = false
)

class TranslateRepository(
    private val translationDao: TranslationDao,
    private val savedVocabDao: SavedVocabDao
) {
    val recentTranslations: Flow<List<TranslationHistoryEntity>> = translationDao.getRecentTranslations()
    val favoriteTranslations: Flow<List<TranslationHistoryEntity>> = translationDao.getFavoriteTranslations()

    suspend fun translateText(
        text: String,
        sourceLang: String, // "es" or "en"
        targetLang: String  // "en" or "es"
    ): TranslationResult {
        val cleanText = text.trim()
        if (cleanText.isEmpty()) {
            return TranslationResult("", "")
        }

        val prompt = """
Translate the following text from ${if (sourceLang == "es") "Spanish (Mexican)" else "English"} to ${if (targetLang == "en") "English (Natural CEFR B2 level)" else "Spanish (Mexican)"}.
Provide the output strictly in JSON with format:
{
  "translation": "translated text here",
  "phonetic": "IPA phonetic transcription for the English part",
  "notes": "brief usage note or register (e.g. formal, informal, technical)",
  "synonyms": ["syn1", "syn2"]
}

Source text: "$cleanText"
        """.trimIndent()

        val systemInstruction = "You are an expert bilingual English (B2) and Spanish teacher. Respond ONLY with valid JSON."

        val aiResult = GeminiClient.generateText(prompt, systemInstruction)
        val translationResult: TranslationResult = if (aiResult.isSuccess) {
            val rawJson = aiResult.getOrNull()?.trim() ?: ""
            parseTranslationJson(rawJson, cleanText, targetLang)
        } else {
            // Online free translation fallback via MyMemory
            val onlineTr = MyMemoryTranslateClient.translate(cleanText, sourceLang, targetLang)
            if (onlineTr.isSuccess) {
                val trText = onlineTr.getOrNull()?.translatedText ?: ""
                TranslationResult(
                    translatedText = trText,
                    phonetic = "",
                    notes = "Traducción en línea en tiempo real",
                    isOffline = false
                )
            } else {
                // Offline fallback
                offlineFallbackTranslate(cleanText, sourceLang, targetLang)
            }
        }

        // Save to translation history
        translationDao.insertTranslation(
            TranslationHistoryEntity(
                sourceText = cleanText,
                translatedText = translationResult.translatedText,
                phonetic = translationResult.phonetic,
                sourceLang = sourceLang,
                targetLang = targetLang,
                createdAt = System.currentTimeMillis(),
                isFavorite = false
            )
        )

        return translationResult
    }

    private fun parseTranslationJson(jsonStr: String, originalText: String, targetLang: String): TranslationResult {
        return try {
            val sanitized = jsonStr.replace("```json", "").replace("```", "").trim()
            val obj = JSONObject(sanitized)
            TranslationResult(
                translatedText = obj.optString("translation", originalText),
                phonetic = obj.optString("phonetic", ""),
                notes = obj.optString("notes", ""),
                isOffline = false
            )
        } catch (e: Exception) {
            // If raw text returned
            TranslationResult(
                translatedText = jsonStr.take(200),
                phonetic = "",
                notes = "",
                isOffline = false
            )
        }
    }

    private fun offlineFallbackTranslate(text: String, sourceLang: String, targetLang: String): TranslationResult {
        val lower = text.lowercase().trim()
        val dict = mapOf(
            "hola" to Pair("Hello", "/həˈloʊ/"),
            "buenos días" to Pair("Good morning", "/ɡʊd ˈmɔːr.nɪŋ/"),
            "buenas tardes" to Pair("Good afternoon", "/ɡʊd ˌæf.tɚˈnuːn/"),
            "buenas noches" to Pair("Good evening / Good night", "/ɡʊd ˈiːv.nɪŋ/"),
            "gracias" to Pair("Thank you very much", "/ˈθæŋk juː ˈveri mʌtʃ/"),
            "por favor" to Pair("Please", "/pliːz/"),
            "computadora" to Pair("Computer", "/kəmˈpjuː.tɚ/"),
            "red" to Pair("Network", "/ˈnet.wɝːk/"),
            "ancho de banda" to Pair("Bandwidth", "/ˈbænd.wɪdθ/"),
            "servidor" to Pair("Server", "/ˈsɝː.vɚ/"),
            "maestro" to Pair("Teacher / Educator", "/ˈtiː.tʃɚ/"),
            "alumno" to Pair("Student / Pupil", "/ˈstuː.dənt/"),
            "escuela" to Pair("School", "/skuːl/"),
            "examen" to Pair("Exam / Test", "/ɪɡˈzæm/"),
            "tarea" to Pair("Homework / Assignment", "/ˈhoʊm.wɝːk/"),
            "cable" to Pair("Cable", "/ˈkeɪ.bəl/"),
            "fallo" to Pair("Failure / Glitch", "/ˈfeɪ.ljɚ/"),
            "respaldo" to Pair("Backup", "/ˈbæk.ʌp/"),
            "actualizar" to Pair("Update / Upgrade", "/ʌpˈdeɪt/"),
            "conectar" to Pair("Connect", "/kəˈnekt/")
        )

        val dictEnToEs = mapOf(
            "hello" to Pair("Hola", "/həˈloʊ/"),
            "network" to Pair("Red / Red de datos", "/ˈnet.wɝːk/"),
            "server" to Pair("Servidor", "/ˈsɝː.vɚ/"),
            "bandwidth" to Pair("Ancho de banda", "/ˈbænd.wɪdθ/"),
            "troubleshoot" to Pair("Diagnosticar y resolver fallas", "/ˈtrʌb.əl.ʃuːt/"),
            "teacher" to Pair("Profesor / Maestro", "/ˈtiː.tʃɚ/"),
            "student" to Pair("Estudiante / Alumno", "/ˈstuː.dənt/"),
            "backup" to Pair("Copia de seguridad / Respaldo", "/ˈbæk.ʌp/"),
            "curriculum" to Pair("Plan de estudios", "/kəˈrɪk.jə.ləm/"),
            "assessment" to Pair("Evaluación pedagógica", "/əˈses.mənt/")
        )

        if (sourceLang == "es") {
            val match = dict[lower]
            if (match != null) {
                return TranslationResult(match.first, match.second, "Traducción rápida sin conexión", isOffline = true)
            }
        } else {
            val match = dictEnToEs[lower]
            if (match != null) {
                return TranslationResult(match.first, match.second, "Traducción rápida sin conexión", isOffline = true)
            }
        }

        return TranslationResult(
            translatedText = if (sourceLang == "es") "[$text in English]" else "[$text en español]",
            phonetic = "",
            notes = "Modo sin conexión. Conéctate a internet para traducción con IA.",
            isOffline = true
        )
    }

    suspend fun toggleFavorite(id: Long, currentStatus: Boolean) {
        translationDao.toggleFavorite(id, !currentStatus)
    }

    suspend fun deleteTranslation(id: Long) {
        translationDao.deleteTranslation(id)
    }

    suspend fun clearHistory() {
        translationDao.clearHistory()
    }

    suspend fun saveToSrs(
        sourceText: String,
        translation: String,
        phonetic: String = "",
        notes: String = ""
    ) {
        val id = "srs_tr_" + UUID.randomUUID().toString()
        val item = SavedVocabItemEntity(
            id = id,
            sourceText = sourceText,
            translation = translation,
            phonetic = phonetic,
            sourceModule = "translator",
            definition = notes,
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
}
