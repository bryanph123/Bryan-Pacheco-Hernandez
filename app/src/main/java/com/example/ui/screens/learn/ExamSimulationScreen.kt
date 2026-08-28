package com.example.ui.screens.learn

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
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
import com.example.ui.components.ScreenTopBar
import com.example.ui.theme.*
import com.example.ui.viewmodels.LearnSubScreen
import com.example.ui.viewmodels.MainViewModel
import kotlinx.coroutines.delay
import java.util.Locale

data class ExamItem(
    val partName: String,
    val instructions: String,
    val textContext: String,
    val keyWord: String? = null,
    val options: List<String> = emptyList(),
    val correctAnswer: String,
    val explanation: String
)

@Composable
fun ExamSimulationScreen(
    viewModel: MainViewModel
) {
    var selectedExamLevel by remember { mutableStateOf("A1") }

    val a1ExamItems = remember {
        listOf(
            ExamItem(
                partName = "Part 1: Basic Grammar & Pronouns",
                instructions = "Elige la opción correcta para completar la oración (A, B o C):",
                textContext = "Hello! My name is Lucas and I _____ from Argentina.",
                options = listOf("am", "is", "are"),
                correctAnswer = "am",
                explanation = "Con el pronombre de primera persona 'I' se usa 'am'."
            ),
            ExamItem(
                partName = "Part 2: Everyday Vocabulary",
                instructions = "Selecciona la palabra adecuada según la situación:",
                textContext = "At 7:30 in the morning, I always eat _____ with orange juice.",
                options = listOf("breakfast", "dinner", "lunch"),
                correctAnswer = "breakfast",
                explanation = "Por la mañana la primera comida del día es el desayuno (breakfast)."
            ),
            ExamItem(
                partName = "Part 3: Word Completion",
                instructions = "Escribe el posesivo correcto para una mujer (her / his / their):",
                textContext = "Maria is talking to _____ brother on the phone.",
                correctAnswer = "her",
                explanation = "El posesivo para tercera persona singular femenina (Maria) es 'her'."
            ),
            ExamItem(
                partName = "Part 4: Sentence Understanding",
                instructions = "Completa la frase con 'there is' o 'there are':",
                textContext = "In my classroom, __________ twenty desks for the students.",
                correctAnswer = "there are",
                explanation = "Para sustantivos en plural (twenty desks) se utiliza 'there are'."
            )
        )
    }

    val a2ExamItems = remember {
        listOf(
            ExamItem(
                partName = "Part 1: Signs & Short Notices (KET)",
                instructions = "Lee el anuncio y elige la opción que describe su significado:",
                textContext = "Notice: 'Swimming pool closed for cleaning from 1 PM to 3 PM.'\nWhat does it mean?",
                options = listOf(
                    "You cannot swim in the pool at 2 PM.",
                    "The pool is open all afternoon.",
                    "You must clean the pool before swimming."
                ),
                correctAnswer = "You cannot swim in the pool at 2 PM.",
                explanation = "La alberca está cerrada de 1 PM a 3 PM, por lo que a las 2 PM no se puede nadar."
            ),
            ExamItem(
                partName = "Part 2: Past Simple Verb Form",
                instructions = "Elige la forma pasada correcta para completar el diálogo:",
                textContext = "A: Did you enjoy the movie last night?\nB: Yes, I _____ it was fantastic!",
                options = listOf("thought", "think", "thinking", "thoughted"),
                correctAnswer = "thought",
                explanation = "El pasado irregular de 'think' es 'thought'."
            ),
            ExamItem(
                partName = "Part 3: Comparative Sentence Completion",
                instructions = "Escribe el comparativo correcto de 'fast':",
                textContext = "A train is usually __________ than a bicycle.",
                correctAnswer = "faster",
                explanation = "El adjetivo corto 'fast' añade '-er' para formar el comparativo: 'faster'."
            ),
            ExamItem(
                partName = "Part 4: Open Cloze (Prepositions & Modals)",
                instructions = "Escribe UNA sola palabra para completar la sugerencia:",
                textContext = "Why _____ we go to the Italian restaurant tonight?",
                correctAnswer = "don't",
                explanation = "La fórmula fija para sugerencias amables es 'Why don't we...?'."
            )
        )
    }

    val b1ExamItems = remember {
        listOf(
            ExamItem(
                partName = "Part 1: Multiple-Choice Notices & Signs",
                instructions = "Lee el texto y elige la opción correcta (A, B o C):",
                textContext = "Notice: 'Library computers are reserved for research only. No video games permitted.'\nWhat does this notice say?",
                options = listOf(
                    "You cannot play games on the computers.",
                    "You must ask permission to do research.",
                    "The library is closed for research today."
                ),
                correctAnswer = "You cannot play games on the computers.",
                explanation = "'No video games permitted' significa que los videojuegos están prohibidos en las computadoras."
            ),
            ExamItem(
                partName = "Part 2: Multiple-Choice Short Text",
                instructions = "Elige la palabra adecuada para completar la oración (A, B, C o D):",
                textContext = "Elena has decided to _____ up tennis because she wants to get fit.",
                options = listOf("take", "make", "get", "do"),
                correctAnswer = "take",
                explanation = "El phrasal verb 'take up' significa empezar un nuevo pasatiempo o deporte."
            ),
            ExamItem(
                partName = "Part 3: Open Cloze (Grammar Focus)",
                instructions = "Escribe UNA sola palabra para completar el espacio:",
                textContext = "If you want to pass the exam, you _____ study every day.",
                correctAnswer = "must",
                explanation = "Se usa el modal 'must' o 'should' para expresar obligación o necesidad en la oración condicional."
            ),
            ExamItem(
                partName = "Part 4: Sentence Transformation (PET)",
                instructions = "Completa la segunda frase para que signifique lo mismo que la primera (1 a 3 palabras):",
                textContext = "This laptop is cheaper than the desktop computer.\nThe desktop computer is ____________ than this laptop.",
                correctAnswer = "more expensive",
                explanation = "El opuesto de 'cheaper' es 'more expensive' en comparativo de superioridad."
            )
        )
    }

    val b2ExamItems = remember {
        listOf(
            ExamItem(
                partName = "Part 1: Multiple-Choice Cloze",
                instructions = "Para cada espacio, elige la opción correcta (A, B, C o D):",
                textContext = "Technological innovation has had a profound _____ on educational institutions globally.",
                options = listOf("impact", "affect", "result", "outcome"),
                correctAnswer = "impact",
                explanation = "La colocación exacta es 'have an impact on' (sustantivo 'impact' con preposición 'on')."
            ),
            ExamItem(
                partName = "Part 2: Open Cloze",
                instructions = "Escribe UNA sola palabra que encaje en el espacio:",
                textContext = "Unless teachers are provided _____ proper digital training, classroom software remains underutilized.",
                correctAnswer = "with",
                explanation = "El verbo pasivo 'provided' toma la preposición 'with' para introducir los recursos o herramientas otorgadas."
            ),
            ExamItem(
                partName = "Part 3: Word Formation",
                instructions = "Transforma la palabra en mayúsculas para completar la oración:",
                textContext = "The sudden _____ of internet connectivity disrupted the remote exam session.\n(DISCONNECT)",
                correctAnswer = "disconnection",
                explanation = "A partir de 'disconnect' formamos el sustantivo 'disconnection' precedido por el artículo 'The'."
            ),
            ExamItem(
                partName = "Part 4: Key Word Transformation",
                instructions = "Completa la segunda frase usando entre 2 y 5 palabras incluyendo la palabra clave:",
                textContext = "I haven't repaired a server for six months.\nSINCE\nIt is six months ________________ a server.",
                keyWord = "SINCE",
                correctAnswer = "since I repaired",
                explanation = "Estructura fija temporal: 'It is + periodo + since + Past Simple ('since I repaired')."
            )
        )
    }

    val examItems = when (selectedExamLevel) {
        "A1" -> a1ExamItems
        "A2" -> a2ExamItems
        "B1" -> b1ExamItems
        else -> b2ExamItems
    }

    var answers by remember(selectedExamLevel) { mutableStateOf(List(examItems.size) { "" }) }
    var remainingSeconds by remember(selectedExamLevel) { mutableIntStateOf(15 * 60) }
    var isSubmitted by remember(selectedExamLevel) { mutableStateOf(false) }

    // Timer countdown
    LaunchedEffect(isSubmitted) {
        while (!isSubmitted && remainingSeconds > 0) {
            delay(1000L)
            remainingSeconds--
        }
        if (remainingSeconds <= 0 && !isSubmitted) {
            isSubmitted = true
        }
    }

    val minutes = remainingSeconds / 60
    val seconds = remainingSeconds % 60
    val timerStr = String.format(Locale.US, "%02d:%02d", minutes, seconds)

    // Calculate score
    var correctCount = 0
    examItems.forEachIndexed { i, item ->
        val userAns = answers.getOrElse(i) { "" }.trim()
        if (userAns.equals(item.correctAnswer.trim(), ignoreCase = true) ||
            (item.options.isNotEmpty() && userAns.equals(item.correctAnswer, ignoreCase = true))
        ) {
            correctCount++
        }
    }
    val scorePercent = (correctCount.toFloat() / examItems.size * 100).toInt()
    val cambridgeScaleScore = 140 + (scorePercent * 0.5).toInt()
    val isPassed = scorePercent >= 60

    Scaffold(
        topBar = {
            ScreenTopBar(
                title = when (selectedExamLevel) {
                    "A1" -> "Test A1 (Fundamentos)"
                    "A2" -> "Simulación Cambridge A2 Key (KET)"
                    "B1" -> "Simulación Cambridge B1 (PET)"
                    else -> "Simulación Cambridge B2 First"
                },
                subtitle = "Reading & Use of English",
                onBack = { viewModel.navigateToLearnSubScreen(LearnSubScreen.TOPICS_LIST) },
                actions = {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = if (remainingSeconds < 180) ErrorRedLight else BrandBlueLight,
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                Icons.Default.Timer,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                                tint = if (remainingSeconds < 180) ErrorRed else BrandBlue
                            )
                            Text(
                                text = timerStr,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = if (remainingSeconds < 180) ErrorRed else BrandBlue
                            )
                        }
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Exam Header Banner with Level Selector
            item {
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            listOf("A1" to "A1 Starter", "A2" to "A2 Key", "B1" to "B1 PET", "B2" to "B2 First").forEach { (lvl, lbl) ->
                                FilterChip(
                                    selected = selectedExamLevel == lvl,
                                    onClick = { selectedExamLevel = lvl },
                                    label = { Text(lbl, fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = BrandBlueLight,
                                        selectedLabelColor = BrandBlue
                                    ),
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = when (selectedExamLevel) {
                                "A1" -> "Evaluación Diagnóstica: Nivel A1 (Beginner)"
                                "A2" -> "Cambridge English: A2 Key (KET) Mock"
                                "B1" -> "Cambridge English: B1 Preliminary (PET) Mock"
                                else -> "Cambridge English: B2 First (FCE) Mock"
                            },
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = when (selectedExamLevel) {
                                "A1" -> "Test de fundamentos básicos de gramática y vocabulario diario (CEFR 100-119 puntos)."
                                "A2" -> "Simulador oficial de Reading & Writing A2 Key (CEFR 120-139 puntos)."
                                "B1" -> "Simulador oficial de Reading & Writing B1 Preliminary (CEFR 140-159 puntos)."
                                else -> "Simulador oficial de Use of English B2 First (CEFR 160-179 puntos)."
                            },
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // Questions List
            itemsIndexed(examItems) { index, item ->
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = BrandBlueLight
                        ) {
                            Text(
                                text = item.partName,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = BrandBlue,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = item.instructions,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = item.textContext,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurface,
                            lineHeight = 20.sp
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        if (item.options.isNotEmpty()) {
                            // Multiple choice options
                            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                item.options.forEach { opt ->
                                    val isSelected = answers.getOrElse(index) { "" } == opt
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = if (isSelected) BrandBlueLight else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable(enabled = !isSubmitted) {
                                                val newAns = answers.toMutableList()
                                                newAns[index] = opt
                                                answers = newAns
                                            }
                                            .testTag("exam_opt_${index}_$opt")
                                    ) {
                                        Text(
                                            text = opt,
                                            fontSize = 13.sp,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                            color = if (isSelected) BrandBlue else MaterialTheme.colorScheme.onSurface,
                                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp)
                                        )
                                    }
                                }
                            }
                        } else {
                            // Text Input
                            OutlinedTextField(
                                value = answers.getOrElse(index) { "" },
                                onValueChange = { text ->
                                    if (!isSubmitted) {
                                        val newAns = answers.toMutableList()
                                        newAns[index] = text
                                        answers = newAns
                                    }
                                },
                                label = { Text("Escribe tu respuesta:") },
                                singleLine = true,
                                enabled = !isSubmitted,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("exam_input_$index")
                            )
                        }

                        // Explanation when submitted
                        if (isSubmitted) {
                            Spacer(modifier = Modifier.height(10.dp))
                            val isCorrect = answers.getOrElse(index) { "" }.trim().equals(item.correctAnswer.trim(), ignoreCase = true)
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = if (isCorrect) SuccessGreenLight else ErrorRedLight,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(10.dp)) {
                                    Text(
                                        text = if (isCorrect) "✓ Correcto" else "✗ Respuesta correcta: ${item.correctAnswer}",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp,
                                        color = if (isCorrect) SuccessGreen else ErrorRed
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = item.explanation,
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Results Card / Submit Button
            item {
                if (!isSubmitted) {
                    Button(
                        onClick = { isSubmitted = true },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = BrandBlue),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("submit_exam_btn")
                    ) {
                        Text("Calificar Examen Simulacro", fontSize = 15.sp)
                    }
                } else {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isPassed) SuccessGreenLight else ErrorRedLight
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = if (isPassed) "🎉 ¡Examen Aprobado (Nivel B2)!" else "Aún por debajo del corte B2",
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                color = if (isPassed) SuccessGreen else ErrorRed
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Aciertos: $correctCount / ${examItems.size} ($scorePercent%)",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "Puntuación estimada en Cambridge Scale: $cambridgeScaleScore / 190",
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(
                                onClick = { viewModel.navigateToLearnSubScreen(LearnSubScreen.TOPICS_LIST) },
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Text("Finalizar y Guardar")
                            }
                        }
                    }
                }
            }
        }
    }
}
