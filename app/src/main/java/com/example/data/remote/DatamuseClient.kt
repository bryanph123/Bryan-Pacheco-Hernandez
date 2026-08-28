package com.example.data.remote

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import java.net.URLEncoder
import java.util.concurrent.TimeUnit

data class DatamuseWord(
    val word: String,
    val score: Int,
    val tags: List<String> = emptyList()
)

object DatamuseClient {
    private val client = OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(10, TimeUnit.SECONDS)
        .build()

    suspend fun getSynonyms(word: String, max: Int = 8): Result<List<String>> = withContext(Dispatchers.IO) {
        try {
            val encoded = URLEncoder.encode(word.trim(), "UTF-8")
            val url = "https://api.datamuse.com/words?rel_syn=$encoded&max=$max"
            val request = Request.Builder().url(url).build()
            val response = client.newCall(request).execute()
            if (!response.isSuccessful) return@withContext Result.failure(Exception("HTTP ${response.code}"))

            val body = response.body?.string() ?: ""
            val jsonArray = JSONArray(body)
            val list = mutableListOf<String>()
            for (i in 0 until jsonArray.length()) {
                val obj = jsonArray.getJSONObject(i)
                list.add(obj.getString("word"))
            }
            Result.success(list)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getRelatedWords(meaningLike: String, max: Int = 8): Result<List<String>> = withContext(Dispatchers.IO) {
        try {
            val encoded = URLEncoder.encode(meaningLike.trim(), "UTF-8")
            val url = "https://api.datamuse.com/words?ml=$encoded&max=$max"
            val request = Request.Builder().url(url).build()
            val response = client.newCall(request).execute()
            if (!response.isSuccessful) return@withContext Result.failure(Exception("HTTP ${response.code}"))

            val body = response.body?.string() ?: ""
            val jsonArray = JSONArray(body)
            val list = mutableListOf<String>()
            for (i in 0 until jsonArray.length()) {
                val obj = jsonArray.getJSONObject(i)
                list.add(obj.getString("word"))
            }
            Result.success(list)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
