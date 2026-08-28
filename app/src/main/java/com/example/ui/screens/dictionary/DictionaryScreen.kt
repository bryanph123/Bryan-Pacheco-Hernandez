package com.example.ui.screens.dictionary

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
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
import com.example.data.repository.DictionaryResult
import com.example.ui.components.AudioSpeakButton
import com.example.ui.theme.*
import com.example.ui.viewmodels.MainViewModel
import kotlinx.coroutines.launch

@Composable
fun DictionaryScreen(
    viewModel: MainViewModel,
    onSpeak: (String, Boolean) -> Unit
) {
    val dictRepo = viewModel.dictionaryRepo
    val recentLookups by dictRepo.recentLookups.collectAsState(initial = emptyList())
    val coroutineScope = rememberCoroutineScope()

    var searchQuery by remember { mutableStateOf("") }
    var dictionaryResult by remember { mutableStateOf<DictionaryResult?>(null) }
    var isSearching by remember { mutableStateOf(false) }
    var srsSavedMessage by remember { mutableStateOf<String?>(null) }

    val popularTerms = listOf(
        "troubleshoot", "scaffolding", "bandwidth", "carry out",
        "nevertheless", "assessment", "hit the ground running", "bottleneck"
    )

    fun performSearch(term: String) {
        if (term.isBlank()) return
        searchQuery = term
        isSearching = true
        coroutineScope.launch {
            dictionaryResult = dictRepo.lookupTerm(term)
            isSearching = false
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
        // Search Header Card
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "Diccionario Inteligente B2",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = { Text("Busca una palabra, phrasal verb o modismo...", fontSize = 13.sp) },
                        leadingIcon = {
                            Icon(Icons.Default.Search, contentDescription = "Buscar", tint = MaterialTheme.colorScheme.onSurfaceVariant)
                        },
                        trailingIcon = {
                            if (searchQuery.isNotEmpty()) {
                                IconButton(onClick = { searchQuery = "" }) {
                                    Icon(Icons.Default.Close, contentDescription = "Limpiar")
                                }
                            }
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("dictionary_search_input")
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Button(
                        onClick = { performSearch(searchQuery) },
                        enabled = searchQuery.isNotBlank() && !isSearching,
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = BrandBlue),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("execute_dict_search_btn")
                    ) {
                        if (isSearching) {
                            CircularProgressIndicator(modifier = Modifier.size(16.dp), color = Color.White, strokeWidth = 2.dp)
                        } else {
                            Text("Consultar Definición Completa", fontSize = 14.sp)
                        }
                    }
                }
            }
        }

        // Quick Suggestion Chips
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                popularTerms.forEach { term ->
                    SuggestionChip(
                        onClick = { performSearch(term) },
                        label = { Text(term, fontSize = 11.sp) },
                        colors = SuggestionChipDefaults.suggestionChipColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        ),
                        modifier = Modifier.testTag("dict_suggest_$term")
                    )
                }
            }
        }

        // Search Result Entry Card
        if (dictionaryResult != null && dictionaryResult!!.term.isNotEmpty()) {
            item {
                val res = dictionaryResult!!
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        // Title & Phonetic
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = res.term,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 22.sp,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                if (res.phonetic.isNotEmpty()) {
                                    Text(
                                        text = res.phonetic,
                                        fontSize = 14.sp,
                                        color = BrandBlueDark,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }

                            AudioSpeakButton(
                                text = res.term,
                                onSpeak = onSpeak,
                                size = 42
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        // Part of speech
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = BrandCoralLight
                        ) {
                            Text(
                                text = res.partOfSpeech.uppercase(),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = BrandCoral,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Spanish Definition
                        Text(
                            text = res.definitionEs,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        // English Definition
                        Text(
                            text = res.definitionEn,
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        // Examples
                        if (res.examples.isNotEmpty()) {
                            Text(
                                text = "Ejemplos en Contexto B2:",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            res.examples.forEach { (en, es) ->
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 3.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(10.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(text = en, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurface)
                                            Text(text = es, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                        }
                                        AudioSpeakButton(text = en, onSpeak = onSpeak, size = 32)
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                        }

                        // Synonyms
                        if (res.synonyms.isNotEmpty()) {
                            Text(
                                text = "Sinónimos:",
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(
                                modifier = Modifier.horizontalScroll(rememberScrollState()),
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                res.synonyms.forEach { syn ->
                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = BrandBlueLight
                                    ) {
                                        Text(
                                            text = syn,
                                            fontSize = 11.sp,
                                            color = BrandBlue,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                        }

                        // Usage Notes
                        if (res.usageNotes.isNotEmpty()) {
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = WarningAmberLight,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(10.dp)) {
                                    Text(
                                        text = "💡 Nota de Registro y Uso:",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 11.sp,
                                        color = WarningAmber
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = res.usageNotes,
                                        fontSize = 12.sp,
                                        lineHeight = 17.sp,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                        }

                        // Save to SRS Button
                        Button(
                            onClick = {
                                coroutineScope.launch {
                                    dictRepo.saveToSrs(res)
                                    srsSavedMessage = "¡Término añadido a tus tarjetas de repaso espaciado!"
                                }
                            },
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = BrandCoral),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("save_dict_to_srs_btn")
                        ) {
                            Icon(Icons.Default.BookmarkAdd, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Guardar Palabra en Tarjetas SRS", fontSize = 13.sp)
                        }
                    }
                }
            }
        }

        // Recent Lookups
        item {
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Búsquedas Recientes",
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        if (recentLookups.isEmpty()) {
            item {
                Text(
                    text = "Aún no has consultado términos.",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            items(recentLookups, key = { it.id }) { item ->
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { performSearch(item.term) }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = item.term,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = item.definitionEs,
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                maxLines = 1
                            )
                        }
                        Icon(Icons.Default.ChevronRight, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
        }
    }

    if (srsSavedMessage != null) {
        AlertDialog(
            onDismissRequest = { srsSavedMessage = null },
            title = { Text("¡Guardado en SRS!") },
            text = { Text(srsSavedMessage!!) },
            confirmButton = {
                Button(onClick = { srsSavedMessage = null }) {
                    Text("Aceptar")
                }
            }
        )
    }
}
