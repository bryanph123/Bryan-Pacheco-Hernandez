package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun AppHeader(
    streakDays: Int = 3,
    xp: Int = 340,
    dailyMinutesStudied: Int = 18,
    dailyMinutesGoal: Int = 25,
    completedExercises: Int = 0,
    dailyExerciseGoal: Int = 5,
    isOnline: Boolean = true,
    networkType: String = "Wi-Fi",
    onAlarmsClick: () -> Unit = {},
    onNetworkClick: () -> Unit = {}
) {
    Surface(
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 2.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Title and B2 Badge
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(BrandBlue),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "B2",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                    }
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text(
                                text = "Inglés B2",
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            // Live Network Dot Pill
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = if (isOnline) SuccessGreenLight else MaterialTheme.colorScheme.errorContainer,
                                modifier = Modifier.clickable { onNetworkClick() }.testTag("network_status_badge")
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(6.dp)
                                            .clip(CircleShape)
                                            .background(if (isOnline) SuccessGreen else MaterialTheme.colorScheme.error)
                                    )
                                    Text(
                                        text = if (isOnline) "En línea" else "Offline",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isOnline) SuccessGreen else MaterialTheme.colorScheme.error
                                    )
                                }
                            }
                        }
                        Text(
                            text = "Dominio Profesional & Certificación",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                // Streak, XP & Alarms
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    // Streak Pill
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = WarningAmberLight,
                        modifier = Modifier.testTag("streak_badge")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(text = "🔥", fontSize = 13.sp)
                            Text(
                                text = "$streakDays d",
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                color = WarningAmber
                            )
                        }
                    }

                    // XP Pill
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = BrandBlueLight,
                        modifier = Modifier.testTag("xp_badge")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(text = "⚡", fontSize = 13.sp)
                            Text(
                                text = "$xp XP",
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                color = BrandBlue
                            )
                        }
                    }

                    // Notification / Alarms button
                    IconButton(
                        onClick = onAlarmsClick,
                        modifier = Modifier
                            .size(36.dp)
                            .testTag("alarms_header_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = "Alertas y Notificaciones de Estudio",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Animated Exercise Progress Bar (Material 3)
            val exerciseTargetProgress = (completedExercises.toFloat() / dailyExerciseGoal.coerceAtLeast(1)).coerceIn(0f, 1f)
            val animatedExerciseProgress by animateFloatAsState(
                targetValue = exerciseTargetProgress,
                animationSpec = tween(durationMillis = 850, easing = FastOutSlowInEasing),
                label = "HeaderExerciseProgressAnimation"
            )
            val animatedExerciseBarColor by animateColorAsState(
                targetValue = if (exerciseTargetProgress >= 1f) SuccessGreen else BrandBlue,
                animationSpec = tween(durationMillis = 600),
                label = "HeaderExerciseColorAnimation"
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("header_exercise_progress_row"),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = if (completedExercises >= dailyExerciseGoal) Icons.Default.CheckCircle else Icons.Default.AssignmentTurnedIn,
                        contentDescription = null,
                        tint = animatedExerciseBarColor,
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = "Ejercicios: $completedExercises/$dailyExerciseGoal",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                LinearProgressIndicator(
                    progress = { animatedExerciseProgress },
                    modifier = Modifier
                        .weight(1f)
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp))
                        .testTag("header_exercise_progress_indicator"),
                    color = animatedExerciseBarColor,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant
                )

                Text(
                    text = "${(animatedExerciseProgress * 100).toInt()}%",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = animatedExerciseBarColor
                )
            }
        }
    }
}

/**
 * Reusable animated Material 3 progress bar for exercises completed with smooth physics animations
 */
