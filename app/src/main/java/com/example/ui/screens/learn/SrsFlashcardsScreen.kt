package com.example.ui.screens.learn

import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.VocabWordItem
import com.example.data.local.VocabularyBankData
import com.example.ui.components.AudioSpeakButton
import com.example.ui.components.ScreenTopBar
import com.example.ui.theme.*
import com.example.ui.viewmodels.LearnSubScreen
import com.example.ui.viewmodels.MainViewModel

enum class VocabViewMode {
    EXPLORER_LIST,
    FLASHCARD_DECK
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SrsFlashcardsScreen(
    viewModel: MainViewModel,
    onSpeak: (String, Boolean) -> Unit
) {
    val dueItems by viewModel.dueVocabItems.collectAsState()
    val allSavedItems by viewModel.allVocabItems.collectAsState()
    val focusManager = LocalFocusManager.current

    // Search and Filter States
    var searchQuery by remember { mutableStateOf("") }
    var selectedLevelFilter by remember { mutableStateOf("TODOS") }
    var selectedTopicFilter by remember { mutableStateOf("Todos los Temas") }
    var selectedStatusFilter by remember { mutableStateOf("TODOS") }
    var viewMode by remember { mutableStateOf(VocabViewMode.EXPLORER_LIST) }

    // Aggregate all vocabulary words (Saved SRS + Pre-populated Multi-Level Bank)
    val allVocabWords = remember(allSavedItems) {
        VocabularyBankData.getAllVocabulary(allSavedItems)
    }

    // Filter in real-time based on search query, level, topic, and status
    val filteredVocabWords = remember(
        allVocabWords,
        searchQuery,
        selectedLevelFilter,
        selectedTopicFilter,
        selectedStatusFilter
    ) {
        VocabularyBankData.filterVocabulary(
            items = allVocabWords,
            query = searchQuery,
            levelFilter = selectedLevelFilter,
            topicFilter = selectedTopicFilter,
            statusFilter = selectedStatusFilter
        )
    }

    // Flashcard Deck State based on filtered words
    var cardIndex by remember { mutableIntStateOf(0) }
    var isFlipped by remember { mutableStateOf(false) }

    // Keep card index safe when filter changes
    LaunchedEffect(filteredVocabWords.size) {
        if (cardIndex >= filteredVocabWords.size) {
            cardIndex = 0
        }
        isFlipped = false
    }

    val currentCard: VocabWordItem? = filteredVocabWords.getOrNull(cardIndex)

    Scaffold(
        topBar = {
            ScreenTopBar(
                title = "Vocabulario y Repaso SRS",
                subtitle = "Búsqueda en tiempo real por nivel, tema o traducción",
                onBack = { viewModel.navigateToLearnSubScreen(LearnSubScreen.TOPICS_LIST) },
                actions = {
                    IconButton(
                        onClick = {
                            viewMode = if (viewMode == VocabViewMode.EXPLORER_LIST) {
                                VocabViewMode.FLASHCARD_DECK
                            } else {
                                VocabViewMode.EXPLORER_LIST
                            }
                        },
                        modifier = Modifier.testTag("toggle_vocab_view_mode_btn")
                    ) {
                        Icon(
                            imageVector = if (viewMode == VocabViewMode.EXPLORER_LIST) Icons.Default.Style else Icons.Default.ViewList,
                            contentDescription = "Cambiar vista",
                            tint = BrandBlue
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            // ================= 1. SEARCH BAR & FILTERS HEADER =================
            Card(
                shape = RoundedCornerShape(0.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
                    // REAL-TIME SEARCH BAR
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("vocab_search_input"),
                        placeholder = {
                            Text(
                                text = "Buscar palabra, traducción, tema o nivel...",
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                            )
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Buscar",
                                tint = BrandBlue
                            )
                        },
                        trailingIcon = {
                            if (searchQuery.isNotEmpty()) {
                                IconButton(
                                    onClick = { searchQuery = "" },
                                    modifier = Modifier.testTag("clear_vocab_search_btn")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Limpiar búsqueda",
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = BrandBlue,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                            focusedContainerColor = MaterialTheme.colorScheme.surface,
                            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f)
                        ),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                        keyboardActions = KeyboardActions(onSearch = { focusManager.clearFocus() })
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // LEVEL FILTER CHIPS
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(VocabularyBankData.levelsList) { (lvlKey, lvlLabel) ->
                            val isSelected = selectedLevelFilter == lvlKey
                            FilterChip(
                                selected = isSelected,
                                onClick = { selectedLevelFilter = lvlKey },
                                label = {
                                    Text(
                                        text = lvlLabel,
                                        fontSize = 12.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                    )
                                },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = BrandBlueLight,
                                    selectedLabelColor = BrandBlue,
                                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                                ),
                                modifier = Modifier.testTag("filter_level_${lvlKey.lowercase()}")
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    // TOPIC FILTER CHIPS
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(VocabularyBankData.topicsList) { topicName ->
                            val isSelected = selectedTopicFilter == topicName
                            ElevatedFilterChip(
                                selected = isSelected,
                                onClick = { selectedTopicFilter = topicName },
                                label = {
                                    Text(
                                        text = topicName,
                                        fontSize = 11.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                    )
                                },
                                colors = FilterChipDefaults.elevatedFilterChipColors(
                                    selectedContainerColor = BrandCoralLight,
                                    selectedLabelColor = BrandCoral
                                ),
                                modifier = Modifier.testTag("filter_topic_${topicName.take(6).lowercase()}")
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // MODE SWITCHER & STATS SUMMARY
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${filteredVocabWords.size} palabras encontradas" +
                                if (searchQuery.isNotEmpty()) " para \"$searchQuery\"" else "",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        // Mode Toggle Buttons
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                                .padding(2.dp)
                        ) {
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = if (viewMode == VocabViewMode.EXPLORER_LIST) BrandBlue else Color.Transparent,
                                modifier = Modifier
                                    .clickable { viewMode = VocabViewMode.EXPLORER_LIST }
                                    .testTag("mode_list_tab")
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        Icons.Default.ViewList,
                                        contentDescription = null,
                                        tint = if (viewMode == VocabViewMode.EXPLORER_LIST) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "Lista",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = if (viewMode == VocabViewMode.EXPLORER_LIST) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }

                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = if (viewMode == VocabViewMode.FLASHCARD_DECK) BrandBlue else Color.Transparent,
                                modifier = Modifier
                                    .clickable { viewMode = VocabViewMode.FLASHCARD_DECK }
                                    .testTag("mode_flashcards_tab")
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        Icons.Default.Style,
                                        contentDescription = null,
                                        tint = if (viewMode == VocabViewMode.FLASHCARD_DECK) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "Tarjetas SRS",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = if (viewMode == VocabViewMode.FLASHCARD_DECK) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // ================= 2. CONTENT AREA =================
            if (filteredVocabWords.isEmpty()) {
                // Empty state when search yields no result
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                            modifier = Modifier.size(80.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.SearchOff,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(36.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "No se encontraron palabras",
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Intenta buscar por otra traducción, término en inglés, nivel (A1–B2) o tema.",
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        OutlinedButton(
                            onClick = {
                                searchQuery = ""
                                selectedLevelFilter = "TODOS"
                                selectedTopicFilter = "Todos los Temas"
                                selectedStatusFilter = "TODOS"
                            },
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.testTag("reset_vocab_filters_btn")
                        ) {
                            Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Restablecer Filtros y Ver Todo", fontSize = 13.sp)
                        }
                    }
                }
            } else if (viewMode == VocabViewMode.EXPLORER_LIST) {
                // ================= EXPLORER LIST VIEW =================
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    contentPadding = PaddingValues(vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(filteredVocabWords, key = { it.id }) { item ->
                        VocabItemCard(
                            item = item,
                            searchQuery = searchQuery,
                            onSpeak = onSpeak,
                            onPracticeInFlashcard = {
                                cardIndex = filteredVocabWords.indexOf(item).coerceAtLeast(0)
                                viewMode = VocabViewMode.FLASHCARD_DECK
                            },
                            onSaveToSrs = {
                                viewModel.saveWordToSrs(
                                    term = item.word,
                                    translation = item.translation,
                                    sourceModule = "cambridge_${item.level}"
                                )
                            }
                        )
                    }
                }
            } else {
                // ================= FLASHCARD DECK VIEW =================
                if (currentCard != null) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Card Progress Bar & Counter
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Tarjeta ${cardIndex + 1} de ${filteredVocabWords.size}",
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            // Quick Level & Topic Pills
                            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                Surface(
                                    shape = RoundedCornerShape(4.dp),
                                    color = BrandBlueLight
                                ) {
                                    Text(
                                        text = currentCard.level,
                                        color = BrandBlue,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                                Surface(
                                    shape = RoundedCornerShape(4.dp),
                                    color = BrandCoralLight
                                ) {
                                    Text(
                                        text = currentCard.topic,
                                        color = BrandCoral,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Medium,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        LinearProgressIndicator(
                            progress = { (cardIndex + 1).toFloat() / filteredVocabWords.size.coerceAtLeast(1) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp)),
                            color = BrandBlue,
                            trackColor = MaterialTheme.colorScheme.surfaceVariant
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        // Interactive 3D Card
                        val rotation by animateFloatAsState(
                            targetValue = if (isFlipped) 180f else 0f,
                            animationSpec = tween(400),
                            label = "card_flip"
                        )

                        Card(
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (isFlipped) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.surface
                            ),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .graphicsLayer {
                                    rotationY = rotation
                                    cameraDistance = 12f * density
                                }
                                .clickable { isFlipped = !isFlipped }
                                .testTag("srs_flashcard_card")
                        ) {
                            if (rotation <= 90f) {
                                // FRONT SIDE (English Word + Audio + Topic + Prompt)
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(24.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Surface(
                                            shape = RoundedCornerShape(6.dp),
                                            color = MaterialTheme.colorScheme.surfaceVariant
                                        ) {
                                            Text(
                                                text = "ANVERSO · INGLÉS",
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                            )
                                        }

                                        AudioSpeakButton(
                                            text = currentCard.word,
                                            isSpanish = false,
                                            onSpeak = onSpeak
                                        )
                                    }

                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        modifier = Modifier.padding(vertical = 12.dp)
                                    ) {
                                        Text(
                                            text = currentCard.word,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 28.sp,
                                            textAlign = TextAlign.Center,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )

                                        if (currentCard.phonetic.isNotEmpty()) {
                                            Spacer(modifier = Modifier.height(6.dp))
                                            Text(
                                                text = currentCard.phonetic,
                                                fontSize = 15.sp,
                                                color = MaterialTheme.colorScheme.primary,
                                                fontWeight = FontWeight.Medium
                                            )
                                        }

                                        Spacer(modifier = Modifier.height(12.dp))
                                        Text(
                                            text = "Tema: ${currentCard.topic}",
                                            fontSize = 12.sp,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }

                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.Center,
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Icon(
                                            Icons.Default.TouchApp,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.primary,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = "Toca para ver traducción y significado",
                                            fontSize = 12.sp,
                                            color = MaterialTheme.colorScheme.primary,
                                            fontWeight = FontWeight.Medium
                                        )
                                    }
                                }
                            } else {
                                // BACK SIDE (Spanish Translation + Definition + Context Example)
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(24.dp)
                                        .graphicsLayer { rotationY = 180f },
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Surface(
                                            shape = RoundedCornerShape(6.dp),
                                            color = SuccessGreenLight
                                        ) {
                                            Text(
                                                text = "REVERSO · ESPAÑOL",
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = SuccessGreen,
                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                            )
                                        }

                                        AudioSpeakButton(
                                            text = currentCard.word,
                                            isSpanish = false,
                                            onSpeak = onSpeak
                                        )
                                    }

                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        modifier = Modifier.padding(vertical = 8.dp)
                                    ) {
                                        Text(
                                            text = currentCard.translation,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 24.sp,
                                            textAlign = TextAlign.Center,
                                            color = BrandCoral
                                        )

                                        if (currentCard.definition.isNotEmpty()) {
                                            Spacer(modifier = Modifier.height(10.dp))
                                            Text(
                                                text = currentCard.definition,
                                                fontSize = 13.sp,
                                                textAlign = TextAlign.Center,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }

                                        if (currentCard.exampleEn.isNotEmpty()) {
                                            Spacer(modifier = Modifier.height(12.dp))
                                            Surface(
                                                shape = RoundedCornerShape(10.dp),
                                                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                                                modifier = Modifier.fillMaxWidth()
                                            ) {
                                                Column(modifier = Modifier.padding(10.dp)) {
                                                    Text(
                                                        text = "Ejemplo:",
                                                        fontSize = 11.sp,
                                                        fontWeight = FontWeight.Bold,
                                                        color = MaterialTheme.colorScheme.primary
                                                    )
                                                    Text(
                                                        text = currentCard.exampleEn,
                                                        fontSize = 12.sp,
                                                        fontWeight = FontWeight.Medium,
                                                        color = MaterialTheme.colorScheme.onSurface
                                                    )
                                                    Text(
                                                        text = currentCard.exampleEs,
                                                        fontSize = 11.sp,
                                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                                    )
                                                }
                                            }
                                        }
                                    }

                                    Text(
                                        text = "Nivel ${currentCard.level} • ${currentCard.topic}",
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // SRS Rating Buttons or Next / Prev Controls
                        if (isFlipped) {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    text = "¿Qué tan bien lo recordabas? (Algoritmo SM-2)",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.align(Alignment.CenterHorizontally)
                                )

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    // 1. Otra vez
                                    RatingButton(
                                        label = "1. Otra vez",
                                        sub = "<1 día",
                                        color = ErrorRed,
                                        modifier = Modifier
                                            .weight(1f)
                                            .testTag("srs_rate_1"),
                                        onClick = {
                                            currentCard.originalSavedItem?.let { viewModel.reviewSrsCard(it, 1) }
                                            isFlipped = false
                                            cardIndex = (cardIndex + 1) % filteredVocabWords.size
                                        }
                                    )

                                    // 2. Difícil
                                    RatingButton(
                                        label = "2. Difícil",
                                        sub = "1 día",
                                        color = WarningAmber,
                                        modifier = Modifier
                                            .weight(1f)
                                            .testTag("srs_rate_2"),
                                        onClick = {
                                            currentCard.originalSavedItem?.let { viewModel.reviewSrsCard(it, 2) }
                                            isFlipped = false
                                            cardIndex = (cardIndex + 1) % filteredVocabWords.size
                                        }
                                    )

                                    // 3. Bien
                                    RatingButton(
                                        label = "3. Bien",
                                        sub = "${(currentCard.srsIntervalDays * 2.5).toInt().coerceAtLeast(2)}d",
                                        color = BrandBlue,
                                        modifier = Modifier
                                            .weight(1f)
                                            .testTag("srs_rate_3"),
                                        onClick = {
                                            currentCard.originalSavedItem?.let { viewModel.reviewSrsCard(it, 4) }
                                            isFlipped = false
                                            cardIndex = (cardIndex + 1) % filteredVocabWords.size
                                        }
                                    )

                                    // 4. Fácil
                                    RatingButton(
                                        label = "4. Fácil",
                                        sub = "${(currentCard.srsIntervalDays * 3.5).toInt().coerceAtLeast(4)}d",
                                        color = SuccessGreen,
                                        modifier = Modifier
                                            .weight(1f)
                                            .testTag("srs_rate_4"),
                                        onClick = {
                                            currentCard.originalSavedItem?.let { viewModel.reviewSrsCard(it, 5) }
                                            isFlipped = false
                                            cardIndex = (cardIndex + 1) % filteredVocabWords.size
                                        }
                                    )
                                }
                            }
                        } else {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                IconButton(
                                    onClick = {
                                        if (cardIndex > 0) cardIndex-- else cardIndex = filteredVocabWords.size - 1
                                    },
                                    modifier = Modifier.size(44.dp)
                                ) {
                                    Icon(Icons.Default.ChevronLeft, contentDescription = "Anterior")
                                }

                                Button(
                                    onClick = { isFlipped = true },
                                    shape = RoundedCornerShape(12.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = BrandBlue),
                                    modifier = Modifier
                                        .weight(1f)
                                        .testTag("srs_reveal_answer_btn")
                                ) {
                                    Icon(Icons.Default.Visibility, contentDescription = null, modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Ver Traducción y Respuesta", fontSize = 14.sp)
                                }

                                IconButton(
                                    onClick = {
                                        cardIndex = (cardIndex + 1) % filteredVocabWords.size
                                    },
                                    modifier = Modifier.size(44.dp)
                                ) {
                                    Icon(Icons.Default.ChevronRight, contentDescription = "Siguiente")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * Clean, modern Vocabulary Item Card with audio, phonetic, translation, badges, and quick actions
 */
@Composable
fun VocabItemCard(
    item: VocabWordItem,
    searchQuery: String,
    onSpeak: (String, Boolean) -> Unit,
    onPracticeInFlashcard: () -> Unit,
    onSaveToSrs: () -> Unit
) {
    var isSavedLocally by remember { mutableStateOf(item.isFromUserSrs) }

    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("vocab_card_${item.word.lowercase().replace(" ", "_")}")
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Row 1: English Word, Audio, and Level/Topic Badges
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = item.word,
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    AudioSpeakButton(
                        text = item.word,
                        isSpanish = false,
                        onSpeak = onSpeak
                    )
                }

                // Level Badge
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = when (item.level) {
                        "A1" -> BrandBlueLight
                        "A2" -> BrandCoralLight
                        "B1" -> BrandPurpleLight
                        "B2" -> SuccessGreenLight
                        else -> MaterialTheme.colorScheme.surfaceVariant
                    }
                ) {
                    Text(
                        text = item.level,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = when (item.level) {
                            "A1" -> BrandBlue
                            "A2" -> BrandCoral
                            "B1" -> BrandPurple
                            "B2" -> SuccessGreen
                            else -> MaterialTheme.colorScheme.onSurfaceVariant
                        },
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            if (item.phonetic.isNotEmpty()) {
                Text(
                    text = item.phonetic,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Row 2: Spanish Translation
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Translate,
                    contentDescription = null,
                    tint = BrandCoral,
                    modifier = Modifier.size(15.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = item.translation,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    color = BrandCoral
                )
            }

            if (item.definition.isNotEmpty()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = item.definition,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 16.sp
                )
            }

            if (item.exampleEn.isNotEmpty()) {
                Spacer(modifier = Modifier.height(6.dp))
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        Text(
                            text = item.exampleEn,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        if (item.exampleEs.isNotEmpty()) {
                            Text(
                                text = item.exampleEs,
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Row 3: Topic badge & Quick Practice Actions
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
                ) {
                    Text(
                        text = item.topic,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    if (!isSavedLocally) {
                        FilledTonalButton(
                            onClick = {
                                onSaveToSrs()
                                isSavedLocally = true
                            },
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                            modifier = Modifier.height(28.dp)
                        ) {
                            Icon(Icons.Default.BookmarkAdd, contentDescription = null, modifier = Modifier.size(12.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Guardar SRS", fontSize = 10.sp)
                        }
                    }

                    OutlinedButton(
                        onClick = onPracticeInFlashcard,
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                        modifier = Modifier.height(28.dp)
                    ) {
                        Icon(Icons.Default.Style, contentDescription = null, modifier = Modifier.size(12.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Repasar Tarjeta", fontSize = 10.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun RatingButton(
    label: String,
    sub: String,
    color: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = color.copy(alpha = 0.12f),
        modifier = modifier.clickable { onClick() }
    ) {
        Column(
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = label,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = color
            )
            Text(
                text = sub,
                fontSize = 10.sp,
                color = color.copy(alpha = 0.8f)
            )
        }
    }
}
