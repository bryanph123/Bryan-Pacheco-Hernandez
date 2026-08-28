package com.example.data.remote

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import java.util.concurrent.TimeUnit

data class FreeDictionaryDefinition(
    val definition: String,
    val example: String?,
    val synonyms: List<String>,
    val antonyms: List<String>
)

data class FreeDictionaryMeaning(
    val partOfSpeech: String,
    val definitions: List<FreeDictionaryDefinition>,
    val synonyms: List<String>
)

data class FreeDictionaryEntry(
    val word: String,
    val phonetic: String,
    val audioUrl: String?,
    val meanings: List<FreeDictionaryMeaning>
)

object FreeDictionaryClient {
    private val client = OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(10, TimeUnit.SECONDS)
        .build()

    suspend fun lookupWord(word: String): Result<FreeDictionaryEntry> = withContext(Dispatchers.IO) {
        try {
            val cleanWord = word.trim().lowercase().replace(" ", "%20")
            if (cleanWord.isBlank()) {
                return@withContext Result.failure(Exception("Palabra vacía"))
            }

            val request = Request.Builder()
                .url("https://api.dictionaryapi.dev/api/v2/entries/en/$cleanWord")
                .header("User-Agent", "InglesB2App/1.0")
                .build()

            val response = client.newCall(request).execute()
            if (!response.isSuccessful) {
                return@withContext Result.failure(Exception("HTTP ${response.code}"))
            }

            val body = response.body?.string() ?: ""
            if (body.isBlank()) {
                return@withContext Result.failure(Exception("Cuerpo de respuesta vacío"))
            }

            val jsonArray = JSONArray(body)
            if (jsonArray.length() == 0) {
                return@withContext Result.failure(Exception("Sin resultados"))
            }

            val firstEntry = jsonArray.getJSONObject(0)
            val entryWord = firstEntry.optString("word", word)
            var phonetic = firstEntry.optString("phonetic", "")
            var audioUrl: String? = null

            val phoneticsArr = firstEntry.optJSONArray("phonetics")
            if (phoneticsArr != null) {
                for (i in 0 until phoneticsArr.length()) {
                    val pObj = phoneticsArr.getJSONObject(i)
                    if (phonetic.isBlank() && pObj.has("text")) {
                        phonetic = pObj.optString("text")
                    }
                    val audio = pObj.optString("audio")
                    if (audio.isNotBlank() && audioUrl == null) {
                        audioUrl = audio
                    }
                }
            }

            val meaningsList = mutableListOf<FreeDictionaryMeaning>()
            val meaningsArr = firstEntry.optJSONArray("meanings")
            if (meaningsArr != null) {
                for (i in 0 until meaningsArr.length()) {
                    val mObj = meaningsArr.getJSONObject(i)
                    val partOfSpeech = mObj.optString("partOfSpeech", "general")

                    val meaningSynonyms = mutableListOf<String>()
                    val mSynArr = mObj.optJSONArray("synonyms")
                    if (mSynArr != null) {
                        for (k in 0 until mSynArr.length()) {
                            meaningSynonyms.add(mSynArr.getString(k))
                        }
                    }

                    val defsList = mutableListOf<FreeDictionaryDefinition>()
                    val defsArr = mObj.optJSONArray("definitions")
                    if (defsArr != null) {
                        for (j in 0 until defsArr.length()) {
                            val dObj = defsArr.getJSONObject(j)
                            val defText = dObj.optString("definition", "")
                            val exampleText = if (dObj.has("example")) dObj.optString("example") else null

                            val defSynList = mutableListOf<String>()
                            val dSynArr = dObj.optJSONArray("synonyms")
                            if (dSynArr != null) {
                                for (k in 0 until dSynArr.length()) {
                                    defSynList.add(dSynArr.getString(k))
                                }
                            }

                            defsList.add(
                                FreeDictionaryDefinition(
                                    definition = defText,
                                    example = exampleText,
                                    synonyms = defSynList,
                                    antonyms = emptyList()
                                )
                            )
                        }
                    }

                    meaningsList.add(
                        FreeDictionaryMeaning(
                            partOfSpeech = partOfSpeech,
                            definitions = defsList,
                            synonyms = meaningSynonyms
                        )
                    )
                }
            }

            Result.success(
                FreeDictionaryEntry(
                    word = entryWord,
                    phonetic = phonetic,
                    audioUrl = audioUrl,
                    meanings = meaningsList
                )
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