@Composable
fun AnimatedExerciseProgressBar(
    completedCount: Int,
    totalCount: Int,
    label: String = "Progreso de Ejercicios",
    modifier: Modifier = Modifier,
    indicatorColor: Color = BrandBlue,
    showPercent: Boolean = true
) {
    val targetProgress = if (totalCount > 0) (completedCount.toFloat() / totalCount).coerceIn(0f, 1f) else 0f
    val animatedProgress by animateFloatAsState(
        targetValue = targetProgress,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "AnimatedExerciseProgressBarProgress"
    )

    val activeColor by animateColorAsState(
        targetValue = if (targetProgress >= 1f) SuccessGreen else indicatorColor,
        animationSpec = tween(durationMillis = 500),
        label = "AnimatedExerciseProgressBarColor"
    )

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    imageVector = if (targetProgress >= 1f) Icons.Default.CheckCircle else Icons.Default.FitnessCenter,
                    contentDescription = null,
                    tint = activeColor,
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = label,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = "$completedCount / $totalCount",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                if (showPercent) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = activeColor.copy(alpha = 0.15f)
                    ) {
                        Text(
                            text = "${(animatedProgress * 100).toInt()}%",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = activeColor,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
            }
        }

        LinearProgressIndicator(
            progress = { animatedProgress },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp))
                .testTag("animated_exercise_progress_indicator"),
            color = activeColor,
            trackColor = MaterialTheme.colorScheme.surfaceVariant
        )
    }
}

/**
 * Dedicated Material 3 Hero Card that shows dynamically updated animated progress for exercises
 */
