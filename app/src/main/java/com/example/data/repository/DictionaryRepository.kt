package com.example.data.repository

import com.example.data.local.dao.DictionaryDao
import com.example.data.local.dao.SavedVocabDao
import com.example.data.local.entities.DictionaryLookupEntity
import com.example.data.local.entities.SavedVocabItemEntity
import com.example.data.remote.DatamuseClient
import com.example.data.remote.FreeDictionaryClient
import com.example.data.remote.GeminiClient
import com.example.data.remote.MyMemoryTranslateClient
import kotlinx.coroutines.flow.Flow
import org.json.JSONArray
import org.json.JSONObject
import java.util.UUID

data class DictionaryResult(
    val term: String,
    val phonetic: String,
    val partOfSpeech: String,
    val definitionEs: String,
    val definitionEn: String,
    val examples: List<Pair<String, String>>,
    val synonyms: List<String>,
    val phrasalVerbs: List<String>,
    val usageNotes: String,
    val isOffline: Boolean = false,
    val audioUrl: String? = null
)

class DictionaryRepository(
    private val dictionaryDao: DictionaryDao,
    private val savedVocabDao: SavedVocabDao
) {
    val recentLookups: Flow<List<DictionaryLookupEntity>> = dictionaryDao.getRecentLookups()

    suspend fun lookupTerm(term: String): DictionaryResult {
        val cleanTerm = term.trim()
        if (cleanTerm.isEmpty()) {
            return emptyResult("")
        }

        // 1. Check local cache first
        val cached = dictionaryDao.getLookupByTerm(cleanTerm)
        if (cached != null) {
            return parseCachedEntity(cached)
        }

        // 2. Query Gemini API first if configured
        val prompt = """
Provide a detailed English-Spanish dictionary entry for the word or phrase: "$cleanTerm".
Target level: CEFR B2.
Output strictly in valid JSON format:
{
  "term": "$cleanTerm",
  "phonetic": "/IPA phonetic transcription/",
  "partOfSpeech": "verb / noun / adjective / phrasal verb / idiom",
  "definitionEs": "Definición clara en español",
  "definitionEn": "Clear natural English definition",
  "examples": [
    {"en": "First authentic B2 context sentence.", "es": "Primera oración de ejemplo en contexto."},
    {"en": "Second authentic B2 context sentence.", "es": "Segunda oración de ejemplo en contexto."}
  ],
  "synonyms": ["synonym1", "synonym2", "synonym3"],
  "phrasalVerbs": ["related phrasal verb 1", "related phrasal verb 2"],
  "usageNotes": "Explicación metalingüística en español: formal vs informal register, colocaciones habituales, y errores comunes para hispanohablantes."
}
        """.trimIndent()

        val systemInstruction = "You are an expert Cambridge B2 English lexicographer and bilingual linguist. Return ONLY valid JSON."

        val aiResult = GeminiClient.generateText(prompt, systemInstruction)
        var result: DictionaryResult = if (aiResult.isSuccess) {
            val jsonStr = aiResult.getOrNull()?.trim() ?: ""
            parseAiJson(jsonStr, cleanTerm)
        } else {
            // 3. Fallback to Live Free Dictionary API over Internet
            lookupLiveFreeDictionary(cleanTerm)
        }

        // Cache the lookup locally
        val examplesJson = result.examples.joinToString(prefix = "[", postfix = "]") { (en, es) ->
            "{\"en\":\"${en.replace("\"", "\\\"")}\",\"es\":\"${es.replace("\"", "\\\"")}\"}"
        }
        val synonymsJson = result.synonyms.joinToString(prefix = "[", postfix = "]") { "\"${it.replace("\"", "\\\"")}\"" }
        val phrasalVerbsJson = result.phrasalVerbs.joinToString(prefix = "[", postfix = "]") { "\"${it.replace("\"", "\\\"")}\"" }

        dictionaryDao.insertLookup(
            DictionaryLookupEntity(
                id = "dict_" + cleanTerm.lowercase().replace(" ", "_"),
                term = cleanTerm,
                phonetic = result.phonetic,
                partOfSpeech = result.partOfSpeech,
                definitionEs = result.definitionEs,
                definitionEn = result.definitionEn,
                examplesJson = examplesJson,
                synonymsJson = synonymsJson,
                phrasalVerbsJson = phrasalVerbsJson,
                usageNotes = result.usageNotes,
                lookedUpAt = System.currentTimeMillis()
            )
        )

        return result
    }

    private suspend fun lookupLiveFreeDictionary(term: String): DictionaryResult {
        val onlineResult = FreeDictionaryClient.lookupWord(term)
        if (onlineResult.isSuccess) {
            val entry = onlineResult.getOrNull()
            if (entry != null && entry.meanings.isNotEmpty()) {
                val firstMeaning = entry.meanings.first()
                val firstDef = firstMeaning.definitions.firstOrNull()
                val defEn = firstDef?.definition ?: ""
                val exampleEn = firstDef?.example

                // Translate English definition to Spanish using online translation client
                val defEsResult = MyMemoryTranslateClient.translate(defEn, "en", "es")
                val defEs = if (defEsResult.isSuccess) defEsResult.getOrNull()?.translatedText ?: defEn else defEn

                val examplesList = mutableListOf<Pair<String, String>>()
                if (!exampleEn.isNullOrBlank()) {
                    val exEsResult = MyMemoryTranslateClient.translate(exampleEn, "en", "es")
                    val exEs = if (exEsResult.isSuccess) exEsResult.getOrNull()?.translatedText ?: "" else ""
                    examplesList.add(Pair(exampleEn, exEs))
                }

                // Add synonyms from FreeDictionary or Datamuse
                val allSyns = (firstMeaning.synonyms + (firstDef?.synonyms ?: emptyList())).distinct()
                val synonyms = if (allSyns.isNotEmpty()) {
                    allSyns.take(6)
                } else {
                    val datamuseSyns = DatamuseClient.getSynonyms(term, 6).getOrDefault(emptyList())
                    datamuseSyns
                }

                return DictionaryResult(
                    term = entry.word,
                    phonetic = entry.phonetic,
                    partOfSpeech = firstMeaning.partOfSpeech,
                    definitionEs = defEs,
                    definitionEn = defEn,
                    examples = examplesList,
                    synonyms = synonyms,
                    phrasalVerbs = emptyList(),
                    usageNotes = "Definición obtenida en vivo vía Free Dictionary API y traducción automática en línea.",
                    isOffline = false,
                    audioUrl = entry.audioUrl
                )
            }
        }

        // If online free dict fails, attempt translation as word
        val directTr = MyMemoryTranslateClient.translate(term, "en", "es")
        if (directTr.isSuccess) {
            val translated = directTr.getOrNull()?.translatedText ?: ""
            val syns = DatamuseClient.getSynonyms(term, 4).getOrDefault(emptyList())
            return DictionaryResult(
                term = term,
                phonetic = "/$term/",
                partOfSpeech = "general",
                definitionEs = translated,
                definitionEn = "Direct translation for '$term'.",
                examples = listOf(Pair("We studied '$term' in our English lesson.", "Estudiamos '$term' en nuestra lección de inglés.")),
                synonyms = syns,
                phrasalVerbs = emptyList(),
                usageNotes = "Traducción directa en línea.",
                isOffline = false
            )
        }

        // Final local offline fallback
        return offlineFallbackLookup(term)
    }

    private fun parseAiJson(jsonStr: String, term: String): DictionaryResult {
        return try {
            val sanitized = jsonStr.replace("```json", "").replace("```", "").trim()
            val obj = JSONObject(sanitized)

            val examplesList = mutableListOf<Pair<String, String>>()
            val exArr = obj.optJSONArray("examples")
            if (exArr != null) {
                for (i in 0 until exArr.length()) {
                    val item = exArr.getJSONObject(i)
                    examplesList.add(Pair(item.optString("en"), item.optString("es")))
                }
            }

            val synList = mutableListOf<String>()
            val synArr = obj.optJSONArray("synonyms")
            if (synArr != null) {
                for (i in 0 until synArr.length()) {
                    synList.add(synArr.getString(i))
                }
            }

            val pvList = mutableListOf<String>()
            val pvArr = obj.optJSONArray("phrasalVerbs")
            if (pvArr != null) {
                for (i in 0 until pvArr.length()) {
                    pvList.add(pvArr.getString(i))
                }
            }

            DictionaryResult(
                term = obj.optString("term", term),
                phonetic = obj.optString("phonetic", ""),
                partOfSpeech = obj.optString("partOfSpeech", "general"),
                definitionEs = obj.optString("definitionEs", ""),
                definitionEn = obj.optString("definitionEn", ""),
                examples = examplesList,
                synonyms = synList,
                phrasalVerbs = pvList,
                usageNotes = obj.optString("usageNotes", ""),
                isOffline = false
            )
        } catch (e: Exception) {
            emptyResult(term)
        }
    }

    private fun parseCachedEntity(entity: DictionaryLookupEntity): DictionaryResult {
        val examplesList = mutableListOf<Pair<String, String>>()
        try {
            val arr = JSONArray(entity.examplesJson)
            for (i in 0 until arr.length()) {
                val obj = arr.getJSONObject(i)
                examplesList.add(Pair(obj.getString("en"), obj.getString("es")))
            }
        } catch (_: Exception) {}

        val synList = mutableListOf<String>()
        try {
            val arr = JSONArray(entity.synonymsJson)
            for (i in 0 until arr.length()) {
                synList.add(arr.getString(i))
            }
        } catch (_: Exception) {}

        val pvList = mutableListOf<String>()
        try {
            val arr = JSONArray(entity.phrasalVerbsJson)
            for (i in 0 until arr.length()) {
                pvList.add(arr.getString(i))
            }
        } catch (_: Exception) {}

        return DictionaryResult(
            term = entity.term,
            phonetic = entity.phonetic,
            partOfSpeech = entity.partOfSpeech,
            definitionEs = entity.definitionEs,
            definitionEn = entity.definitionEn,
            examples = examplesList,
            synonyms = synList,
            phrasalVerbs = pvList,
            usageNotes = entity.usageNotes,
            isOffline = false
        )
    }

    private fun offlineFallbackLookup(term: String): DictionaryResult {
        return DictionaryResult(
            term = term,
            phonetic = "/.../",
            partOfSpeech = "Entrada en caché",
            definitionEs = "Término guardado o consultado. Conéctate a internet para ver la definición ampliada con IA.",
            definitionEn = "Dictionary offline mode.",
            examples = listOf(
                Pair("We use this word frequently in B2 English.", "Usamos esta palabra frecuentemente en inglés B2.")
            ),
            synonyms = listOf("relevant term"),
            phrasalVerbs = emptyList(),
            usageNotes = "Para ver sinónimos contextuales y notas gramaticales completas, activa tu conexión de datos.",
            isOffline = true
        )
    }

    private fun emptyResult(term: String): DictionaryResult {
        return DictionaryResult(
            term = term,
            phonetic = "",
            partOfSpeech = "",
            definitionEs = "No se encontraron resultados.",
            definitionEn = "No results found.",
            examples = emptyList(),
            synonyms = emptyList(),
            phrasalVerbs = emptyList(),
            usageNotes = ""
        )
    }

    suspend fun saveToSrs(result: DictionaryResult) {
        val id = "srs_dict_" + UUID.randomUUID().toString()
        val examplesJson = result.examples.joinToString(prefix = "[", postfix = "]") { (en, es) ->
            "{\"en\":\"${en.replace("\"", "\\\"")}\",\"es\":\"${es.replace("\"", "\\\"")}\"}"
        }
        val synonymsJson = result.synonyms.joinToString(prefix = "[", postfix = "]") { "\"${it.replace("\"", "\\\"")}\"" }

        val item = SavedVocabItemEntity(
            id = id,
            sourceText = result.term,
            translation = result.definitionEs,
            phonetic = result.phonetic,
            sourceModule = "dictionary",
            definition = result.definitionEn,
            examplesJson = examplesJson,
            synonymsJson = synonymsJson,
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
