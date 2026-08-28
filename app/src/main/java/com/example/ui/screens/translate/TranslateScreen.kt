package com.example.ui.screens.translate

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.entities.TranslationHistoryEntity
import com.example.data.repository.TranslationResult
import com.example.ui.components.AudioSpeakButton
import com.example.ui.theme.*
import com.example.ui.viewmodels.MainViewModel
import kotlinx.coroutines.launch

@Composable
fun TranslateScreen(
    viewModel: MainViewModel,
    onSpeak: (String, Boolean) -> Unit
) {
    val translateRepo = viewModel.translateRepo
    val recentHistory by translateRepo.recentTranslations.collectAsState(initial = emptyList())
    val coroutineScope = rememberCoroutineScope()

    var sourceLang by remember { mutableStateOf("es") } // "es" or "en"
    var targetLang by remember { mutableStateOf("en") } // "en" or "es"
    var inputText by remember { mutableStateOf("") }
    var translationResult by remember { mutableStateOf<TranslationResult?>(null) }
    var isTranslating by remember { mutableStateOf(false) }
    var showCameraScanDialog by remember { mutableStateOf(false) }
    var saveSrsMessage by remember { mutableStateOf<String?>(null) }

    fun executeTranslation() {
        if (inputText.isBlank()) return
        isTranslating = true
        coroutineScope.launch {
            translationResult = translateRepo.translateText(inputText, sourceLang, targetLang)
            isTranslating = false
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        contentPadding = PaddingValues(bottom = 80.dp)
    ) {
        // Language Selector Header
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Source Language
                    Text(
                        text = if (sourceLang == "es") "Español (México)" else "Inglés (B2)",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.primary
                    )

                    // Swap Button
                    IconButton(
                        onClick = {
                            val temp = sourceLang
                            sourceLang = targetLang
                            targetLang = temp
                            // swap text if translated
                            if (translationResult != null && translationResult!!.translatedText.isNotEmpty()) {
                                inputText = translationResult!!.translatedText
                                translationResult = null
                            }
                        },
                        modifier = Modifier.testTag("swap_language_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.SwapHoriz,
                            contentDescription = "Invertir idiomas",
                            tint = BrandCoral
                        )
                    }

                    // Target Language
                    Text(
                        text = if (targetLang == "en") "Inglés (B2)" else "Español (México)",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = BrandCoral
                    )
                }
            }
        }

        // Input Card
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    OutlinedTextField(
                        value = inputText,
                        onValueChange = { inputText = it },
                        placeholder = {
                            Text(
                                if (sourceLang == "es") "Escribe o pega texto en español..." else "Type or paste English text...",
                                fontSize = 14.sp
                            )
                        },
                        minLines = 3,
                        maxLines = 6,
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedBorderColor = Color.Transparent,
                            focusedBorderColor = Color.Transparent
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("translate_input_field")
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            // Camera OCR scan button
                            IconButton(
                                onClick = { showCameraScanDialog = true },
                                modifier = Modifier.testTag("camera_ocr_btn")
                            ) {
                                Icon(Icons.Default.CameraAlt, contentDescription = "Escanear con cámara", tint = MaterialTheme.colorScheme.onSurfaceVariant)
                            }

                            // TTS speak source text
                            if (inputText.isNotBlank()) {
                                AudioSpeakButton(
                                    text = inputText,
                                    isSpanish = (sourceLang == "es"),
                                    onSpeak = onSpeak
                                )
                            }
                        }

                        // Translate Action Button
                        Button(
                            onClick = { executeTranslation() },
                            enabled = inputText.isNotBlank() && !isTranslating,
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = BrandBlue),
                            modifier = Modifier.testTag("execute_translate_btn")
                        ) {
                            if (isTranslating) {
                                CircularProgressIndicator(modifier = Modifier.size(16.dp), color = Color.White, strokeWidth = 2.dp)
                            } else {
                                Icon(Icons.Default.Translate, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Traducir", fontSize = 13.sp)
                            }
                        }
                    }
                }
            }
        }

        // Translation Result Card
        if (translationResult != null && translationResult!!.translatedText.isNotEmpty()) {
            item {
                val res = translationResult!!
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = BrandBlueLight.copy(alpha = 0.6f)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Traducción B2",
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                color = BrandBlueDark
                            )

                            AudioSpeakButton(
                                text = res.translatedText,
                                isSpanish = (targetLang == "es"),
                                onSpeak = onSpeak
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = res.translatedText,
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp,
                            lineHeight = 24.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        if (res.phonetic.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = res.phonetic,
                                fontSize = 13.sp,
                                color = BrandBlue,
                                fontWeight = FontWeight.Medium
                            )
                        }

                        if (res.notes.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "ℹ️ ${res.notes}",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // 1-Tap Save to SRS Card Button
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            Button(
                                onClick = {
                                    coroutineScope.launch {
                                        val enText = if (targetLang == "en") res.translatedText else inputText
                                        val esText = if (targetLang == "es") res.translatedText else inputText
                                        translateRepo.saveToSrs(
                                            sourceText = enText,
                                            translation = esText,
                                            phonetic = res.phonetic,
                                            notes = res.notes
                                        )
                                        saveSrsMessage = "¡Agregado al mazo de repaso SRS!"
                                    }
                                },
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = BrandCoral),
                                modifier = Modifier.testTag("save_translation_to_srs_btn")
                            ) {
                                Icon(Icons.Default.BookmarkAdd, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Guardar en Tarjetas SRS", fontSize = 12.sp)
                            }
                        }
                    }
                }
            }
        }

        // Recent Translation History
        item {
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Historial Reciente",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = MaterialTheme.colorScheme.onBackground
                )

                if (recentHistory.isNotEmpty()) {
                    TextButton(onClick = {
                        coroutineScope.launch { translateRepo.clearHistory() }
                    }) {
                        Text("Limpiar", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
        }

        if (recentHistory.isEmpty()) {
            item {
                Text(
                    text = "No hay traducciones recientes en el historial.",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            items(recentHistory, key = { it.id }) { item ->
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            inputText = item.sourceText
                            sourceLang = item.sourceLang
                            targetLang = item.targetLang
                            executeTranslation()
                        }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = item.sourceText,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = item.translatedText,
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }

                        IconButton(
                            onClick = {
                                coroutineScope.launch {
                                    translateRepo.toggleFavorite(item.id, item.isFavorite)
                                }
                            }
                        ) {
                            Icon(
                                imageVector = if (item.isFavorite) Icons.Default.Star else Icons.Default.StarBorder,
                                contentDescription = "Favorito",
                                tint = if (item.isFavorite) WarningAmber else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }

    // Camera Scan Modal Simulator
    if (showCameraScanDialog) {
        AlertDialog(
            onDismissRequest = { showCameraScanDialog = false },
            title = { Text("Escanear Texto con Cámara / OCR") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Selecciona una muestra técnica o pedagógica para traducir instantáneamente:")
                    Button(
                        onClick = {
                            inputText = "The primary gateway router failed to route packets due to an incorrect subnet mask configuration."
                            sourceLang = "en"
                            targetLang = "es"
                            showCameraScanDialog = false
                            executeTranslation()
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Texto 1: Manual Técnico de Redes")
                    }
                    Button(
                        onClick = {
                            inputText = "Los alumnos de tercer grado completaron su proyecto colaborativo de inglés."
                            sourceLang = "es"
                            targetLang = "en"
                            showCameraScanDialog = false
                            executeTranslation()
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Texto 2: Bitácora de Clase Telesecundaria")
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showCameraScanDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

    if (saveSrsMessage != null) {
        AlertDialog(
            onDismissRequest = { saveSrsMessage = null },
            title = { Text("¡Guardado en SRS!") },
            text = { Text(saveSrsMessage!!) },
            confirmButton = {
                Button(onClick = { saveSrsMessage = null }) {
                    Text("Aceptar")
                }
            }
        )
    }
}