@Composable
fun AnimatedExerciseProgressCard(
    todayExercises: Int,
    dailyGoal: Int,
    levelCompletedCount: Int,
    levelTotalCount: Int,
    selectedLevel: String,
    totalExercisesAllTime: Int,
    onPracticeClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var selectedProgressMode by remember { mutableStateOf(0) } // 0: Meta Diaria, 1: Nivel Actual

    val currentCompleted = if (selectedProgressMode == 0) todayExercises else levelCompletedCount
    val currentTotal = if (selectedProgressMode == 0) dailyGoal else levelTotalCount
    val targetProgress = if (currentTotal > 0) (currentCompleted.toFloat() / currentTotal).coerceIn(0f, 1f) else 0f

    val animatedProgress by animateFloatAsState(
        targetValue = targetProgress,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioLowBouncy,
            stiffness = Spring.StiffnessMediumLow
        ),
        label = "ExerciseHeroCardProgress"
    )

    val progressColor by animateColorAsState(
        targetValue = when {
            targetProgress >= 1f -> SuccessGreen
            targetProgress >= 0.5f -> BrandBlue
            else -> BrandCoral
        },
        animationSpec = tween(durationMillis = 600),
        label = "ExerciseHeroCardColor"
    )

    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier
            .fillMaxWidth()
            .testTag("animated_exercise_progress_card")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header with title and mode toggle chips
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Surface(
                        shape = CircleShape,
                        color = progressColor.copy(alpha = 0.15f),
                        modifier = Modifier.size(36.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = if (targetProgress >= 1f) Icons.Default.EmojiEvents else Icons.Default.AssignmentTurnedIn,
                                contentDescription = null,
                                tint = progressColor,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                    Column {
                        Text(
                            text = "Progreso de Ejercicios",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = if (targetProgress >= 1f) "¡Meta completada con éxito! 🎉" else if (targetProgress >= 0.5f) "¡Vas por buen camino, continúa!" else "Completa ejercicios para ganar XP",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                // Mode switcher chips
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    FilterChip(
                        selected = selectedProgressMode == 0,
                        onClick = { selectedProgressMode = 0 },
                        label = { Text("Hoy", fontSize = 11.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = BrandBlue,
                            selectedLabelColor = Color.White
                        ),
                        modifier = Modifier.testTag("mode_chip_today")
                    )
                    FilterChip(
                        selected = selectedProgressMode == 1,
                        onClick = { selectedProgressMode = 1 },
                        label = { Text(if (selectedLevel == "TODOS") "Nivel" else selectedLevel, fontSize = 11.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = BrandBlue,
                            selectedLabelColor = Color.White
                        ),
                        modifier = Modifier.testTag("mode_chip_level")
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Large Animated Progress Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    text = if (selectedProgressMode == 0) "Ejercicios completados hoy" else "Temas del nivel $selectedLevel",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = "$currentCompleted",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = progressColor
                    )
                    Text(
                        text = "/ $currentTotal",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "(${(animatedProgress * 100).toInt()}%)",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = progressColor
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Material 3 LinearProgressIndicator with dynamic animated value
            LinearProgressIndicator(
                progress = { animatedProgress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
                    .clip(RoundedCornerShape(5.dp))
                    .testTag("card_animated_exercise_progress_bar"),
                color = progressColor,
                trackColor = MaterialTheme.colorScheme.surfaceVariant
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Quick Stats Row & Action Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(text = "📊", fontSize = 11.sp)
                            Text(
                                text = "$totalExercisesAllTime intentos totales",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                FilledTonalButton(
                    onClick = onPracticeClick,
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                    modifier = Modifier
                        .height(34.dp)
                        .testTag("btn_practice_exercises_now")
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "Practicar", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun StatusBadge(
    status: String,
    modifier: Modifier = Modifier
) {
    val (bgColor, textColor, label) = when (status) {
        "MASTERED" -> Triple(SuccessGreenLight, SuccessGreen, "Dominado 🌟")
        "COMPLETED" -> Triple(BrandBlueLight, BrandBlue, "Completado ✓")
        "IN_PROGRESS" -> Triple(WarningAmberLight, WarningAmber, "En progreso ⏳")
        else -> Triple(MaterialTheme.colorScheme.surfaceVariant, MaterialTheme.colorScheme.onSurfaceVariant, "No iniciado")
    }

    Surface(
        shape = RoundedCornerShape(12.dp),
        color = bgColor,
        modifier = modifier
    ) {
        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            color = textColor,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
        )
    }
}

@Composable
fun AudioSpeakButton(
    text: String,
    isSpanish: Boolean = false,
    onSpeak: (String, Boolean) -> Unit,
    modifier: Modifier = Modifier,
    size: Int = 40,
    accentColor: Color = BrandBlue,
    containerColor: Color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.35f)
) {
    Surface(
        shape = CircleShape,
        color = containerColor,
        modifier = modifier
            .size(size.coerceAtLeast(44).dp)
            .testTag("audio_speak_container_${text.take(12)}")
    ) {
        IconButton(
            onClick = { onSpeak(text, isSpanish) },
            modifier = Modifier
                .fillMaxSize()
                .testTag("audio_speak_btn_${text.take(12)}")
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                contentDescription = "Escuchar pronunciación nativa de $text",
                tint = accentColor,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
fun ScreenTopBar(
    title: String,
    subtitle: String? = null,
    onBack: (() -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {}
) {
    Surface(
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 1.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (onBack != null) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier.testTag("back_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Regresar",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            } else {
                Spacer(modifier = Modifier.width(8.dp))
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp)
            ) {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onSurface
                )
                if (subtitle != null) {
                    Text(
                        text = subtitle,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                actions()
            }
        }
    }
}

@Composable
fun CategoryIcon(category: String, modifier: Modifier = Modifier) {
    val (icon, color) = when (category) {
        "Gramática" -> Pair(Icons.Default.MenuBook, ModuleLearnBlue)
        "Vocabulario" -> Pair(Icons.Default.AutoStories, BrandCoral)
        "Listening" -> Pair(Icons.Default.Headphones, Color(0xFF9C27B0))
        "Speaking" -> Pair(Icons.Default.RecordVoiceOver, Color(0xFFE91E63))
        "Reading" -> Pair(Icons.Default.FindInPage, Color(0xFF009688))
        "Writing" -> Pair(Icons.Default.EditNote, Color(0xFF3F51B5))
        "Pronunciación" -> Pair(Icons.Default.Mic, Color(0xFFFF9800))
        "Funciones Comunicativas" -> Pair(Icons.Default.Forum, Color(0xFF4CAF50))
        else -> Pair(Icons.Default.School, ModuleLearnBlue)
    }

    Box(
        modifier = modifier
            .size(36.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(color.copy(alpha = 0.12f)),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = category,
            tint = color,
            modifier = Modifier.size(20.dp)
        )
    }
}
