package com.example.ui.screens.stats

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.CategoryIcon
import com.example.ui.theme.*
import com.example.ui.viewmodels.MainViewModel
import com.example.ui.viewmodels.StatsChartTab
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun StatsScreen(
    viewModel: MainViewModel
) {
    val allTopics by viewModel.allTopics.collectAsState()
    val userSettings by viewModel.userSettings.collectAsState()
    val totalSeconds by viewModel.totalStudySeconds.collectAsState()
    val allVocab by viewModel.allVocabItems.collectAsState()
    val dailyPomodoroStats by viewModel.dailyPomodoroStats.collectAsState()
    val dailyAccuracyStats by viewModel.dailyAccuracyStats.collectAsState()
    val categoryAccuracyStats by viewModel.categoryAccuracyStats.collectAsState()
    val selectedDaysRange by viewModel.statsDaysRange.collectAsState()
    val selectedChartTab by viewModel.statsChartTab.collectAsState()
    val allStudySessions by viewModel.allStudySessions.collectAsState()
    val allExerciseAttempts by viewModel.allExerciseAttemptsList.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    val totalTopics = allTopics.size
    val completedCount = allTopics.count { it.status == "COMPLETED" || it.status == "MASTERED" }
    val masteredCount = allTopics.count { it.status == "MASTERED" }
    val inProgressCount = allTopics.count { it.status == "IN_PROGRESS" }
    val notStartedCount = allTopics.count { it.status == "NOT_STARTED" }

    val globalPercent = if (totalTopics > 0) (completedCount.toFloat() / totalTopics * 100).toInt() else 0
    val studyHours = totalSeconds / 3600
    val studyMinutes = (totalSeconds % 3600) / 60
    val dailyGoal = userSettings?.dailyGoalMinutes ?: 25

    var showGoalDialog by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp, vertical = 10.dp)
            .testTag("screen_stats_progress"),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        contentPadding = PaddingValues(bottom = 88.dp)
    ) {
        // Header & Time Range Filter Selector
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Progreso & Rendimiento",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = "Analítica de Pomodoro y Aciertos Cambridge",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                // Range Selector Pills (7D / 14D / 30D)
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
                ) {
                    Row(
                        modifier = Modifier.padding(4.dp),
                        horizontalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        listOf(7 to "7D", 14 to "14D", 30 to "30D").forEach { (days, label) ->
                            val isSelected = (selectedDaysRange == days)
                            Surface(
                                shape = RoundedCornerShape(16.dp),
                                color = if (isSelected) BrandBlue else Color.Transparent,
                                modifier = Modifier
                                    .clickable { viewModel.setStatsDaysRange(days) }
                                    .testTag("filter_range_${days}d")
                            ) {
                                Text(
                                    text = label,
                                    fontSize = 11.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        // Segmented Visual Tab Switcher (Pomodoro vs Aciertos vs Destrezas vs Resumen)
        item {
            ScrollableTabRow(
                selectedTabIndex = selectedChartTab.ordinal,
                edgePadding = 0.dp,
                containerColor = Color.Transparent,
                divider = {},
                indicator = { tabPositions ->
                    if (selectedChartTab.ordinal in tabPositions.indices) {
                        TabRowDefaults.SecondaryIndicator(
                            Modifier.tabIndicatorOffset(tabPositions[selectedChartTab.ordinal]),
                            color = BrandBlue,
                            height = 3.dp
                        )
                    }
                }
            ) {
                StatsChartTab.values().forEach { tab ->
                    Tab(
                        selected = selectedChartTab == tab,
                        onClick = { viewModel.setStatsChartTab(tab) },
                        text = {
                            Text(
                                text = tab.title,
                                fontSize = 12.sp,
                                fontWeight = if (selectedChartTab == tab) FontWeight.Bold else FontWeight.Medium
                            )
                        }
                    )
                }
            }
        }

        // Primary Dynamic Visual Chart Display
        item {
            AnimatedContent(
                targetState = selectedChartTab,
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                label = "chart_tab_content"
            ) { tab ->
                when (tab) {
                    StatsChartTab.POMODORO_STUDY -> {
                        PomodoroStudyTimeChart(
                            dailyStats = dailyPomodoroStats,
                            dailyGoalMinutes = dailyGoal,
                            onAdjustGoalClick = { showGoalDialog = true }
                        )
                    }
                    StatsChartTab.ACCURACY_RATE -> {
                        ExerciseAccuracyRateChart(
                            accuracyStats = dailyAccuracyStats
                        )
                    }
                    StatsChartTab.SKILLS_DISTRIBUTION -> {
                        SkillsAccuracyBreakdownCard(
                            categoryStats = categoryAccuracyStats
                        )
                    }
                }
            }
        }

        // Global Overview Quick Metrics Row
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Time Card
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = MaterialTheme.colorScheme.surface,
                    modifier = Modifier.weight(1f)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(text = "⏱️ Tiempo", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "${studyHours}h ${studyMinutes}m",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                // Streak Card
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = MaterialTheme.colorScheme.surface,
                    modifier = Modifier.weight(1f)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(text = "🔥 Racha", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "${userSettings?.streakDays ?: 1} días",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = WarningAmber
                        )
                    }
                }

                // XP Card
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = MaterialTheme.colorScheme.surface,
                    modifier = Modifier.weight(1f)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(text = "⚡ XP Total", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "${userSettings?.totalXp ?: 340}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = BrandBlue
                        )
                    }
                }

                // SRS Card
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = MaterialTheme.colorScheme.surface,
                    modifier = Modifier.weight(1f)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(text = "🗂️ Vocab SRS", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "${allVocab.size} cards",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = BrandCoral
                        )
                    }
                }
            }
        }

        // Global Readiness Hero Card
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Índice de Dominio Global B2",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(14.dp))

                    Box(
                        modifier = Modifier.size(120.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            progress = { globalPercent / 100f },
                            modifier = Modifier.fillMaxSize(),
                            strokeWidth = 10.dp,
                            color = BrandBlue,
                            trackColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "$globalPercent%",
                                fontSize = 30.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = BrandBlue
                            )
                            Text(
                                text = "CEFR B2",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "$completedCount de $totalTopics temas completados",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }

        // Shortcut to Mi Perfil & Insignias
        item {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = BrandGold.copy(alpha = 0.12f),
                border = androidx.compose.foundation.BorderStroke(1.dp, BrandGold.copy(alpha = 0.35f)),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { viewModel.setTab(com.example.ui.viewmodels.MainTab.PROFILE) }
                    .testTag("stats_to_profile_banner")
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(text = "🏆", fontSize = 24.sp)
                        Column {
                            Text(
                                text = "Mi Perfil & Insignias Desbloqueables",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "Revisa tu nivel de estudiante, metas y recordatorios push",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                    Icon(Icons.Default.ChevronRight, contentDescription = null, tint = BrandGold)
                }
            }
        }

        // Recent Activity History (Pomodoro Sessions & Exercises Completed)
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Historial Reciente de Pomodoros",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "${allStudySessions.size} registros",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    val recentSessions = allStudySessions.take(5)
                    val timeFmt = SimpleDateFormat("dd MMM, HH:mm", Locale("es", "ES"))

                    if (recentSessions.isEmpty()) {
                        Text(
                            text = "Aún no hay sesiones Pomodoro registradas.",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    } else {
                        recentSessions.forEach { session ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(28.dp)
                                            .background(BrandBlue.copy(alpha = 0.15f), CircleShape),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(if (session.mode == "POMODORO") "🍅" else "⏱️", fontSize = 12.sp)
                                    }
                                    Column {
                                        Text(
                                            text = session.topicTitle ?: "Sesión de Estudio Libre",
                                            fontWeight = FontWeight.SemiBold,
                                            fontSize = 12.sp,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                        Text(
                                            text = "${timeFmt.format(Date(session.timestamp))} · ${session.category}",
                                            fontSize = 10.sp,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }

                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = BrandBlue.copy(alpha = 0.12f)
                                ) {
                                    Text(
                                        text = "${session.durationSeconds / 60} min",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = BrandBlue,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Goal Adjustment Button
        item {
            OutlinedButton(
                onClick = { showGoalDialog = true },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.Tune, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Ajustar Meta Diaria de Pomodoro (${dailyGoal} min/día)", fontSize = 13.sp)
            }
        }
    }

    if (showGoalDialog) {
        var goalInput by remember { mutableStateOf(dailyGoal.toString()) }
        AlertDialog(
            onDismissRequest = { showGoalDialog = false },
            title = { Text("Configurar Meta Diaria de Pomodoro") },
            text = {
                OutlinedTextField(
                    value = goalInput,
                    onValueChange = { goalInput = it },
                    label = { Text("Minutos diarios objetivo") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        val mins = goalInput.toIntOrNull() ?: 25
                        coroutineScope.launch {
                            viewModel.studyRepo.updateGoalMinutes(mins)
                            showGoalDialog = false
                        }
                    }
                ) {
                    Text("Guardar Meta")
                }
            },
            dismissButton = {
                TextButton(onClick = { showGoalDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}
