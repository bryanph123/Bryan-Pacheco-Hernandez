package com.example.ui.screens.learn

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import com.example.data.local.model.VideoLessonItem
import com.example.data.local.model.VideoTimelineMarker
import com.example.ui.components.AudioSpeakButton
import com.example.ui.theme.*
import com.example.ui.viewmodels.LearnSubScreen
import com.example.ui.viewmodels.MainViewModel
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VideoLessonsScreen(
    viewModel: MainViewModel,
    onSpeak: (String, Boolean) -> Unit
) {
    val videoLessons by viewModel.videoLessons.collectAsState()
    val selectedLesson by viewModel.selectedVideoLesson.collectAsState()
    val currentTimeSeconds by viewModel.videoCurrentTimeSeconds.collectAsState()
    val isPlaying by viewModel.isVideoPlaying.collectAsState()
    val isSpanishSubtitles by viewModel.isVideoSubtitlesSpanish.collectAsState()

    var isPlayerOpen by remember { mutableStateOf(false) }
    var selectedCategoryFilter by remember { mutableStateOf("Todas") }

    val categories = listOf("Todas", "Speaking", "Grammar", "Pronunciación")

    val filteredLessons = if (selectedCategoryFilter == "Todas") {
        videoLessons
    } else {
        videoLessons.filter { it.category.equals(selectedCategoryFilter, ignoreCase = true) }
    }

    // Active marker according to current time
    val currentMarker = remember(selectedLesson, currentTimeSeconds) {
        selectedLesson?.timeline?.lastOrNull { it.timeSeconds <= currentTimeSeconds }
            ?: selectedLesson?.timeline?.firstOrNull()
    }

    // Time ticker when playing
    LaunchedEffect(isPlaying, isPlayerOpen) {
        while (isPlaying && isPlayerOpen) {
            delay(1000L)
            val maxDuration = (selectedLesson?.durationMinutes ?: 10) * 60
            if (currentTimeSeconds < maxDuration) {
                viewModel.setVideoTimeSeconds(currentTimeSeconds + 1)
            } else {
                viewModel.setVideoPlaying(false)
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
                            color = BrandCoral.copy(alpha = 0.15f),
                            modifier = Modifier.size(34.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    Icons.Default.SmartDisplay,
                                    contentDescription = null,
                                    tint = BrandCoral,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                        Column {
                            Text(
                                text = if (isPlayerOpen) selectedLesson?.titleEn ?: "Videoclase" else "Videoclases y Masterclasses",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                            Text(
                                text = if (isPlayerOpen) selectedLesson?.titleEs ?: "Aula Interactiva" else "Estrategias de examen y explicaciones visuales",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            if (isPlayerOpen) {
                                isPlayerOpen = false
                                viewModel.setVideoPlaying(false)
                            } else {
                                viewModel.navigateToLearnSubScreen(LearnSubScreen.TOPICS_LIST)
                            }
                        },
                        modifier = Modifier.testTag("video_lessons_back_btn")
                    ) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    if (isPlayerOpen) {
                        IconButton(
                            onClick = { viewModel.toggleVideoSubtitlesSpanish() },
                            modifier = Modifier.testTag("toggle_video_subtitles_btn")
                        ) {
                            Icon(
                                if (isSpanishSubtitles) Icons.Default.Subtitles else Icons.Outlined.Subtitles,
                                contentDescription = "Alternar subtítulos",
                                tint = if (isSpanishSubtitles) BrandCoral else MaterialTheme.colorScheme.onSurfaceVariant
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
            if (!isPlayerOpen) {
                // ==========================================
                // 1. VIDEO LESSONS CATALOG VIEW
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
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A)),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 6.dp)
                                .testTag("video_lessons_hero_card")
                        ) {
                            Box(modifier = Modifier.fillMaxWidth()) {
                                Image(
                                    painter = painterResource(id = R.drawable.img_video_lessons_hero_1787935273584),
                                    contentDescription = "Video Lessons Hero",
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
                                                colors = listOf(Color.Transparent, Color(0xE60F172A))
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
                                        color = BrandBlue
                                    ) {
                                        Text(
                                            text = "AULA VIRTUAL CAMBRIDGE",
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = Color.White,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "Masterclasses con Profesores y Examinadores",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 17.sp,
                                        color = Color.White
                                    )
                                    Text(
                                        text = "Aprende técnicas reales para aprobar tus exámenes",
                                        fontSize = 12.sp,
                                        color = Color.White.copy(alpha = 0.85f)
                                    )
                                }
                            }
                        }
                    }

                    // Category Filter Chips
                    item {
                        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                            Text(
                                text = "Categorías de Aprendizaje",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                items(categories) { cat ->
                                    val isSelected = selectedCategoryFilter == cat
                                    FilterChip(
                                        selected = isSelected,
                                        onClick = { selectedCategoryFilter = cat },
                                        label = {
                                            Text(
                                                text = cat,
                                                fontSize = 12.sp,
                                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                            )
                                        },
                                        colors = FilterChipDefaults.filterChipColors(
                                            selectedContainerColor = BrandCoral,
                                            selectedLabelColor = Color.White
                                        ),
                                        modifier = Modifier.testTag("filter_video_category_$cat")
                                    )
                                }
                            }
                        }
                    }

                    // Video Lessons List
                    items(filteredLessons, key = { it.id }) { lesson ->
                        VideoLessonCardItem(
                            lesson = lesson,
                            onClick = {
                                viewModel.selectVideoLesson(lesson.id)
                                isPlayerOpen = true
                            }
                        )
                    }
                }
            } else {
                // ==========================================
                // 2. INTERACTIVE VIDEO PLAYER & LESSON STUDIO
                // ==========================================
                selectedLesson?.let { lesson ->
                    val maxDurationSeconds = lesson.durationMinutes * 60

                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(bottom = 32.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Interactive Video Stage / Screen Mockup
                        item {
                            Card(
                                shape = RoundedCornerShape(0.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.Black),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("video_player_stage")
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(230.dp)
                                ) {
                                    Image(
                                        painter = painterResource(id = lesson.bannerResId),
                                        contentDescription = lesson.titleEn,
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.fillMaxSize()
                                    )
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .background(Color.Black.copy(alpha = 0.45f))
                                    )

                                    // Center Play / Pause Pulsing Button
                                    Box(
                                        modifier = Modifier
                                            .align(Alignment.Center)
                                            .size(64.dp)
                                            .clip(CircleShape)
                                            .background(BrandCoral.copy(alpha = 0.85f))
                                            .clickable { viewModel.setVideoPlaying(!isPlaying) },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                            contentDescription = if (isPlaying) "Pausar" else "Reproducir",
                                            tint = Color.White,
                                            modifier = Modifier.size(36.dp)
                                        )
                                    }

                                    // Top Video Info Overlay
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .align(Alignment.TopStart)
                                            .padding(12.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Surface(
                                            shape = RoundedCornerShape(6.dp),
                                            color = Color.Black.copy(alpha = 0.7f)
                                        ) {
                                            Text(
                                                text = "🔴 HD Masterclass • ${lesson.level}",
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color.White,
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                            )
                                        }

                                        Surface(
                                            shape = RoundedCornerShape(6.dp),
                                            color = Color.Black.copy(alpha = 0.7f)
                                        ) {
                                            Text(
                                                text = if (isSpanishSubtitles) "CC: EN + ES" else "CC: EN ONLY",
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = BrandCoral,
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                            )
                                        }
                                    }

                                    // Bottom Timeline Slider & Time readout
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .align(Alignment.BottomStart)
                                            .background(
                                                Brush.verticalGradient(
                                                    colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.9f))
                                                )
                                            )
                                            .padding(horizontal = 14.dp, vertical = 8.dp)
                                    ) {
                                        Slider(
                                            value = currentTimeSeconds.toFloat(),
                                            onValueChange = { viewModel.setVideoTimeSeconds(it.toInt()) },
                                            valueRange = 0f..maxDurationSeconds.toFloat(),
                                            colors = SliderDefaults.colors(
                                                thumbColor = BrandCoral,
                                                activeTrackColor = BrandCoral,
                                                inactiveTrackColor = Color.White.copy(alpha = 0.3f)
                                            ),
                                            modifier = Modifier.height(24.dp)
                                        )

                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            val curMin = currentTimeSeconds / 60
                                            val curSec = currentTimeSeconds % 60
                                            val totMin = maxDurationSeconds / 60
                                            val totSec = maxDurationSeconds % 60
                                            Text(
                                                text = "%02d:%02d / %02d:%02d".format(curMin, curSec, totMin, totSec),
                                                fontSize = 11.sp,
                                                color = Color.White,
                                                fontWeight = FontWeight.Bold
                                            )
                                            Text(
                                                text = currentMarker?.titleEn ?: lesson.titleEn,
                                                fontSize = 11.sp,
                                                color = Color.White.copy(alpha = 0.8f),
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        // Live Subtitle & Transcript Card
                        currentMarker?.let { marker ->
                            item {
                                Card(
                                    shape = RoundedCornerShape(16.dp),
                                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 16.dp)
                                        .testTag("video_live_transcript_card")
                                ) {
                                    Column(
                                        modifier = Modifier.padding(16.dp),
                                        verticalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text(
                                                text = "🎙️ Transcripción en Vivo",
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 13.sp,
                                                color = BrandCoral
                                            )
                                            AudioSpeakButton(
                                                text = marker.transcriptEn,
                                                isSpanish = false,
                                                onSpeak = onSpeak,
                                                modifier = Modifier.size(30.dp)
                                            )
                                        }

                                        Text(
                                            text = marker.transcriptEn,
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = MaterialTheme.colorScheme.onSurface,
                                            lineHeight = 20.sp
                                        )

                                        if (isSpanishSubtitles) {
                                            Text(
                                                text = marker.transcriptEs,
                                                fontSize = 13.sp,
                                                fontStyle = FontStyle.Italic,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                                lineHeight = 18.sp
                                            )
                                        }

                                        // Grammar Pointers
                                        Surface(
                                            shape = RoundedCornerShape(10.dp),
                                            color = BrandBlueLight.copy(alpha = 0.4f),
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Column(modifier = Modifier.padding(10.dp)) {
                                                Text(
                                                    text = "💡 Regla Clave del Examinador:",
                                                    fontWeight = FontWeight.Bold,
                                                    fontSize = 11.sp,
                                                    color = BrandBlueDark
                                                )
                                                Text(
                                                    text = marker.grammarRule,
                                                    fontSize = 12.sp,
                                                    color = MaterialTheme.colorScheme.onSurface
                                                )
                                                Spacer(modifier = Modifier.height(4.dp))
                                                Text(
                                                    text = "Frase modelo: ${marker.keyExpression}",
                                                    fontSize = 12.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = BrandCoralDark
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        // Chapters Timeline Jumper
                        item {
                            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                                Text(
                                    text = "Capítulos de la Videoclase",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = MaterialTheme.colorScheme.onBackground
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }

                        itemsIndexed(lesson.timeline) { index, marker ->
                            val isCurrent = (currentMarker?.timeSeconds == marker.timeSeconds)
                            Card(
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (isCurrent) BrandCoralLight.copy(alpha = 0.35f) else MaterialTheme.colorScheme.surface
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp)
                                    .clickable {
                                        viewModel.setVideoTimeSeconds(marker.timeSeconds)
                                    }
                                    .testTag("video_chapter_$index")
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    Surface(
                                        shape = CircleShape,
                                        color = if (isCurrent) BrandCoral else MaterialTheme.colorScheme.surfaceVariant,
                                        modifier = Modifier.size(30.dp)
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Text(
                                                text = "%02d:%02d".format(marker.timeSeconds / 60, marker.timeSeconds % 60),
                                                fontSize = 9.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = if (isCurrent) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }
                                    }

                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = marker.titleEn,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 13.sp,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                        Text(
                                            text = marker.titleEs,
                                            fontSize = 11.sp,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }

                                    Icon(
                                        Icons.Default.PlayCircleOutline,
                                        contentDescription = null,
                                        tint = if (isCurrent) BrandCoral else MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }

                        // Key Takeaways & Exam Strategy Notes
                        item {
                            Card(
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp)
                            ) {
                                Column(
                                    modifier = Modifier.padding(16.dp),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Text(
                                        text = "📌 Conclusiones Clave de la Clase",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        color = BrandBlue
                                    )
                                    lesson.keyTakeaways.forEach { takeaway ->
                                        Row(
                                            verticalAlignment = Alignment.Top,
                                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            Text(text = "✓", color = SuccessGreen, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                            Text(
                                                text = takeaway,
                                                fontSize = 12.sp,
                                                color = MaterialTheme.colorScheme.onSurface,
                                                lineHeight = 18.sp
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun VideoLessonCardItem(
    lesson: VideoLessonItem,
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
            .testTag("video_lesson_card_${lesson.id}")
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Thumbnail Image with Overlay
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
            ) {
                Image(
                    painter = painterResource(id = lesson.bannerResId),
                    contentDescription = lesson.titleEn,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.7f))
                            )
                        )
                )
                // Center Play Icon
                Surface(
                    shape = CircleShape,
                    color = BrandCoral.copy(alpha = 0.9f),
                    modifier = Modifier
                        .size(42.dp)
                        .align(Alignment.Center)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(Icons.Default.PlayArrow, contentDescription = null, tint = Color.White, modifier = Modifier.size(24.dp))
                    }
                }

                // Level Tag
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = BrandBlue,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(8.dp)
                ) {
                    Text(
                        text = "${lesson.category} • ${lesson.level}",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }

                // Duration Tag
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = Color.Black.copy(alpha = 0.75f),
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(8.dp)
                ) {
                    Text(
                        text = "${lesson.durationMinutes}:00 min",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            // Description
            Column(
                modifier = Modifier.padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = lesson.titleEn,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = lesson.titleEs,
                    fontSize = 12.sp,
                    color = BrandCoralDark,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "Instructor: ${lesson.instructor}",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
