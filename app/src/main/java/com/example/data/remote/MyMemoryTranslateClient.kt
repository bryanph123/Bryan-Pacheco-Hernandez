package com.example.data.remote

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.net.URLEncoder
import java.util.concurrent.TimeUnit

data class MyMemoryResponse(
    val translatedText: String,
    val matchScore: Float,
    val isSuccess: Boolean
)

object MyMemoryTranslateClient {
    private val client = OkHttpClient.Builder()
        .connectTimeout(12, TimeUnit.SECONDS)
        .readTimeout(12, TimeUnit.SECONDS)
        .build()

    suspend fun translate(
        text: String,
        sourceLang: String, // "es" or "en"
        targetLang: String  // "en" or "es"
    ): Result<MyMemoryResponse> = withContext(Dispatchers.IO) {
        try {
            val cleanText = text.trim()
            if (cleanText.isEmpty()) {
                return@withContext Result.failure(Exception("Texto vacío"))
            }

            val encoded = URLEncoder.encode(cleanText, "UTF-8")
            val langPair = "${sourceLang.lowercase()}|${targetLang.lowercase()}"
            val url = "https://api.mymemory.translated.net/get?q=$encoded&langpair=$langPair"

            val request = Request.Builder()
                .url(url)
                .header("User-Agent", "InglesB2App/1.0")
                .build()

            val response = client.newCall(request).execute()
            if (!response.isSuccessful) {
                return@withContext Result.failure(Exception("HTTP ${response.code}"))
            }

            val body = response.body?.string() ?: ""
            if (body.isEmpty()) {
                return@withContext Result.failure(Exception("Cuerpo de respuesta vacío"))
            }

            val json = JSONObject(body)
            val responseData = json.optJSONObject("responseData")
            val translatedText = responseData?.optString("translatedText", "") ?: ""
            val match = responseData?.optDouble("match", 0.0)?.toFloat() ?: 0f

            if (translatedText.isNotBlank()) {
                // Decode HTML entities if any (like &#39;)
                val cleanTranslated = android.text.Html.fromHtml(translatedText, android.text.Html.FROM_HTML_MODE_LEGACY).toString()
                Result.success(
                    MyMemoryResponse(
                        translatedText = cleanTranslated,
                        matchScore = match,
                        isSuccess = true
                    )
                )
            } else {
                Result.failure(Exception("Traducción no encontrada"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
