package com.example.ui.screens.learn

import androidx.compose.animation.*
import androidx.compose.animation.core.*
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
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.ExerciseLocalData
import com.example.data.local.model.ModularExerciseQuestion
import com.example.data.local.model.ModularExerciseType
import com.example.data.srs.SrsAlgorithm
import com.example.data.srs.SrsEvaluationResult
import com.example.ui.components.AudioSpeakButton
import com.example.ui.components.ScreenTopBar
import com.example.ui.screens.learn.exercises.*
import com.example.ui.theme.*
import com.example.ui.viewmodels.LearnSubScreen
import com.example.ui.viewmodels.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExercisesScreen(
    viewModel: MainViewModel,
    onSpeak: (String, Boolean) -> Unit
) {
    val currentTopic by viewModel.selectedTopic.collectAsState()
    val savedVocabItems by viewModel.allVocabItems.collectAsState()
    val dueVocabItems by viewModel.dueVocabItems.collectAsState()
    val srsStats by viewModel.srsSummaryStats.collectAsState()

    // Exercise Mode and Filter State
    var selectedTypeFilter by remember { mutableStateOf<ModularExerciseType?>(null) }
    var selectedLevelFilter by remember { mutableStateOf("TODOS") }
    var onlySavedVocab by remember { mutableStateOf(false) }
    var onlyDueSrs by remember { mutableStateOf(false) }

    // Fetch exercises from local data source and Room DB, prioritized with SRS algorithm
    val questions = remember(currentTopic, savedVocabItems, selectedTypeFilter, selectedLevelFilter, onlySavedVocab, onlyDueSrs) {
        if (onlyDueSrs) {
            val dueList = savedVocabItems.filter { it.nextReviewTimestamp <= System.currentTimeMillis() }
            val pool = if (dueList.isNotEmpty()) dueList else savedVocabItems
            ExerciseLocalData.getFilteredExercises(
                typeFilter = selectedTypeFilter,
                levelFilter = selectedLevelFilter,
                savedVocabItems = pool,
                currentTopic = currentTopic,
                onlyDueSrs = true,
                sortBySrsPriority = true
            )
        } else if (onlySavedVocab) {
            ExerciseLocalData.getFilteredExercises(
                typeFilter = selectedTypeFilter,
                levelFilter = selectedLevelFilter,
                savedVocabItems = savedVocabItems,
                currentTopic = currentTopic,
                onlyDueSrs = false,
                sortBySrsPriority = true
            )
        } else {
            ExerciseLocalData.getFilteredExercises(
                typeFilter = selectedTypeFilter,
                levelFilter = selectedLevelFilter,
                savedVocabItems = savedVocabItems,
                currentTopic = currentTopic,
                onlyDueSrs = false,
                sortBySrsPriority = true
            )
        }
    }

    // Active Exercise Navigation & Submission State
    var currentIndex by remember(questions) { mutableIntStateOf(0) }
    var selectedOptionIndex by remember { mutableStateOf<Int?>(null) }
    var fillBlankInput by remember { mutableStateOf("") }
    var isSubmitted by remember { mutableStateOf(false) }
    var isAnswerCorrect by remember { mutableStateOf<Boolean?>(null) }
    var comboStreak by remember { mutableIntStateOf(0) }
    var score by remember { mutableIntStateOf(0) }
    var isCompleted by remember { mutableStateOf(false) }

    // Mascot interactive dialog messages
    val mascotMessage = remember(isSubmitted, isAnswerCorrect, comboStreak, currentIndex) {
        when {
            comboStreak >= 3 -> "🔥 ¡Racha de $comboStreak aciertos seguidos! ¡Estás en tu mejor nivel!"
            isSubmitted && isAnswerCorrect == true -> "¡Increíble! 🎉 Respuesta perfecta. Sigamos aprendiendo."
            isSubmitted && isAnswerCorrect == false -> "¡Buen intento! 💡 Revisa la explicación en español para reforzarlo."
            else -> "¡Elige o escribe tu respuesta en inglés! Siempre puedes consultar la pista bilingüe 🦉"
        }
    }

    // SRS Tracking Variables for continuous calibration
    var questionStartTime by remember(currentIndex) { mutableLongStateOf(System.currentTimeMillis()) }
    var usedHint by remember { mutableStateOf(false) }
    var currentSrsResult by remember { mutableStateOf<SrsEvaluationResult?>(null) }
    var sessionSrsUpdatesCount by remember { mutableIntStateOf(0) }

    // Reset current question state when filter or question index changes
    fun resetQuestionState() {
        selectedOptionIndex = null
        fillBlankInput = ""
        isSubmitted = false
        isAnswerCorrect = null
        usedHint = false
        currentSrsResult = null
        questionStartTime = System.currentTimeMillis()
    }

    val currentQuestion = questions.getOrNull(currentIndex)
    val totalCount = questions.size.coerceAtLeast(1)

    // Celebration Confetti state
    var showCelebration by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            ScreenTopBar(
                title = if (currentTopic != null) "Práctica: ${currentTopic?.titleSpanish}" else "Ejercicios Bilingües y Exámenes",
                subtitle = if (questions.isNotEmpty()) "Pregunta ${currentIndex + 1} de $totalCount • Algoritmo SRS Bilingüe" else "0 preguntas disponibles",
                onBack = {
                    if (currentTopic != null) {
                        viewModel.navigateToLearnSubScreen(LearnSubScreen.TOPIC_DETAIL)
                    } else {
                        viewModel.navigateToLearnSubScreen(LearnSubScreen.TOPICS_LIST)
                    }
                }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            if (questions.isEmpty()) {
                // Empty State
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Surface(
                        shape = CircleShape,
                        color = BrandBlueLight,
                        modifier = Modifier.size(80.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(text = "🔍", fontSize = 36.sp)
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "No hay ejercicios con los filtros seleccionados",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Intenta seleccionar 'Todos los tipos' o cambiar el nivel.",
                        fontSize = 13.sp,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Button(
                        onClick = {
                            selectedTypeFilter = null
                            selectedLevelFilter = "TODOS"
                            onlySavedVocab = false
                            onlyDueSrs = false
                            currentIndex = 0
                            resetQuestionState()
                        },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = BrandBlue)
                    ) {
                        Text("Restablecer Filtros")
                    }
                }
            } else if (isCompleted || currentQuestion == null) {
                // Summary Screen with detailed SRS, animated trophy, and performance breakdown
                val accuracy = if (totalCount > 0) (score.toFloat() / totalCount * 100).toInt() else 0
                val xpEarned = score * 15 + sessionSrsUpdatesCount * 5

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item {
                        AnimatedTrophyHero(
                            score = score,
                            total = totalCount,
                            accuracy = accuracy,
                            earnedXp = xpEarned,
                            onRestart = {
                                currentIndex = 0
                                resetQuestionState()
                                score = 0
                                comboStreak = 0
                                isCompleted = false
                                sessionSrsUpdatesCount = 0
                            },
                            onContinue = {
                                if (currentTopic != null) {
                                    viewModel.recordExerciseScore(currentTopic?.id ?: "general", "MODULAR_PRACTICE", score, totalCount)
                                    viewModel.navigateToLearnSubScreen(LearnSubScreen.TOPIC_DETAIL)
                                } else {
                                    viewModel.recordExerciseScore("general", "MODULAR_PRACTICE", score, totalCount)
                                    viewModel.navigateToLearnSubScreen(LearnSubScreen.TOPICS_LIST)
                                }
                            }
                        )
                    }

                    // SRS Impact Summary Card
                    item {
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                            modifier = Modifier.fillMaxWidth().testTag("srs_session_summary_card")
                        ) {
                            Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Text("🧠", fontSize = 16.sp)
                                    Text(
                                        text = "Actualización del Algoritmo SRS (SM-2+)",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        color = BrandPurpleDark
                                    )
                                }
                                Text(
                                    text = "Tus respuestas han recalibrado los intervalos y la frecuencia de aparición en el sistema de ejercicios. Los términos dominados aparecerán con menor frecuencia y los términos difíciles serán reforzados.",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    lineHeight = 16.sp
                                )
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "• Items actualizados: $sessionSrsUpdatesCount",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = "• Retención estimada: ${srsStats.estimatedRetentionRate}%",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = SuccessGreenDark
                                    )
                                }
                            }
                        }
                    }
                }
            } else {
                // Active Question View
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // 0. ANIMATED MASCOT COMPANION & STREAK COMBO
                    item {
                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            AnimatedMascotCompanion(
                                stateMessage = mascotMessage,
                                isCorrect = isAnswerCorrect,
                                comboStreak = comboStreak
                            )
                            if (comboStreak >= 2) {
                                StreakComboMeter(combo = comboStreak)
                            }
                        }
                    }

                    // 1. FILTER TABS (Exercise Type & Level Selectors)
                    item {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            // Level Filter Row
                            LazyRow(
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                val levels = listOf(
                                    "TODOS" to "🎯 Todos",
                                    "Starters" to "👶 Starters (Pre A1)",
                                    "Movers" to "🚀 Movers (A1)",
                                    "Flyers" to "✈️ Flyers (A2)",
                                    "A1" to "📘 A1",
                                    "A2" to "📙 A2",
                                    "B1" to "📗 B1",
                                    "B2" to "🎓 B2"
                                )
                                items(levels) { (code, label) ->
                                    FilterChip(
                                        selected = selectedLevelFilter == code,
                                        onClick = {
                                            selectedLevelFilter = code
                                            currentIndex = 0
                                            resetQuestionState()
                                        },
                                        label = { Text(label, fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
                                        colors = FilterChipDefaults.filterChipColors(
                                            selectedContainerColor = BrandBlue,
                                            selectedLabelColor = Color.White
                                        ),
                                        modifier = Modifier.testTag("filter_level_$code")
                                    )
                                }
                            }

                            // Exercise Type Row
                            LazyRow(
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                item {
                                    FilterChip(
                                        selected = selectedTypeFilter == null && !onlySavedVocab && !onlyDueSrs,
                                        onClick = {
                                            selectedTypeFilter = null
                                            onlySavedVocab = false
                                            onlyDueSrs = false
                                            currentIndex = 0
                                            resetQuestionState()
                                        },
                                        label = { Text("⚡ Todos los Tipos", fontSize = 11.sp) },
                                        colors = FilterChipDefaults.filterChipColors(
                                            selectedContainerColor = BrandBlue,
                                            selectedLabelColor = Color.White
                                        ),
                                        modifier = Modifier.testTag("filter_all_types")
                                    )
                                }
                                if (dueVocabItems.isNotEmpty()) {
                                    item {
                                        FilterChip(
                                            selected = onlyDueSrs,
                                            onClick = {
                                                onlyDueSrs = true
                                                onlySavedVocab = false
                                                currentIndex = 0
                                                resetQuestionState()
                                            },
                                            label = { Text("🔴 Vencidos SRS (${dueVocabItems.size})", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                                            colors = FilterChipDefaults.filterChipColors(
                                                selectedContainerColor = ErrorRed,
                                                selectedLabelColor = Color.White
                                            ),
                                            modifier = Modifier.testTag("filter_due_srs")
                                        )
                                    }
                                }
                                item {
                                    FilterChip(
                                        selected = selectedTypeFilter == ModularExerciseType.IMAGE_VOCAB_MATCHING && !onlySavedVocab && !onlyDueSrs,
                                        onClick = {
                                            selectedTypeFilter = ModularExerciseType.IMAGE_VOCAB_MATCHING
                                            onlySavedVocab = false
                                            onlyDueSrs = false
                                            currentIndex = 0
                                            resetQuestionState()
                                        },
                                        label = { Text("🖼️ Visual Emojis", fontSize = 11.sp) },
                                        colors = FilterChipDefaults.filterChipColors(
                                            selectedContainerColor = BrandCoral,
                                            selectedLabelColor = Color.White
                                        ),
                                        modifier = Modifier.testTag("filter_image_vocab")
                                    )
                                }
                                item {
                                    FilterChip(
                                        selected = selectedTypeFilter == ModularExerciseType.MULTIPLE_CHOICE && !onlySavedVocab && !onlyDueSrs,
                                        onClick = {
                                            selectedTypeFilter = ModularExerciseType.MULTIPLE_CHOICE
                                            onlySavedVocab = false
                                            onlyDueSrs = false
                                            currentIndex = 0
                                            resetQuestionState()
                                        },
                                        label = { Text("🔘 Opción Múltiple", fontSize = 11.sp) },
                                        colors = FilterChipDefaults.filterChipColors(
                                            selectedContainerColor = BrandBlue,
                                            selectedLabelColor = Color.White
                                        ),
                                        modifier = Modifier.testTag("filter_multiple_choice")
                                    )
                                }
                                item {
                                    FilterChip(
                                        selected = selectedTypeFilter == ModularExerciseType.FILL_IN_THE_BLANK && !onlySavedVocab && !onlyDueSrs,
                                        onClick = {
                                            selectedTypeFilter = ModularExerciseType.FILL_IN_THE_BLANK
                                            onlySavedVocab = false
                                            onlyDueSrs = false
                                            currentIndex = 0
                                            resetQuestionState()
                                        },
                                        label = { Text("✍️ Completar", fontSize = 11.sp) },
                                        colors = FilterChipDefaults.filterChipColors(
                                            selectedContainerColor = SuccessGreen,
                                            selectedLabelColor = Color.White
                                        ),
                                        modifier = Modifier.testTag("filter_fill_blank")
                                    )
                                }
                                if (savedVocabItems.isNotEmpty()) {
                                    item {
                                        FilterChip(
                                            selected = onlySavedVocab && !onlyDueSrs,
                                            onClick = {
                                                onlySavedVocab = true
                                                onlyDueSrs = false
                                                currentIndex = 0
                                                resetQuestionState()
                                            },
                                            label = { Text("🧠 Mi Vocabulario (${savedVocabItems.size})", fontSize = 11.sp) },
                                            colors = FilterChipDefaults.filterChipColors(
                                                selectedContainerColor = BrandPurple,
                                                selectedLabelColor = Color.White
                                            ),
                                            modifier = Modifier.testTag("filter_srs_vocab")
                                        )
                                    }
                                }
                            }

                            // Progress Indicator with Animation
                            val currentFraction = (currentIndex + 1).toFloat() / totalCount
                            val animatedFraction by animateFloatAsState(
                                targetValue = currentFraction,
                                animationSpec = tween(500, easing = FastOutSlowInEasing),
                                label = "ExerciseProgressBarAnimation"
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                LinearProgressIndicator(
                                    progress = { animatedFraction },
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(8.dp)
                                        .clip(RoundedCornerShape(4.dp))
                                        .testTag("modular_exercise_progress_bar"),
                                    color = when (currentQuestion.type) {
                                        ModularExerciseType.IMAGE_VOCAB_MATCHING -> BrandCoral
                                        ModularExerciseType.MULTIPLE_CHOICE -> BrandBlue
                                        ModularExerciseType.FILL_IN_THE_BLANK -> SuccessGreen
                                        ModularExerciseType.KEYWORD_TRANSFORMATION -> BrandPurple
                                    },
                                    trackColor = MaterialTheme.colorScheme.surfaceVariant
                                )
                                Text(
                                    text = "${currentIndex + 1} / $totalCount",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }

                    // 2. ANIMATED BILINGUAL QUESTION CONTEXT CARD
                    item {
                        AnimatedContent(
                            targetState = currentQuestion,
                            transitionSpec = {
                                (fadeIn(animationSpec = tween(300)) + slideInHorizontally { width -> width / 3 })
                                    .togetherWith(fadeOut(animationSpec = tween(200)) + slideOutHorizontally { width -> -width / 3 })
                            },
                            label = "QuestionContextAnimation"
                        ) { targetQ ->
                            BilingualQuestionContextCard(
                                question = targetQ,
                                onSpeak = onSpeak
                            )
                        }
                    }

                    // 3. MODULAR QUESTION TYPE INTERFACE (With Animation)
                    item {
                        AnimatedContent(
                            targetState = currentQuestion,
                            transitionSpec = {
                                (fadeIn(animationSpec = tween(300)) + slideInHorizontally { width -> width / 4 })
                                    .togetherWith(fadeOut(animationSpec = tween(200)) + slideOutHorizontally { width -> -width / 4 })
                            },
                            label = "QuestionInputAnimation"
                        ) { targetQ ->
                            when (targetQ.type) {
                                ModularExerciseType.MULTIPLE_CHOICE -> {
                                    MultipleChoiceQuestionView(
                                        question = targetQ,
                                        selectedIndex = selectedOptionIndex,
                                        isSubmitted = isSubmitted,
                                        onSelectOption = { selectedOptionIndex = it }
                                    )
                                }
                                ModularExerciseType.FILL_IN_THE_BLANK -> {
                                    FillBlankQuestionView(
                                        question = targetQ,
                                        userInput = fillBlankInput,
                                        isSubmitted = isSubmitted,
                                        onInputChange = { fillBlankInput = it },
                                        onSubmit = {
                                            if (!isSubmitted) {
                                                isSubmitted = true
                                                val isCorrect = checkUserAnswer(targetQ, selectedOptionIndex, fillBlankInput)
                                                isAnswerCorrect = isCorrect
                                                if (isCorrect) {
                                                    score++
                                                    comboStreak++
                                                    showCelebration = true
                                                } else {
                                                    comboStreak = 0
                                                }
                                                val responseTime = (System.currentTimeMillis() - questionStartTime).coerceAtLeast(100L)
                                                viewModel.processExerciseAnswerSrs(
                                                    question = targetQ,
                                                    isCorrect = isCorrect,
                                                    usedHint = usedHint,
                                                    responseTimeMs = responseTime
                                                ) { eval ->
                                                    currentSrsResult = eval
                                                    sessionSrsUpdatesCount++
                                                }
                                            }
                                        }
                                    )
                                }
                                ModularExerciseType.IMAGE_VOCAB_MATCHING -> {
                                    ImageMatchingQuestionView(
                                        question = targetQ,
                                        selectedIndex = selectedOptionIndex,
                                        isSubmitted = isSubmitted,
                                        onSpeak = onSpeak,
                                        onSelectOption = { selectedOptionIndex = it }
                                    )
                                }
                                ModularExerciseType.KEYWORD_TRANSFORMATION -> {
                                    KeywordTransformationQuestionView(
                                        question = targetQ,
                                        userInput = fillBlankInput,
                                        isSubmitted = isSubmitted,
                                        onInputChange = { fillBlankInput = it },
                                        onSubmit = {
                                            if (!isSubmitted) {
                                                isSubmitted = true
                                                val isCorrect = checkUserAnswer(targetQ, selectedOptionIndex, fillBlankInput)
                                                isAnswerCorrect = isCorrect
                                                if (isCorrect) {
                                                    score++
                                                    comboStreak++
                                                    showCelebration = true
                                                } else {
                                                    comboStreak = 0
                                                }
                                                val responseTime = (System.currentTimeMillis() - questionStartTime).coerceAtLeast(100L)
                                                viewModel.processExerciseAnswerSrs(
                                                    question = targetQ,
                                                    isCorrect = isCorrect,
                                                    usedHint = usedHint,
                                                    responseTimeMs = responseTime
                                                ) { eval ->
                                                    currentSrsResult = eval
                                                    sessionSrsUpdatesCount++
                                                }
                                            }
                                        }
                                    )
                                }
                            }
                        }
                    }

                    // 4. FEEDBACK / EXPLANATION CARD WITH SRS SCHEDULING
                    if (isSubmitted) {
                        item {
                            val isCorrect = checkUserAnswer(currentQuestion, selectedOptionIndex, fillBlankInput)
                            ModularExerciseFeedbackCard(
                                question = currentQuestion,
                                isCorrect = isCorrect,
                                onSpeak = onSpeak,
                                srsResult = currentSrsResult
                            )
                        }
                    }

                    // 5. ACTION BUTTON (Comprobar / Siguiente)
                    item {
                        Spacer(modifier = Modifier.height(6.dp))
                        if (!isSubmitted) {
                            val canSubmit = when (currentQuestion.type) {
                                ModularExerciseType.MULTIPLE_CHOICE,
                                ModularExerciseType.IMAGE_VOCAB_MATCHING -> selectedOptionIndex != null
                                ModularExerciseType.FILL_IN_THE_BLANK,
                                ModularExerciseType.KEYWORD_TRANSFORMATION -> fillBlankInput.isNotBlank()
                            }

                            Button(
                                onClick = {
                                    isSubmitted = true
                                    val isCorrect = checkUserAnswer(currentQuestion, selectedOptionIndex, fillBlankInput)
                                    isAnswerCorrect = isCorrect
                                    if (isCorrect) {
                                        score++
                                        comboStreak++
                                        showCelebration = true
                                    } else {
                                        comboStreak = 0
                                    }
                                    val responseTime = (System.currentTimeMillis() - questionStartTime).coerceAtLeast(100L)
                                    viewModel.processExerciseAnswerSrs(
                                        question = currentQuestion,
                                        isCorrect = isCorrect,
                                        usedHint = usedHint,
                                        responseTimeMs = responseTime
                                    ) { eval ->
                                        currentSrsResult = eval
                                        sessionSrsUpdatesCount++
                                    }
                                },
                                enabled = canSubmit,
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = BrandBlue),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp)
                                    .testTag("verify_answer_btn")
                            ) {
                                Icon(Icons.Default.CheckCircleOutline, contentDescription = null)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Comprobar Respuesta", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                            }
                        } else {
                            Button(
                                onClick = {
                                    showCelebration = false
                                    if (currentIndex < questions.size - 1) {
                                        currentIndex++
                                        resetQuestionState()
                                    } else {
                                        isCompleted = true
                                    }
                                },
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = BrandBlue),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp)
                                    .testTag("next_exercise_btn")
                            ) {
                                Text(
                                    text = if (currentIndex < questions.size - 1) "Siguiente Ejercicio →" else "Ver Resultados y XP 🏆",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }

            // Confetti Celebration Overlay
            CelebrationConfettiOverlay(visible = showCelebration)
        }
    }
}

/**
 * Intelligent Answer Verification Helper
 */
fun checkUserAnswer(
    question: ModularExerciseQuestion,
    selectedOptionIndex: Int?,
    userInput: String
): Boolean {
    return when (question.type) {
        ModularExerciseType.MULTIPLE_CHOICE -> {
            if (selectedOptionIndex == null) return false
            val chosen = question.options.getOrNull(selectedOptionIndex) ?: return false
            chosen.equals(question.correctAnswer, ignoreCase = true) ||
                question.acceptedAlternatives.any { it.equals(chosen, ignoreCase = true) }
        }
        ModularExerciseType.FILL_IN_THE_BLANK -> {
            val clean = userInput.trim().lowercase()
            val correctClean = question.correctAnswer.trim().lowercase()
            clean == correctClean || question.acceptedAlternatives.any { it.trim().lowercase() == clean }
        }
        ModularExerciseType.IMAGE_VOCAB_MATCHING -> {
            if (selectedOptionIndex == null) return false
            val chosen = question.options.getOrNull(selectedOptionIndex) ?: return false
            chosen.equals(question.correctAnswer, ignoreCase = true) ||
                question.acceptedAlternatives.any { it.equals(chosen, ignoreCase = true) }
        }
        ModularExerciseType.KEYWORD_TRANSFORMATION -> {
            val clean = userInput.trim().lowercase().replace(Regex("[.,!?;]"), "")
            val correctClean = question.correctAnswer.trim().lowercase().replace(Regex("[.,!?;]"), "")
            clean == correctClean || question.acceptedAlternatives.any {
                it.trim().lowercase().replace(Regex("[.,!?;]"), "") == clean
            }
        }
    }
}
