package com.example.ui.screens.learn.exercises

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.local.model.ModularExerciseQuestion
import com.example.data.local.model.ModularExerciseType
import com.example.data.local.model.VisualIllustration
import com.example.data.srs.SrsEvaluationResult
import com.example.ui.components.AudioSpeakButton
import com.example.ui.theme.*
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

/**
 * Animated Mascot Companion with Floating Motion, Glowing Aura, and Dynamic Speech Bubble
 */
@Composable
fun AnimatedMascotCompanion(
    stateMessage: String,
    isCorrect: Boolean?,
    comboStreak: Int,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "MascotFloat")
    val floatOffset by infiniteTransition.animateFloat(
        initialValue = -5f,
        targetValue = 5f,
        animationSpec = infiniteRepeatable(
            animation = tween(1800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "FloatY"
    )

    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.25f,
        targetValue = 0.75f,
        animationSpec = infiniteRepeatable(
            animation = tween(1400, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "GlowPulse"
    )

    val bounceScale by animateFloatAsState(
        targetValue = when (isCorrect) {
            true -> 1.12f
            false -> 0.94f
            null -> 1f
        },
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "MascotReactionScale"
    )

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // Interactive 3D Owl Mascot with Floating Aura
        Box(
            modifier = Modifier
                .size(68.dp)
                .offset(y = floatOffset.dp)
                .scale(bounceScale)
                .testTag("animated_mascot_avatar"),
            contentAlignment = Alignment.Center
        ) {
            // Radiant Animated Glow Ring
            Box(
                modifier = Modifier
                    .size(66.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                when (isCorrect) {
                                    true -> SuccessGreen.copy(alpha = glowAlpha)
                                    false -> ErrorRed.copy(alpha = glowAlpha)
                                    null -> BrandBlue.copy(alpha = glowAlpha)
                                },
                                Color.Transparent
                            )
                        )
                    )
            )

            // Mascot Image Container
            Surface(
                shape = CircleShape,
                color = MaterialTheme.colorScheme.surface,
                shadowElevation = 4.dp,
                modifier = Modifier
                    .size(54.dp)
                    .border(
                        2.dp,
                        when (isCorrect) {
                            true -> SuccessGreen
                            false -> ErrorRed
                            null -> BrandBlue
                        },
                        CircleShape
                    )
            ) {
                Image(
                    painter = painterResource(id = R.drawable.img_exercise_mascot_1787934905928),
                    contentDescription = "Mascota Guía de Estudio",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            // Small Floating Emoji Reaction Badge
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .size(22.dp)
                    .clip(CircleShape)
                    .background(
                        when (isCorrect) {
                            true -> SuccessGreen
                            false -> ErrorRed
                            null -> BrandPurple
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = when {
                        comboStreak >= 3 -> "🔥"
                        isCorrect == true -> "🎉"
                        isCorrect == false -> "💡"
                        else -> "🦉"
                    },
                    fontSize = 11.sp
                )
            }
        }

        // Animated Speech Bubble
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surface,
            shadowElevation = 2.dp,
            modifier = Modifier
                .weight(1f)
                .border(
                    1.dp,
                    when (isCorrect) {
                        true -> SuccessGreen.copy(alpha = 0.5f)
                        false -> ErrorRed.copy(alpha = 0.5f)
                        null -> MaterialTheme.colorScheme.surfaceVariant
                    },
                    RoundedCornerShape(16.dp)
                )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                AnimatedContent(
                    targetState = stateMessage,
                    transitionSpec = {
                        (fadeIn(tween(250)) + slideInVertically { it / 3 })
                            .togetherWith(fadeOut(tween(200)) + slideOutVertically { -it / 3 })
                    },
                    label = "MascotSpeechAnimation",
                    modifier = Modifier.weight(1f)
                ) { msg ->
                    Text(
                        text = msg,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface,
                        lineHeight = 16.sp
                    )
                }

                if (comboStreak >= 2) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = WarningAmberLight
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(2.dp)
                        ) {
                            Text("🔥", fontSize = 11.sp)
                            Text(
                                text = "${comboStreak}x",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = WarningAmberDark
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * Creative Animated Streak Combo Bar
 */
@Composable
fun StreakComboMeter(
    combo: Int,
    modifier: Modifier = Modifier
) {
    if (combo < 2) return

    val infiniteTransition = rememberInfiniteTransition(label = "StreakFlame")
    val flamePulse by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "FlameScale"
    )

    Surface(
        shape = RoundedCornerShape(12.dp),
        color = WarningAmberLight,
        shadowElevation = 2.dp,
        modifier = modifier
            .scale(flamePulse)
            .border(1.dp, WarningAmber, RoundedCornerShape(12.dp))
            .testTag("streak_combo_meter")
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text("🔥", fontSize = 14.sp)
            Text(
                text = "¡Racha de $combo aciertos seguidos!",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = WarningAmberDark
            )
            Surface(
                shape = CircleShape,
                color = BrandCoral
            ) {
                Text(
                    text = "+${combo * 5} XP",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.padding(horizontal = 5.dp, vertical = 1.dp)
                )
            }
        }
    }
}

/**
 * Animated Celebration Confetti Overlay with Spinning Stars & Particles
 */
@Composable
fun CelebrationConfettiOverlay(
    visible: Boolean,
    modifier: Modifier = Modifier
) {
    if (!visible) return

    val particles = remember {
        List(55) {
            CreativeConfettiParticle(
                x = Random.nextFloat(),
                speedY = Random.nextFloat() * 450f + 250f,
                speedX = (Random.nextFloat() - 0.5f) * 260f,
                color = listOf(
                    Color(0xFF4CAF50), Color(0xFF2196F3), Color(0xFFFF9800),
                    Color(0xFFE91E63), Color(0xFFFFEB3B), Color(0xFF9C27B0),
                    Color(0xFF00E676), Color(0xFFFF4081)
                ).random(),
                size = Random.nextFloat() * 14f + 6f,
                rotation = Random.nextFloat() * 360f,
                rotationSpeed = (Random.nextFloat() - 0.5f) * 450f,
                isStar = Random.nextBoolean()
            )
        }
    }

    val progress = remember { Animatable(0f) }
    LaunchedEffect(visible) {
        progress.snapTo(0f)
        progress.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 1600, easing = LinearOutSlowInEasing)
        )
    }

    Canvas(
        modifier = modifier
            .fillMaxSize()
            .testTag("celebration_confetti_canvas")
    ) {
        val w = size.width
        val h = size.height
        val t = progress.value
        val alpha = (1f - t).coerceIn(0f, 1f)

        particles.forEach { p ->
            val curX = (p.x * w + p.speedX * t) % w
            val curY = p.speedY * t * 1.5f
            if (curY < h) {
                if (p.isStar) {
                    drawCircle(
                        color = p.color.copy(alpha = alpha),
                        radius = p.size * 0.45f,
                        center = Offset(curX, curY)
                    )
                } else {
                    drawRect(
                        color = p.color.copy(alpha = alpha),
                        topLeft = Offset(curX, curY),
                        size = Size(p.size, p.size * 0.6f)
                    )
                }
            }
        }
    }
}

