package com.example.ui.screens.learn

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.ScreenTopBar
import com.example.ui.theme.*
import com.example.ui.viewmodels.LearnSubScreen
import com.example.ui.viewmodels.MainViewModel
import com.example.ui.viewmodels.PomodoroMode
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PomodoroScreen(
    viewModel: MainViewModel
) {
    val allTopics by viewModel.allTopics.collectAsState()
    val pomodoroMode by viewModel.pomodoroMode.collectAsState()
    val isRunning by viewModel.isPomodoroRunning.collectAsState()
    val totalSeconds by viewModel.pomodoroTotalSeconds.collectAsState()
    val remainingSeconds by viewModel.pomodoroRemainingSeconds.collectAsState()
    val completedSessions by viewModel.pomodoroCompletedSessions.collectAsState()
    val linkedTopicId by viewModel.pomodoroLinkedTopicId.collectAsState()
    val linkedTopic by viewModel.pomodoroLinkedTopic.collectAsState()

    var showTopicDropdown by remember { mutableStateOf(false) }
    var customMinutesInput by remember { mutableStateOf("30") }
    var showCustomDialog by remember { mutableStateOf(false) }

    val minutes = remainingSeconds / 60
    val seconds = remainingSeconds % 60
    val timeFormatted = String.format(Locale.US, "%02d:%02d", minutes, seconds)

    val progress = if (totalSeconds > 0) (remainingSeconds.toFloat() / totalSeconds).coerceIn(0f, 1f) else 0f
    val animatedProgress by animateFloatAsState(targetValue = progress, label = "pomodoro_progress")

    Scaffold(
        topBar = {
            ScreenTopBar(
                title = "Temporizador Pomodoro",
                subtitle = if (isRunning) "⏱️ Enfoque activo en progreso" else "Sesiones de estudio y descansos",
                onBack = { viewModel.navigateToLearnSubScreen(LearnSubScreen.TOPICS_LIST) }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Mode Selector Chips
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    item {
                        FilterChip(
                            selected = pomodoroMode == PomodoroMode.STUDY_25,
                            onClick = { viewModel.setPomodoroMode(PomodoroMode.STUDY_25) },
                            label = { Text("🍅 Estudio (25 min)", fontSize = 12.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = BrandBlue,
                                selectedLabelColor = Color.White
                            ),
                            modifier = Modifier.testTag("pomodoro_mode_study25")
                        )
                    }
                    item {
                        FilterChip(
                            selected = pomodoroMode == PomodoroMode.SHORT_BREAK_5,
                            onClick = { viewModel.setPomodoroMode(PomodoroMode.SHORT_BREAK_5) },
                            label = { Text("☕ Descanso (5 min)", fontSize = 12.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = SuccessGreen,
                                selectedLabelColor = Color.White
                            ),
                            modifier = Modifier.testTag("pomodoro_mode_break5")
                        )
                    }
                    item {
                        FilterChip(
                            selected = pomodoroMode == PomodoroMode.LONG_BREAK_15,
                            onClick = { viewModel.setPomodoroMode(PomodoroMode.LONG_BREAK_15) },
                            label = { Text("🌿 Descanso Largo (15 min)", fontSize = 12.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = Color(0xFF0D9488),
                                selectedLabelColor = Color.White
                            ),
                            modifier = Modifier.testTag("pomodoro_mode_break15")
                        )
                    }
                    item {
                        FilterChip(
                            selected = pomodoroMode == PomodoroMode.CUSTOM,
                            onClick = { showCustomDialog = true },
                            label = { Text("⚙️ Personalizado (${totalSeconds / 60}m)", fontSize = 12.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = Color(0xFF8B5CF6),
                                selectedLabelColor = Color.White
                            ),
                            modifier = Modifier.testTag("pomodoro_mode_custom")
                        )
                    }
                }

                // Linked Cambridge Topic Selector Card
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showTopicDropdown = !showTopicDropdown }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.MenuBook,
                                contentDescription = null,
                                tint = BrandBlue,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = "Tema de estudio vinculado:",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = linkedTopic?.title ?: "Estudio General de Inglés",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                        Icon(Icons.Default.ArrowDropDown, contentDescription = "Cambiar tema")
                    }
                }

                DropdownMenu(
                    expanded = showTopicDropdown,
                    onDismissRequest = { showTopicDropdown = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Estudio General de Inglés") },
                        onClick = {
                            viewModel.setPomodoroLinkedTopic(null)
                            showTopicDropdown = false
                        }
                    )
                    allTopics.take(15).forEach { topic ->
                        DropdownMenuItem(
                            text = { Text("${topic.difficulty}: ${topic.title}") },
                            onClick = {
                                viewModel.setPomodoroLinkedTopic(topic.id)
                                showTopicDropdown = false
                            }
                        )
                    }
                }
            }

            // Big Circular Digital Display
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(260.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            colors = if (pomodoroMode.isBreak) listOf(SuccessGreen.copy(alpha = 0.15f), Color.Transparent)
                            else listOf(BrandBlue.copy(alpha = 0.15f), Color.Transparent)
                        )
                    )
            ) {
                CircularProgressIndicator(
                    progress = { animatedProgress },
                    modifier = Modifier.size(240.dp),
                    strokeWidth = 12.dp,
                    color = if (pomodoroMode.isBreak) SuccessGreen else BrandBlue,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant
                )

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = if (pomodoroMode.isBreak) "DESCANSO" else "ENFOQUE",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.sp,
                        color = if (pomodoroMode.isBreak) SuccessGreen else BrandBlue
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = timeFormatted,
                        fontSize = 52.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.testTag("pomodoro_screen_time_text")
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = if (isRunning) "En curso..." else "En pausa",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Status info & Streak Protector
            Surface(
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Completados hoy", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text("$completedSessions sesiones", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = BrandBlue)
                    }
                    Divider(modifier = Modifier.height(24.dp).width(1.dp))
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Persistencia", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text("ViewModel Activo", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = SuccessGreen)
                    }
                }
            }

            // Controls: Start/Pause, Reset, Skip
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Reset Button
                IconButton(
                    onClick = { viewModel.resetPomodoro() },
                    modifier = Modifier
                        .size(54.dp)
                        .background(MaterialTheme.colorScheme.surfaceVariant, CircleShape)
                        .testTag("pomodoro_screen_reset_btn")
                ) {
                    Icon(
                        imageVector = Icons.Default.Replay,
                        contentDescription = "Reiniciar temporizador",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                // Main Play/Pause Button
                Button(
                    onClick = { viewModel.togglePomodoro() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (pomodoroMode.isBreak) SuccessGreen else BrandBlue
                    ),
                    shape = CircleShape,
                    modifier = Modifier
                        .size(76.dp)
                        .testTag("pomodoro_screen_toggle_btn"),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Icon(
                        imageVector = if (isRunning) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = if (isRunning) "Pausar" else "Iniciar",
                        modifier = Modifier.size(38.dp),
                        tint = Color.White
                    )
                }

                // Skip / Switch to break or study
                IconButton(
                    onClick = {
                        if (pomodoroMode.isBreak) {
                            viewModel.setPomodoroMode(PomodoroMode.STUDY_25)
                        } else {
                            viewModel.setPomodoroMode(PomodoroMode.SHORT_BREAK_5)
                        }
                    },
                    modifier = Modifier
                        .size(54.dp)
                        .background(MaterialTheme.colorScheme.surfaceVariant, CircleShape)
                        .testTag("pomodoro_screen_skip_btn")
                ) {
                    Icon(
                        imageVector = Icons.Default.SkipNext,
                        contentDescription = "Cambiar modo",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }

    // Custom Minutes Dialog
    if (showCustomDialog) {
        AlertDialog(
            onDismissRequest = { showCustomDialog = false },
            title = { Text("Configurar minutos personalizados", fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    Text("Ingresa los minutos deseados para tu sesión de estudio (1 a 180 min):", fontSize = 13.sp)
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = customMinutesInput,
                        onValueChange = { customMinutesInput = it.filter { ch -> ch.isDigit() } },
                        label = { Text("Minutos") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val mins = customMinutesInput.toIntOrNull() ?: 25
                        viewModel.setCustomPomodoroMinutes(mins)
                        showCustomDialog = false
                    }
                ) {
                    Text("Guardar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showCustomDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}
