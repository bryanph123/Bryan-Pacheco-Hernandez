package com.example.ui.screens.learn

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.local.model.AudiobookItem
import com.example.data.local.model.AudiobookSentence
import com.example.data.local.model.KeyVocabItem
import com.example.ui.components.AudioSpeakButton
import com.example.ui.theme.*
import com.example.ui.viewmodels.LearnSubScreen
import com.example.ui.viewmodels.MainViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AudiobooksScreen(
    viewModel: MainViewModel,
    onSpeak: (String, Boolean) -> Unit
) {
    val audiobooks by viewModel.audiobooks.collectAsState()
    val selectedAudiobook by viewModel.selectedAudiobook.collectAsState()
    val currentSentenceIndex by viewModel.currentAudiobookSentenceIndex.collectAsState()
    val isPlaying by viewModel.isAudiobookPlaying.collectAsState()
    val playbackSpeed by viewModel.audiobookPlaybackSpeed.collectAsState()
    val isSpanishShown by viewModel.isAudiobookSpanishShown.collectAsState()

    var isReaderOpen by remember { mutableStateOf(false) }
    var selectedVocabItem by remember { mutableStateOf<KeyVocabItem?>(null) }
    var selectedLevelFilter by remember { mutableStateOf("TODOS") }
    var sleepTimerMinutes by remember { mutableStateOf<Int?>(null) }
    var isVocabSavedSnackbar by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()

    // Filtered books
    val filteredBooks = if (selectedLevelFilter == "TODOS") {
        audiobooks
    } else {
        audiobooks.filter { it.cefrLevel.contains(selectedLevelFilter) }
    }

    // Auto-advance sentence when playing
    LaunchedEffect(isPlaying, currentSentenceIndex, isReaderOpen) {
        if (isPlaying && isReaderOpen && selectedAudiobook != null) {
            val chapter = selectedAudiobook?.chapters?.firstOrNull()
            if (chapter != null && currentSentenceIndex < chapter.sentences.size) {
                val sentence = chapter.sentences[currentSentenceIndex]
                onSpeak(sentence.textEn, false)
                val durationMs = (sentence.durationSeconds * 1000L / playbackSpeed).toLong()
                delay(durationMs)
                if (currentSentenceIndex + 1 < chapter.sentences.size) {
                    viewModel.setAudiobookSentenceIndex(currentSentenceIndex + 1)
                } else {
                    viewModel.setAudiobookPlaying(false)
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = BrandPurple.copy(alpha = 0.15f),
                            modifier = Modifier.size(34.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    Icons.Default.Headphones,
                                    contentDescription = null,
                                    tint = BrandPurple,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                        Column {
                            Text(
                                text = if (isReaderOpen) selectedAudiobook?.titleEn ?: "Audiolibro" else "Audiolibros Bilingües",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                            Text(
                                text = if (isReaderOpen) selectedAudiobook?.titleEs ?: "Lector Sincronizado" else "Lectura guiada con audio nativo y transcripción",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            if (isReaderOpen) {
                                isReaderOpen = false
                                viewModel.setAudiobookPlaying(false)
                            } else {
                                viewModel.navigateToLearnSubScreen(LearnSubScreen.TOPICS_LIST)
                            }
                        },
                        modifier = Modifier.testTag("audiobooks_back_btn")
                    ) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    if (isReaderOpen) {
                        IconButton(
                            onClick = { viewModel.toggleAudiobookSpanish() },
                            modifier = Modifier.testTag("toggle_audiobook_spanish_btn")
                        ) {
                            Icon(
                                if (isSpanishShown) Icons.Default.Translate else Icons.Outlined.Translate,
                                contentDescription = "Alternar traducción",
                                tint = if (isSpanishShown) BrandBlue else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            if (!isReaderOpen) {
                // ==========================================
                // 1. AUDIOLIBRARY CATALOG VIEW
                // ==========================================
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 32.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Hero Banner Card
                    item {
                        Card(
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF2E1065)),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 6.dp)
                                .testTag("audiobooks_hero_card")
                        ) {
                            Box(modifier = Modifier.fillMaxWidth()) {
                                Image(
                                    painter = painterResource(id = R.drawable.img_audiobooks_hero_1787935258390),
                                    contentDescription = "Audiolibros Hero",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(170.dp)
                                )
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(170.dp)
                                        .background(
                                            Brush.verticalGradient(
                                                colors = listOf(Color.Transparent, Color(0xE61E1B4B))
                                            )
                                        )
                                )
                                Column(
                                    modifier = Modifier
                                        .align(Alignment.BottomStart)
                                        .padding(16.dp)
                                ) {
                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = BrandCoral
                                    ) {
                                        Text(
                                            text = "BIBLIOTECA GRADUADA CEFR",
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = Color.White,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "Aprende Inglés Escuchando Historias",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 17.sp,
                                        color = Color.White
                                    )
                                    Text(
                                        text = "Textos clásicos y modernos con pronunciación sincronizada",
                                        fontSize = 12.sp,
                                        color = Color.White.copy(alpha = 0.85f)
                                    )
                                }
                            }
                        }
                    }

                    // Level Filter Chips
                    item {
                        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                            Text(
                                text = "Filtrar por Nivel de Dificultad",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                items(listOf("TODOS", "A1", "A2", "B1", "B2")) { lvl ->
                                    val isSelected = selectedLevelFilter == lvl
                                    FilterChip(
                                        selected = isSelected,
                                        onClick = { selectedLevelFilter = lvl },
                                        label = {
                                            Text(
                                                text = if (lvl == "TODOS") "Todos los Niveles" else "Nivel $lvl",
                                                fontSize = 12.sp,
                                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                            )
                                        },
                                        leadingIcon = if (isSelected) {
                                            { Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp)) }
                                        } else null,
                                        colors = FilterChipDefaults.filterChipColors(
                                            selectedContainerColor = BrandPurple,
                                            selectedLabelColor = Color.White
                                        ),
                                        modifier = Modifier.testTag("filter_audiobook_level_$lvl")
                                    )
                                }
                            }
                        }
                    }

                    // Books List
                    items(filteredBooks, key = { it.id }) { book ->
                        AudiobookCardItem(
                            book = book,
                            onClick = {
                                viewModel.selectAudiobook(book.id)
                                isReaderOpen = true
                            }
                        )
                    }
                }
            } else {
                // ==========================================
                // 2. INTERACTIVE SYNCHRONIZED READER VIEW
                // ==========================================
                selectedAudiobook?.let { book ->
                    val chapter = book.chapters.firstOrNull()
                    val sentences = chapter?.sentences ?: emptyList()

                    Column(modifier = Modifier.fillMaxSize()) {
                        // Reader Header info bar
                        Surface(
                            color = MaterialTheme.colorScheme.surface,
                            tonalElevation = 2.dp,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = BrandPurpleLight
                                    ) {
                                        Text(
                                            text = book.cefrLevel,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = BrandPurpleDark,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                    Text(
                                        text = "${currentSentenceIndex + 1} de ${sentences.size} oraciones",
                                        fontSize = 12.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }

                                // Playback Speed Pill
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = MaterialTheme.colorScheme.surfaceVariant,
                                    modifier = Modifier.clickable {
                                        val nextSpeed = when (playbackSpeed) {
                                            0.75f -> 1.0f
                                            1.0f -> 1.25f
                                            1.25f -> 1.5f
                                            else -> 0.75f
                                        }
                                        viewModel.setAudiobookPlaybackSpeed(nextSpeed)
                                    }
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Icon(Icons.Default.Speed, contentDescription = null, modifier = Modifier.size(14.dp))
                                        Text(
                                            text = "${playbackSpeed}x",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        }

                        // Sentences Reader List
                        LazyColumn(
                            modifier = Modifier
                                .weight(1f)
                                .padding(horizontal = 16.dp),
                            contentPadding = PaddingValues(top = 12.dp, bottom = 100.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            itemsIndexed(sentences) { index, sentence ->
                                val isActive = index == currentSentenceIndex
                                val isSpeakingSentence = isActive && isPlaying

                                Surface(
                                    shape = RoundedCornerShape(16.dp),
                                    color = if (isActive) BrandBlueLight.copy(alpha = 0.35f) else MaterialTheme.colorScheme.surface,
                                    shadowElevation = if (isActive) 3.dp else 1.dp,
                                    border = if (isActive) androidx.compose.foundation.BorderStroke(1.5.dp, BrandBlue) else null,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            viewModel.setAudiobookSentenceIndex(index)
                                            onSpeak(sentence.textEn, false)
                                        }
                                        .testTag("audiobook_sentence_$index")
                                ) {
                                    Column(
                                        modifier = Modifier.padding(14.dp),
                                        verticalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                                            ) {
                                                Surface(
                                                    shape = CircleShape,
                                                    color = if (isActive) BrandBlue else MaterialTheme.colorScheme.surfaceVariant,
                                                    modifier = Modifier.size(22.dp)
                                                ) {
                                                    Box(contentAlignment = Alignment.Center) {
                                                        Text(
                                                            text = "${index + 1}",
                                                            fontSize = 11.sp,
                                                            fontWeight = FontWeight.Bold,
                                                            color = if (isActive) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                                                        )
                                                    }
                                                }
                                                if (isSpeakingSentence) {
                                                    Text(
                                                        text = "🔊 Leyendo...",
                                                        fontSize = 11.sp,
                                                        fontWeight = FontWeight.Bold,
                                                        color = BrandBlue
                                                    )
                                                }
                                            }

                                            AudioSpeakButton(
                                                text = sentence.textEn,
                                                isSpanish = false,
                                                onSpeak = onSpeak,
                                                modifier = Modifier.size(32.dp)
                                            )
                                        }

                                        // English Main Text
                                        Text(
                                            text = sentence.textEn,
                                            fontSize = 15.sp,
                                            fontWeight = if (isActive) FontWeight.Bold else FontWeight.Normal,
                                            color = MaterialTheme.colorScheme.onSurface,
                                            lineHeight = 22.sp
                                        )

                                        // Spanish Translation
                                        AnimatedVisibility(visible = isSpanishShown) {
                                            Text(
                                                text = sentence.textEs,
                                                fontSize = 13.sp,
                                                fontStyle = FontStyle.Italic,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                                lineHeight = 18.sp
                                            )
                                        }

                                        // Key Vocabulary Chips
                                        if (sentence.keyVocab.isNotEmpty()) {
                                            Spacer(modifier = Modifier.height(2.dp))
                                            Row(
                                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Text(
                                                    text = "Vocabulario:",
                                                    fontSize = 11.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = BrandPurpleDark
                                                )
                                                sentence.keyVocab.forEach { vocab ->
                                                    Surface(
                                                        shape = RoundedCornerShape(6.dp),
                                                        color = BrandPurpleLight,
                                                        modifier = Modifier.clickable {
                                                            selectedVocabItem = vocab
                                                        }
                                                    ) {
                                                        Text(
                                                            text = "📖 ${vocab.word}",
                                                            fontSize = 11.sp,
                                                            fontWeight = FontWeight.Medium,
                                                            color = BrandPurpleDark,
                                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        // Bottom Persistent Audio Controls Bar
                        Surface(
                            shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
                            color = MaterialTheme.colorScheme.surface,
                            shadowElevation = 8.dp,
                            tonalElevation = 4.dp,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 20.dp, vertical = 14.dp)
                            ) {
                                // Linear Progress
                                val progress = if (sentences.isNotEmpty()) (currentSentenceIndex + 1).toFloat() / sentences.size else 0f
                                LinearProgressIndicator(
                                    progress = { progress },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(6.dp)
                                        .clip(RoundedCornerShape(3.dp)),
                                    color = BrandPurple,
                                    trackColor = MaterialTheme.colorScheme.surfaceVariant
                                )

                                Spacer(modifier = Modifier.height(10.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    // Previous Sentence
                                    IconButton(
                                        onClick = {
                                            if (currentSentenceIndex > 0) {
                                                viewModel.setAudiobookSentenceIndex(currentSentenceIndex - 1)
                                            }
                                        },
                                        enabled = currentSentenceIndex > 0
                                    ) {
                                        Icon(Icons.Default.SkipPrevious, contentDescription = "Oración anterior", modifier = Modifier.size(28.dp))
                                    }

                                    // Main Play / Pause Button
                                    FloatingActionButton(
                                        onClick = {
                                            viewModel.setAudiobookPlaying(!isPlaying)
                                        },
                                        containerColor = BrandPurple,
                                        contentColor = Color.White,
                                        modifier = Modifier
                                            .size(54.dp)
                                            .testTag("audiobook_play_pause_fab")
                                    ) {
                                        Icon(
                                            if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                            contentDescription = if (isPlaying) "Pausar" else "Reproducir",
                                            modifier = Modifier.size(30.dp)
                                        )
                                    }

                                    // Next Sentence
                                    IconButton(
                                        onClick = {
                                            if (currentSentenceIndex + 1 < sentences.size) {
                                                viewModel.setAudiobookSentenceIndex(currentSentenceIndex + 1)
                                            }
                                        },
                                        enabled = currentSentenceIndex + 1 < sentences.size
                                    ) {
                                        Icon(Icons.Default.SkipNext, contentDescription = "Siguiente oración", modifier = Modifier.size(28.dp))
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Word Definition & Save-to-SRS Bottom Sheet Dialog
            selectedVocabItem?.let { vocab ->
                AlertDialog(
                    onDismissRequest = { selectedVocabItem = null },
                    confirmButton = {
                        Button(
                            onClick = {
                                viewModel.saveVocabFromReader(
                                    word = vocab.word,
                                    ipa = vocab.ipa,
                                    translation = vocab.translation,
                                    contextSentence = vocab.contextSentence
                                )
                                isVocabSavedSnackbar = true
                                selectedVocabItem = null
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = BrandPurple)
                        ) {
                            Icon(Icons.Default.BookmarkAdd, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Guardar en Repaso SRS")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { selectedVocabItem = null }) {
                            Text("Cerrar")
                        }
                    },
                    title = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(text = vocab.word, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                            AudioSpeakButton(
                                text = vocab.word,
                                isSpanish = false,
                                onSpeak = onSpeak,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                    },
                    text = {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(
                                text = "Pronunciación: ${vocab.ipa}",
                                fontSize = 13.sp,
                                color = BrandPurpleDark,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = "Significado en español: ${vocab.translation}",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium
                            )
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(10.dp)) {
                                    Text(text = "Ejemplo en contexto:", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    Text(text = vocab.contextSentence, fontSize = 13.sp, fontStyle = FontStyle.Italic)
                                }
                            }
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun AudiobookCardItem(
    book: AudiobookItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clickable(onClick = onClick)
            .testTag("audiobook_card_${book.id}")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Book Cover Image with Level Badge
            Box(
                modifier = Modifier
                    .size(90.dp)
                    .clip(RoundedCornerShape(12.dp))
            ) {
                Image(
                    painter = painterResource(id = book.coverResId),
                    contentDescription = book.titleEn,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                Surface(
                    shape = RoundedCornerShape(bottomEnd = 8.dp),
                    color = BrandPurple,
                    modifier = Modifier.align(Alignment.TopStart)
                ) {
                    Text(
                        text = book.cefrLevel,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                    )
                }
            }

            // Book Details
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = book.titleEn,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = book.titleEs,
                    fontSize = 12.sp,
                    color = BrandPurpleDark,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "Por ${book.author}",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(Icons.Default.AccessTime, contentDescription = null, modifier = Modifier.size(12.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(text = "${book.durationMinutes} min", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(text = "•", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(text = "Lectura Guiada", fontSize = 11.sp, color = SuccessGreenDark, fontWeight = FontWeight.Bold)
                }
            }

            // Arrow Action
            Surface(
                shape = CircleShape,
                color = BrandPurpleLight,
                modifier = Modifier.size(36.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        Icons.Default.Headphones,
                        contentDescription = "Escuchar",
                        tint = BrandPurple,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}
