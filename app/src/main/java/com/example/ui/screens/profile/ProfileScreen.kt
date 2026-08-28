package com.example.ui.screens.profile

import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.entities.UserBadgeEntity
import com.example.ui.components.AudioSpeakButton
import com.example.ui.theme.*
import com.example.ui.viewmodels.MainViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ProfileScreen(
    viewModel: MainViewModel,
    onSpeak: (String, Boolean) -> Unit
) {
    val context = LocalContext.current
    val userSettings by viewModel.userSettings.collectAsState()
    val userLevel by viewModel.userLevelInfo.collectAsState()
    val allBadges by viewModel.allBadges.collectAsState()
    val unlockedBadgesCount by viewModel.unlockedBadgesCount.collectAsState()
    val totalSeconds by viewModel.totalStudySeconds.collectAsState()
    val allTopics by viewModel.allTopics.collectAsState()
    val allVocab by viewModel.allVocabItems.collectAsState()
    val attempts by viewModel.allExerciseAttemptsList.collectAsState()
    val cachedExercises by viewModel.cachedExercises.collectAsState()
    val cachedVocabBank by viewModel.cachedVocabBank.collectAsState()
    val allSessions by viewModel.allStudySessions.collectAsState()

    var selectedBadgeCategory by remember { mutableStateOf("TODAS") }
    var showTimePickerDialog by remember { mutableStateOf(false) }
    var isSyncingCache by remember { mutableStateOf(false) }

    val studyHours = totalSeconds / 3600
    val studyMinutes = (totalSeconds % 3600) / 60
    val streakDays = userSettings?.streakDays ?: 1
    val totalXp = userSettings?.totalXp ?: 340
    val completedTopics = allTopics.count { it.status == "COMPLETED" || it.status == "MASTERED" }
    val totalAccuracy = if (attempts.isNotEmpty()) {
        val totalScore = attempts.sumOf { it.score }
        val maxScore = attempts.sumOf { it.maxScore }
        if (maxScore > 0) (totalScore.toFloat() / maxScore * 100).toInt() else 85
    } else 85

    // Detect habitual study hour from study sessions
    val (habitualHour, habitualMinute) = remember(allSessions) {
        com.example.util.StudyReminderScheduler.detectHabitualStudyHour(allSessions)
    }

    val preferredHour = userSettings?.preferredStudyHour ?: habitualHour
    val preferredMinute = userSettings?.preferredStudyMinute ?: habitualMinute
    val remindersEnabled = userSettings?.dailyRemindersEnabled ?: true
    val currentAccent = userSettings?.ttsAccent ?: "UK"

    val filteredBadges = remember(allBadges, selectedBadgeCategory) {
        if (selectedBadgeCategory == "TODAS") {
            allBadges
        } else {
            allBadges.filter { it.category.equals(selectedBadgeCategory, ignoreCase = true) }
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .testTag("screen_my_profile"),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(bottom = 96.dp)
    ) {
        // 1. User Header & Level Showcase
        item {
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    BrandBlue.copy(alpha = 0.12f),
                                    Color.Transparent
                                )
                            )
                        )
                        .padding(20.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        // Level Icon & Badge Badge
                        Box(
                            modifier = Modifier
                                .size(76.dp)
                                .clip(CircleShape)
                                .background(
                                    Brush.linearGradient(
                                        colors = listOf(BrandBlue, BrandCoral)
                                    )
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = userLevel.iconEmoji,
                                fontSize = 34.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Level Title & Number
                        Text(
                            text = "Nivel ${userLevel.levelNumber} · ${userLevel.titleSpanish}",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 20.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = userLevel.title,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            color = BrandBlue
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        // XP Progress Bar
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "${userLevel.currentXp} XP acumulados",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "+${userLevel.xpNeededForNextLevel} XP para Nivel ${userLevel.levelNumber + 1}",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            LinearProgressIndicator(
                                progress = { userLevel.progressFraction },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(10.dp)
                                    .clip(RoundedCornerShape(5.dp)),
                                color = BrandBlue,
                                trackColor = MaterialTheme.colorScheme.surfaceVariant
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Level Perk Banner
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    Icons.Default.Verified,
                                    contentDescription = null,
                                    tint = BrandBlue,
                                    modifier = Modifier.size(18.dp)
                                )
                                Text(
                                    text = userLevel.perkDescription,
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    lineHeight = 15.sp
                                )
                            }
                        }
                    }
                }
            }
        }

        // 2. Global Achieved Statistics Grid
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "Estadísticas Logradas",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Card 1: Study Time
                    ProfileStatItem(
                        emoji = "⏱️",
                        title = "Tiempo Total",
                        value = "${studyHours}h ${studyMinutes}m",
                        color = BrandBlue,
                        modifier = Modifier.weight(1f)
                    )
                    // Card 2: Streak
                    ProfileStatItem(
                        emoji = "🔥",
                        title = "Racha Actual",
                        value = "$streakDays días",
                        color = WarningAmber,
                        modifier = Modifier.weight(1f)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Card 3: Exercises
                    ProfileStatItem(
                        emoji = "🎯",
                        title = "Aciertos Globales",
                        value = "$totalAccuracy%",
                        color = SuccessGreen,
                        modifier = Modifier.weight(1f)
                    )
                    // Card 4: SRS Vocab
                    ProfileStatItem(
                        emoji = "📚",
                        title = "Vocabulario SRS",
                        value = "${allVocab.size} cards",
                        color = BrandCoral,
                        modifier = Modifier.weight(1f)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Card 5: Temas
                    ProfileStatItem(
                        emoji = "📘",
                        title = "Temas Dominados",
                        value = "$completedTopics de ${allTopics.size}",
                        color = BrandBlue,
                        modifier = Modifier.weight(1f)
                    )
                    // Card 6: Insignias
                    ProfileStatItem(
                        emoji = "🏆",
                        title = "Insignias",
                        value = "$unlockedBadgesCount de ${allBadges.size}",
                        color = BrandGold,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // 3. User Badges & Level Goals Section
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Insignias & Metas de Estudio",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "$unlockedBadgesCount de ${allBadges.size} desbloqueadas",
                                fontSize = 12.sp,
                                color = BrandBlue
                            )
                        }

                        IconButton(
                            onClick = {
                                viewModel.evaluateBadgesNow()
                                Toast.makeText(context, "¡Insignias actualizadas con tu progreso!", Toast.LENGTH_SHORT).show()
                            }
                        ) {
                            Icon(Icons.Default.Refresh, contentDescription = "Actualizar insignias", tint = BrandBlue)
                        }
                    }

                    // Category Filter Chips
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        val categories = listOf("TODAS", "STREAK", "VOCABULARY", "EXERCISES", "POMODORO", "CAMBRIDGE", "LEVEL")
                        val labels = mapOf(
                            "TODAS" to "Todas",
                            "STREAK" to "🔥 Rachas",
                            "VOCABULARY" to "📚 Vocabulario",
                            "EXERCISES" to "🎯 Ejercicios",
                            "POMODORO" to "⏱️ Pomodoro",
                            "CAMBRIDGE" to "🌟 Cambridge",
                            "LEVEL" to "👑 Nivel"
                        )
                        items(categories) { cat ->
                            val isSelected = selectedBadgeCategory == cat
                            FilterChip(
                                selected = isSelected,
                                onClick = { selectedBadgeCategory = cat },
                                label = { Text(labels[cat] ?: cat, fontSize = 11.sp) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = BrandBlue.copy(alpha = 0.18f),
                                    selectedLabelColor = BrandBlue
                                )
                            )
                        }
                    }

                    // Badges List
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        filteredBadges.forEach { badge ->
                            BadgeCardItem(
                                badge = badge,
                                onClaimClick = {
                                    viewModel.claimBadgeReward(badge.badgeId) { xp ->
                                        Toast.makeText(context, "¡+$xp XP Reclamados!", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }

        // 4. Daily Reminder Configuration via WorkManager
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(CircleShape)
                                    .background(WarningAmber.copy(alpha = 0.15f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Default.NotificationsActive,
                                    contentDescription = null,
                                    tint = WarningAmber,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Column {
                                Text(
                                    text = "Recordatorios Diarios (WorkManager)",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "Horario habitual detectado: ${String.format("%02d:00 hrs", habitualHour)}",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        Switch(
                            checked = remindersEnabled,
                            onCheckedChange = { isEnabled ->
                                viewModel.scheduleCustomDailyPushReminder(
                                    context = context,
                                    hour = preferredHour,
                                    minute = preferredMinute,
                                    enabled = isEnabled
                                )
                            }
                        )
                    }

                    if (remindersEnabled) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = "Hora Programada de Notificación",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = String.format("%02d:%02d hrs (Diario)", preferredHour, preferredMinute),
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = BrandBlue
                                    )
                                }

                                OutlinedButton(
                                    onClick = { showTimePickerDialog = true },
                                    shape = RoundedCornerShape(10.dp),
                                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                                ) {
                                    Text("Cambiar", fontSize = 12.sp)
                                }
                            }
                        }
                    }

                    // Push Test Button
                    Button(
                        onClick = {
                            viewModel.triggerTestPushReminder(context)
                            Toast.makeText(context, "🔔 Notificación Push enviada vía WorkManager", Toast.LENGTH_SHORT).show()
                        },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            Icons.Default.Schedule,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Probar Notificación Push Inmediata",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            }
        }

        // 5. Audio & TextToSpeech Pronunciation Settings
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(BrandBlue.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.VolumeUp,
                                contentDescription = null,
                                tint = BrandBlue,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Column {
                            Text(
                                text = "Pronunciación & Voz (TextToSpeech)",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "Acento predeterminado para el vocabulario",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    // UK vs US Accent Selector
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf("UK" to "🇬🇧 Inglés Británico (en-GB)", "US" to "🇺🇸 Inglés Americano (en-US)").forEach { (accent, label) ->
                            val isSelected = currentAccent == accent
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = if (isSelected) BrandBlue.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                                border = if (isSelected) androidx.compose.foundation.BorderStroke(1.5.dp, BrandBlue) else null,
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { viewModel.setTtsAccent(accent) }
                            ) {
                                Row(
                                    modifier = Modifier.padding(10.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Text(
                                        text = label,
                                        fontSize = 11.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                        color = if (isSelected) BrandBlue else MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        }
                    }

                    // Test TTS playback button
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "Prueba de audio: 'Pronunciation'",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "Reproduce con motor Android TTS",
                                    fontSize = 10.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            AudioSpeakButton(
                                text = "Pronunciation is key to Cambridge English success.",
                                onSpeak = onSpeak
                            )
                        }
                    }
                }
            }
        }

        // 6. Room Local Database & Offline Cache Status
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(CircleShape)
                                    .background(SuccessGreen.copy(alpha = 0.15f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Default.CloudDone,
                                    contentDescription = null,
                                    tint = SuccessGreen,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Column {
                                Text(
                                    text = "Caché Local Offline (Room DB)",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "Toda la app 100% disponible sin conexión",
                                    fontSize = 11.sp,
                                    color = SuccessGreen
                                )
                            }
                        }

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = SuccessGreen.copy(alpha = 0.15f)
                        ) {
                            Text(
                                text = "ACTIVO",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = SuccessGreen,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                            )
                        }
                    }

                    // Cache statistics
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text(text = "Ejercicios Offline", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text(
                                    text = "${cachedExercises.size} precargados",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = BrandBlue
                                )
                            }
                        }

                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text(text = "Banco Vocabulario", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text(
                                    text = "${cachedVocabBank.size} términos",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = BrandCoral
                                )
                            }
                        }
                    }

                    // Sync button
                    OutlinedButton(
                        onClick = {
                            isSyncingCache = true
                            viewModel.refreshOfflineCache {
                                isSyncingCache = false
                                Toast.makeText(context, "✅ Caché Room sincronizada y verificada", Toast.LENGTH_SHORT).show()
                            }
                        },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        if (isSyncingCache) {
                            CircularProgressIndicator(modifier = Modifier.size(16.dp), strokeWidth = 2.dp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Sincronizando...", fontSize = 12.sp)
                        } else {
                            Icon(Icons.Default.Sync, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Sincronizar y Precargar Caché Local", fontSize = 12.sp)
                        }
                    }
                }
            }
        }
    }

    // Time Picker Dialog for Daily Reminder
    if (showTimePickerDialog) {
        var selectedHour by remember { mutableStateOf(preferredHour) }
        var selectedMinute by remember { mutableStateOf(preferredMinute) }

        AlertDialog(
            onDismissRequest = { showTimePickerDialog = false },
            title = { Text("Configurar Horario de Estudio", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    Text(
                        text = "Selecciona la hora para tu recordatorio push diario:",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    // Quick hour selection chips
                    val hours = listOf(8 to "08:00 (Mañana)", 14 to "14:00 (Mediodía)", 19 to "19:00 (Tarde)", 20 to "20:00 (Noche)", 21 to "21:30 (Noche)")
                    hours.forEach { (h, label) ->
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = if (selectedHour == h) BrandBlue.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                            border = if (selectedHour == h) androidx.compose.foundation.BorderStroke(1.dp, BrandBlue) else null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    selectedHour = h
                                    selectedMinute = if (h == 21) 30 else 0
                                }
                        ) {
                            Row(
                                modifier = Modifier.padding(10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(label, fontSize = 12.sp, fontWeight = if (selectedHour == h) FontWeight.Bold else FontWeight.Normal)
                                if (selectedHour == h) {
                                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = BrandBlue, modifier = Modifier.size(16.dp))
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.scheduleCustomDailyPushReminder(
                            context = context,
                            hour = selectedHour,
                            minute = selectedMinute,
                            enabled = true
                        )
                        showTimePickerDialog = false
                        Toast.makeText(context, "Recordatorio programado a las ${String.format("%02d:%02d", selectedHour, selectedMinute)}", Toast.LENGTH_SHORT).show()
                    }
                ) {
                    Text("Guardar Horario")
                }
            },
            dismissButton = {
                TextButton(onClick = { showTimePickerDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Composable
fun ProfileStatItem(
    emoji: String,
    title: String,
    value: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = MaterialTheme.colorScheme.surface,
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(text = "$emoji $title", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = color
            )
        }
    }
}

@Composable
fun BadgeCardItem(
    badge: UserBadgeEntity,
    onClaimClick: () -> Unit
) {
    val progressFraction = if (badge.targetGoal > 0) {
        (badge.currentProgress.toFloat() / badge.targetGoal.toFloat()).coerceIn(0f, 1f)
    } else 1f

    val tierBorderColor = when (badge.tier.uppercase()) {
        "DIAMOND" -> BrandBlue
        "PLATINUM" -> SuccessGreen
        "GOLD" -> BrandGold
        "SILVER" -> Color(0xFF9E9E9E)
        else -> Color(0xFFCD7F32)
    }

    Surface(
        shape = RoundedCornerShape(14.dp),
        color = if (badge.isUnlocked) MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.15f),
        border = if (badge.isUnlocked) androidx.compose.foundation.BorderStroke(1.5.dp, tierBorderColor.copy(alpha = 0.7f)) else null,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Badge Icon Circle
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(if (badge.isUnlocked) tierBorderColor.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = badge.iconEmoji,
                    fontSize = 22.sp
                )
            }

            // Details
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = badge.titleSpanish,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = if (badge.isUnlocked) SuccessGreen.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surfaceVariant
                    ) {
                        Text(
                            text = if (badge.isUnlocked) "¡DESBLOQUEADA!" else "${badge.currentProgress}/${badge.targetGoal}",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (badge.isUnlocked) SuccessGreen else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }

                Text(
                    text = badge.description,
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 14.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                // Progress Bar
                LinearProgressIndicator(
                    progress = { progressFraction },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = if (badge.isUnlocked) tierBorderColor else BrandBlue,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Recompensa: +${badge.xpReward} XP",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = BrandGold
                    )
                    Text(
                        text = "Nivel: ${badge.tier}",
                        fontSize = 9.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}
