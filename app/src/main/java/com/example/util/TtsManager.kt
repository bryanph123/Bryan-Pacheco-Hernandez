package com.example.util

import android.content.Context
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Locale

class TtsManager(context: Context) : TextToSpeech.OnInitListener {
    private var tts: TextToSpeech? = TextToSpeech(context.applicationContext, this)
    private var isInitialized = false

    private val _isSpeaking = MutableStateFlow(false)
    val isSpeaking: StateFlow<Boolean> = _isSpeaking.asStateFlow()

    private val _speakingText = MutableStateFlow<String?>(null)
    val speakingText: StateFlow<String?> = _speakingText.asStateFlow()

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            isInitialized = true
            tts?.language = Locale.UK
            tts?.setPitch(1.0f)
            tts?.setSpeechRate(0.95f) // slightly slower for optimal comprehension

            tts?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                override fun onStart(utteranceId: String?) {
                    _isSpeaking.value = true
                }

                override fun onDone(utteranceId: String?) {
                    _isSpeaking.value = false
                    _speakingText.value = null
                }

                override fun onError(utteranceId: String?) {
                    _isSpeaking.value = false
                    _speakingText.value = null
                }
            })
        }
    }

    fun speak(
        text: String,
        isSpanish: Boolean = false,
        speechRate: Float = 0.95f,
        accent: String = "UK"
    ) {
        if (!isInitialized || tts == null) return
        if (text.isBlank()) return

        val locale = if (isSpanish) {
            Locale("es", "MX")
        } else {
            if (accent.equals("US", ignoreCase = true)) Locale.US else Locale.UK
        }

        // Set locale safely with fallback
        try {
            val result = tts?.setLanguage(locale)
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                tts?.language = Locale.US
            }
        } catch (_: Exception) {
            tts?.language = Locale.US
        }

        _speakingText.value = text
        tts?.setSpeechRate(speechRate)
        tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "tts_${System.currentTimeMillis()}")
    }

    fun stop() {
        tts?.stop()
        _isSpeaking.value = false
        _speakingText.value = null
    }

    fun shutdown() {
        tts?.stop()
        tts?.shutdown()
        tts = null
        _isSpeaking.value = false
        _speakingText.value = null
    }
}