private data class CreativeConfettiParticle(
    val x: Float,
    val speedY: Float,
    val speedX: Float,
    val color: Color,
    val size: Float,
    val rotation: Float,
    val rotationSpeed: Float,
    val isStar: Boolean
)


/**
 * Interactive Bilingual Context Card: Base in English + Spanish translation toggle & audio
 */
@Composable
fun BilingualQuestionContextCard(
    question: ModularExerciseQuestion,
    onSpeak: (String, Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    var showTranslation by remember(question.id) { mutableStateOf(false) }

    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier
            .fillMaxWidth()
            .animateContentSize(animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Level, Type Badge and Audio
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color(question.type.badgeColor).copy(alpha = 0.15f)
                    ) {
                        Text(
                            text = question.type.label,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(question.type.badgeColor),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant
                    ) {
                        Text(
                            text = question.level,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                        )
                    }

                    SrsQuestionStatusPill(
                        masteryLevel = question.srsMasteryLevel,
                        intervalDays = question.srsIntervalDays,
                        isDue = question.isSrsDue
                    )
                }

                // Audio Button
                val speakContent = question.audioText ?: question.contextText ?: question.baseEnglishSentence ?: question.prompt
                AudioSpeakButton(text = speakContent, onSpeak = onSpeak)
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Main English Title / Category
            Text(
                text = question.title,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(4.dp))

            // English Prompt + Spanish Subtitle
            Text(
                text = question.prompt,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface,
                lineHeight = 19.sp
            )

            if (question.promptSpanish != null) {
                Text(
                    text = "🇪🇸 ${question.promptSpanish}",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 16.sp,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }

            // Context Sentence (Base English in Highlighted Box)
            val baseEnglish = question.contextText ?: question.baseEnglishSentence
            if (baseEnglish != null) {
                Spacer(modifier = Modifier.height(10.dp))
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "🇬🇧 Base en Inglés",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = BrandBlue
                            )

                            // Quick Audio for sentence
                            AudioSpeakButton(text = baseEnglish, onSpeak = onSpeak)
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = baseEnglish,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface,
                            lineHeight = 21.sp
                        )

                        // Interactive Bilingual Translation Toggle
                        val spanishTrans = question.contextTextSpanish ?: question.spanishSentence ?: question.hintSpanish
                        if (spanishTrans != null) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End
                            ) {
                                TextButton(
                                    onClick = { showTranslation = !showTranslation },
                                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                                ) {
                                    Icon(
                                        imageVector = if (showTranslation) Icons.Default.VisibilityOff else Icons.Default.Translate,
                                        contentDescription = null,
                                        tint = BrandPurple,
                                        modifier = Modifier.size(15.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = if (showTranslation) "Ocultar Español" else "Ver en Español 🇪🇸",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = BrandPurple
                                    )
                                }
                            }

                            AnimatedVisibility(
                                visible = showTranslation,
                                enter = fadeIn() + expandVertically(),
                                exit = fadeOut() + shrinkVertically()
                            ) {
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = BrandPurpleLight,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 4.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(10.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Text(text = "🇪🇸", fontSize = 13.sp)
                                        Text(
                                            text = spanishTrans,
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = BrandPurpleDark,
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

/**
 * Visual illustration card for image-based vocabulary matching
 */
@Composable
fun VisualIllustrationCard(
    illustration: VisualIllustration,
    onSpeak: (String, Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val accentColor = Color(illustration.accentColorHex)
    val scaleAnim by rememberInfiniteTransition(label = "Pulse").animateFloat(
        initialValue = 1f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "PulseScale"
    )

    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        modifier = modifier
            .fillMaxWidth()
            .testTag("visual_illustration_card_${illustration.id}")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Category & Audio Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = accentColor.copy(alpha = 0.15f)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(text = illustration.category.primaryEmoji, fontSize = 13.sp)
                        Text(
                            text = illustration.category.displayName,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = accentColor
                        )
                    }
                }

                AudioSpeakButton(
                    text = illustration.englishWord,
                    onSpeak = onSpeak
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Main Graphic Container with gradient aura and subtle pulse
            Box(
                modifier = Modifier
                    .size(130.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                accentColor.copy(alpha = 0.12f),
                                accentColor.copy(alpha = 0.28f)
                            )
                        )
                    )
                    .border(2.dp, accentColor.copy(alpha = 0.35f), RoundedCornerShape(24.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = illustration.visualEmoji,
                    fontSize = 56.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.scale(scaleAnim)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Bilingual Spanish/English Description Clue
            if (illustration.visualDescription.isNotBlank()) {
                Text(
                    text = illustration.visualDescription,
                    fontSize = 13.sp,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 18.sp,
                    modifier = Modifier.padding(horizontal = 12.dp)
                )
            }

            if (illustration.spanishTranslation.isNotBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant
                ) {
                    Text(
                        text = "🇪🇸 ${illustration.spanishTranslation}",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                }
            }
        }
    }
}

/**
 * 1. MULTIPLE CHOICE QUESTION VIEW (Bilingual & Animated)
 */
@Composable
fun MultipleChoiceQuestionView(
    question: ModularExerciseQuestion,
    selectedIndex: Int?,
    isSubmitted: Boolean,
    onSelectOption: (Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("multiple_choice_view"),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        question.options.forEachIndexed { index, optionText ->
            val isSelected = selectedIndex == index
            val isCorrect = optionText.equals(question.correctAnswer, ignoreCase = true)
            val spanishOption = question.optionsSpanish.getOrNull(index)

            // Bouncy Scale Spring Animation
            val scale by animateFloatAsState(
                targetValue = if (isSelected && !isSubmitted) 1.02f else 1.0f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                ),
                label = "OptionSpringScale"
            )

            val containerColor = when {
                !isSubmitted && isSelected -> BrandBlueLight
                isSubmitted && isCorrect -> SuccessGreenLight
                isSubmitted && isSelected && !isCorrect -> ErrorRedLight
                else -> MaterialTheme.colorScheme.surface
            }

            val borderColor = when {
                !isSubmitted && isSelected -> BrandBlue
                isSubmitted && isCorrect -> SuccessGreen
                isSubmitted && isSelected && !isCorrect -> ErrorRed
                else -> MaterialTheme.colorScheme.surfaceVariant
            }

            val labelColor = when {
                !isSubmitted && isSelected -> BrandBlue
                isSubmitted && isCorrect -> SuccessGreen
                isSubmitted && isSelected && !isCorrect -> ErrorRed
                else -> MaterialTheme.colorScheme.onSurface
            }

            Surface(
                shape = RoundedCornerShape(14.dp),
                color = containerColor,
                modifier = Modifier
                    .fillMaxWidth()
                    .scale(scale)
                    .border(1.5.dp, borderColor, RoundedCornerShape(14.dp))
                    .clickable(enabled = !isSubmitted) { onSelectOption(index) }
                    .testTag("mc_option_$index")
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(
                                when {
                                    !isSubmitted && isSelected -> BrandBlue
                                    isSubmitted && isCorrect -> SuccessGreen
                                    isSubmitted && isSelected -> ErrorRed
                                    else -> MaterialTheme.colorScheme.surfaceVariant
                                }
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = ('A' + index).toString(),
                            color = if (isSelected || (isSubmitted && isCorrect)) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = optionText,
                            fontSize = 15.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = labelColor
                        )

                        if (spanishOption != null) {
                            Text(
                                text = "🇪🇸 $spanishOption",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    if (isSubmitted) {
                        if (isCorrect) {
                            Icon(
                                Icons.Default.CheckCircle,
                                contentDescription = "Correcto",
                                tint = SuccessGreen,
                                modifier = Modifier.size(22.dp)
                            )
                        } else if (isSelected) {
                            Icon(
                                Icons.Default.Cancel,
                                contentDescription = "Incorrecto",
                                tint = ErrorRed,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * 2. FILL IN THE BLANK QUESTION VIEW (Bilingual & Animated)
 */
@Composable
fun FillBlankQuestionView(
    question: ModularExerciseQuestion,
    userInput: String,
    isSubmitted: Boolean,
    onInputChange: (String) -> Unit,
    onSubmit: () -> Unit
) {
    var showHint by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("fill_blank_view"),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        OutlinedTextField(
            value = userInput,
            onValueChange = { if (!isSubmitted) onInputChange(it) },
            label = { Text("Escribe tu respuesta en inglés") },
            placeholder = { Text("Palabra o frase faltante...") },
            singleLine = true,
            enabled = !isSubmitted,
            shape = RoundedCornerShape(14.dp),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { if (userInput.isNotBlank()) onSubmit() }),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = BrandBlue,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline
            ),
            trailingIcon = {
                if (userInput.isNotEmpty() && !isSubmitted) {
                    IconButton(onClick = { onInputChange("") }) {
                        Icon(Icons.Default.Clear, contentDescription = "Borrar", modifier = Modifier.size(18.dp))
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .testTag("fill_blank_text_field")
        )

        // Interactive Hint Button
        val hintText = question.hintSpanish
        if (hintText != null && !isSubmitted) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(
                    onClick = { showHint = !showHint },
                    modifier = Modifier.testTag("toggle_hint_btn")
                ) {
                    Icon(
                        imageVector = if (showHint) Icons.Default.VisibilityOff else Icons.Default.Lightbulb,
                        contentDescription = null,
                        tint = WarningAmber,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (showHint) "Ocultar Pista" else "Ver Pista / Traducción",
                        fontSize = 12.sp,
                        color = WarningAmberDark,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            AnimatedVisibility(
                visible = showHint,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = WarningAmberLight,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(Icons.Default.Info, contentDescription = null, tint = WarningAmber, modifier = Modifier.size(18.dp))
                        Text(
                            text = hintText,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurface,
                            lineHeight = 16.sp
                        )
                    }
                }
            }
        }
    }
}

/**
 * 3. IMAGE-BASED VOCABULARY MATCHING VIEW (Bilingual & Animated)
 */
@Composable
fun ImageMatchingQuestionView(
    question: ModularExerciseQuestion,
    selectedIndex: Int?,
    isSubmitted: Boolean,
    onSpeak: (String, Boolean) -> Unit,
    onSelectOption: (Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("image_matching_view"),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Visual Illustration Card
        if (question.visualIllustration != null) {
            VisualIllustrationCard(
                illustration = question.visualIllustration,
                onSpeak = onSpeak
            )
        }

        // Matching Vocabulary Options
        Text(
            text = "Selecciona la palabra en inglés correspondiente:",
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            question.options.forEachIndexed { index, optionText ->
                val isSelected = selectedIndex == index
                val isCorrect = optionText.equals(question.correctAnswer, ignoreCase = true)
                val spanishTranslation = question.optionsSpanish.getOrNull(index)

                val scale by animateFloatAsState(
                    targetValue = if (isSelected && !isSubmitted) 1.02f else 1.0f,
                    animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
                    label = "ImageOptionSpring"
                )

                val containerColor = when {
                    !isSubmitted && isSelected -> BrandCoralLight
                    isSubmitted && isCorrect -> SuccessGreenLight
                    isSubmitted && isSelected && !isCorrect -> ErrorRedLight
                    else -> MaterialTheme.colorScheme.surface
                }

                val borderColor = when {
                    !isSubmitted && isSelected -> BrandCoral
                    isSubmitted && isCorrect -> SuccessGreen
                    isSubmitted && isSelected && !isCorrect -> ErrorRed
                    else -> MaterialTheme.colorScheme.surfaceVariant
                }

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = containerColor,
                    modifier = Modifier
                        .fillMaxWidth()
                        .scale(scale)
                        .border(1.5.dp, borderColor, RoundedCornerShape(12.dp))
                        .clickable(enabled = !isSubmitted) { onSelectOption(index) }
                        .testTag("img_vocab_option_$index")
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(26.dp)
                                    .clip(CircleShape)
                                    .background(
                                        when {
                                            !isSubmitted && isSelected -> BrandCoral
                                            isSubmitted && isCorrect -> SuccessGreen
                                            isSubmitted && isSelected -> ErrorRed
                                            else -> MaterialTheme.colorScheme.surfaceVariant
                                        }
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = ('1' + index).toString(),
                                    color = if (isSelected || (isSubmitted && isCorrect)) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Column {
                                Text(
                                    text = optionText,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) BrandCoralDark else MaterialTheme.colorScheme.onSurface
                                )
                                if (spanishTranslation != null) {
                                    Text(
                                        text = "🇪🇸 $spanishTranslation",
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }

                        if (isSubmitted) {
                            if (isCorrect) {
                                Icon(Icons.Default.CheckCircle, contentDescription = "Correcto", tint = SuccessGreen, modifier = Modifier.size(20.dp))
                            } else if (isSelected) {
                                Icon(Icons.Default.Cancel, contentDescription = "Incorrecto", tint = ErrorRed, modifier = Modifier.size(20.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * 4. CAMBRIDGE KEYWORD TRANSFORMATION VIEW (Bilingual & Animated)
 */
@Composable
fun KeywordTransformationQuestionView(
    question: ModularExerciseQuestion,
    userInput: String,
    isSubmitted: Boolean,
    onInputChange: (String) -> Unit,
    onSubmit: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("transformation_view"),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        if (question.keyWord != null) {
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = BrandPurpleLight,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Palabra Clave Obligatoria:", fontSize = 12.sp, color = BrandPurpleDark)
                    Surface(shape = RoundedCornerShape(6.dp), color = BrandPurple) {
                        Text(
                            text = question.keyWord,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                        )
                    }
                }
            }
        }

        OutlinedTextField(
            value = userInput,
            onValueChange = { if (!isSubmitted) onInputChange(it) },
            label = { Text("Completa el espacio en inglés") },
            placeholder = { Text("Escribe la transformación exacta...") },
            singleLine = true,
            enabled = !isSubmitted,
            shape = RoundedCornerShape(14.dp),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { if (userInput.isNotBlank()) onSubmit() }),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("transformation_text_field")
        )
    }
}

/**
 * SRS Question Status Pill
 */
@Composable
fun SrsQuestionStatusPill(
    masteryLevel: Int?,
    intervalDays: Int?,
    isDue: Boolean,
    modifier: Modifier = Modifier
) {
    if (masteryLevel == null && intervalDays == null && !isDue) return

    val (bgColor, textColor, label) = when {
        isDue -> Triple(ErrorRedLight, ErrorRedDark, "🔴 Repaso Urgente")
        (masteryLevel ?: 0) >= 4 -> Triple(SuccessGreenLight, SuccessGreenDark, "🟢 Dominado (${intervalDays ?: 14}d)")
        (masteryLevel ?: 0) in 2..3 -> Triple(BrandBlueLight, BrandBlueDark, "🔵 Consolidando (${intervalDays ?: 3}d)")
        else -> Triple(WarningAmberLight, WarningAmberDark, "🟡 En Aprendizaje (${intervalDays ?: 1}d)")
    }

    Surface(
        shape = RoundedCornerShape(8.dp),
        color = bgColor,
        modifier = modifier.testTag("srs_question_status_pill")
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = label,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = textColor
            )
        }
    }
}

/**
 * Dynamic Feedback Explanation Card with Bilingual Explanation, Audio, and SRS Spaced Repetition Scheduling
 */
@Composable
fun ModularExerciseFeedbackCard(
    question: ModularExerciseQuestion,
    isCorrect: Boolean,
    onSpeak: (String, Boolean) -> Unit,
    srsResult: SrsEvaluationResult? = null,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isCorrect) SuccessGreenLight else ErrorRedLight
        ),
        modifier = modifier
            .fillMaxWidth()
            .testTag("exercise_feedback_card")
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = if (isCorrect) Icons.Default.CheckCircle else Icons.Default.Cancel,
                        contentDescription = null,
                        tint = if (isCorrect) SuccessGreen else ErrorRed,
                        modifier = Modifier.size(24.dp)
                    )
                    Text(
                        text = if (isCorrect) "¡Excelente! Respuesta Correcta" else "Respuesta esperada: ${question.correctAnswer}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = if (isCorrect) SuccessGreenDark else ErrorRedDark
                    )
                }

                if (question.audioText != null || question.correctAnswer.isNotBlank()) {
                    AudioSpeakButton(text = question.audioText ?: question.correctAnswer, onSpeak = onSpeak)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Explanation in Spanish
            Text(
                text = "🇪🇸 ${question.explanation}",
                fontSize = 13.sp,
                lineHeight = 19.sp,
                color = MaterialTheme.colorScheme.onSurface
            )

            // SRS REPETITION SCHEDULING CARD
            if (srsResult != null) {
                Spacer(modifier = Modifier.height(10.dp))
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.surface,
                    shadowElevation = 1.dp,
                    tonalElevation = 1.dp,
                    modifier = Modifier.fillMaxWidth().testTag("srs_evaluation_banner")
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
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
                                Text(text = "🧠", fontSize = 14.sp)
                                Text(
                                    text = "Repetición Espaciada (SM-2+)",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    color = BrandPurpleDark
                                )
                            }

                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = Color(srsResult.frequencyPriority.colorHex).copy(alpha = 0.15f)
                            ) {
                                Text(
                                    text = srsResult.frequencyPriority.badgeLabel,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(srsResult.frequencyPriority.colorHex),
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }

                        // Metrics Grid
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(text = "Próximo Repaso", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text(
                                    text = srsResult.humanReadableNextReview,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (srsResult.isCorrect) SuccessGreenDark else ErrorRedDark
                                )
                            }

                            Column {
                                Text(text = "Intervalo", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text(
                                    text = "${srsResult.newIntervalDays}d (${if (srsResult.intervalDeltaDays >= 0) "+${srsResult.intervalDeltaDays}d" else "${srsResult.intervalDeltaDays}d"})",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }

                            Column {
                                Text(text = "Dominio", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text(
                                    text = "Nivel ${srsResult.newMasteryLevel}/5 ⭐",
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
    }
}

/**
 * Animated Victory Trophy Hero Card with Rotating Lightburst and Dynamic XP Counter
 */
@Composable
fun AnimatedTrophyHero(
    score: Int,
    total: Int,
    accuracy: Int,
    earnedXp: Int,
    onRestart: () -> Unit,
    onContinue: () -> Unit,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "TrophyRayRotation")
    val rotationAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(20000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "RayAngle"
    )

    val bounceTrophy by infiniteTransition.animateFloat(
        initialValue = -6f,
        targetValue = 6f,
        animationSpec = infiniteRepeatable(
            animation = tween(1800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "BounceY"
    )

    // Animated score and XP count-up
    val animatedXp by animateIntAsState(
        targetValue = earnedXp,
        animationSpec = tween(durationMillis = 1500, easing = FastOutSlowInEasing),
        label = "AnimatedXpCounter"
    )

    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        modifier = modifier
            .fillMaxWidth()
            .testTag("animated_trophy_hero_card")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Radiant Animated Trophy with Rotating Sunburst
            Box(
                modifier = Modifier
                    .size(160.dp),
                contentAlignment = Alignment.Center
            ) {
                // Rotating radiant sunburst background
                Canvas(
                    modifier = Modifier
                        .size(150.dp)
                        .rotate(rotationAngle)
                ) {
                    val center = Offset(size.width / 2, size.height / 2)
                    val rayCount = 12
                    val radius = size.minDimension / 2
                    for (i in 0 until rayCount) {
                        val angle = Math.toRadians((i * (360.0 / rayCount)))
                        val endX = (center.x + radius * cos(angle)).toFloat()
                        val endY = (center.y + radius * sin(angle)).toFloat()
                        drawLine(
                            color = WarningAmber.copy(alpha = 0.25f),
                            start = center,
                            end = Offset(endX, endY),
                            strokeWidth = 14f
                        )
                    }
                }

                // Shimmering Trophy Image
                Surface(
                    shape = CircleShape,
                    color = WarningAmberLight,
                    shadowElevation = 8.dp,
                    modifier = Modifier
                        .size(110.dp)
                        .offset(y = bounceTrophy.dp)
                        .border(3.dp, WarningAmber, CircleShape)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.img_trophy_celebration_1787934922797),
                        contentDescription = "Trofeo de Victoria y Celebración",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = if (accuracy >= 80) "¡Sesión Completada con Maestría!" else "¡Gran Esfuerzo en la Práctica!",
                fontSize = 19.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )

            Text(
                text = "Has consolidado vocabulario y gramática bilingüe",
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 4.dp)
            )

            Spacer(modifier = Modifier.height(18.dp))

            // Animated XP Reward Banner
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = BrandPurpleLight,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text("⚡", fontSize = 22.sp)
                        Column {
                            Text(
                                text = "Experiencia Ganada",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = BrandPurpleDark
                            )
                            Text(
                                text = "+$animatedXp XP",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = BrandPurple
                            )
                        }
                    }

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = BrandPurple
                    ) {
                        Text(
                            text = "$accuracy% Precisión",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Stats row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = SuccessGreenLight,
                    modifier = Modifier.weight(1f)
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "✅ Aciertos", fontSize = 11.sp, color = SuccessGreenDark)
                        Text(
                            text = "$score / $total",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = SuccessGreenDark
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = BrandBlueLight,
                    modifier = Modifier.weight(1f)
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "🧠 Algoritmo SRS", fontSize = 11.sp, color = BrandBlueDark)
                        Text(
                            text = "Calibrado",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = BrandBlueDark
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = onRestart,
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("restart_exercise_btn")
                ) {
                    Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Repetir")
                }

                Button(
                    onClick = onContinue,
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = BrandBlue),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("continue_after_exercise_btn")
                ) {
                    Text("Continuar")
                    Spacer(modifier = Modifier.width(6.dp))
                    Icon(Icons.Default.ArrowForward, contentDescription = null, modifier = Modifier.size(18.dp))
                }
            }
        }
    }
}

