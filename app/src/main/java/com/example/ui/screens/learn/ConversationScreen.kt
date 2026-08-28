package com.example.ui.screens.learn

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.speech.RecognizerIntent
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.automirrored.filled.VolumeUp
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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import com.example.ui.viewmodels.ChatMessage
import com.example.ui.viewmodels.LearnSubScreen
import com.example.ui.viewmodels.MainViewModel
import kotlinx.coroutines.launch
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConversationScreen(
    viewModel: MainViewModel,
    onSpeak: (String, Boolean) -> Unit
) {
    val context = LocalContext.current
    val messages by viewModel.conversationMessages.collectAsState()
    val isLoading by viewModel.isConversationLoading.collectAsState()
    val selectedExam by viewModel.selectedConversationExam.collectAsState()
    val selectedPart by viewModel.selectedConversationPart.collectAsState()

    var inputText by remember { mutableStateOf("") }
    var autoSpeakAi by remember { mutableStateOf(true) }
    var showAssessmentInfo by remember { mutableStateOf(false) }
    var showVoiceHelpDialog by remember { mutableStateOf(false) }

    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    // Speech-to-text launcher
    val speechLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val spokenText = result.data
                ?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                ?.firstOrNull()
            if (!spokenText.isNullOrBlank()) {
                inputText = spokenText
                viewModel.sendConversationMessage(spokenText) { aiReply ->
                    if (autoSpeakAi) {
                        onSpeak(aiReply, false)
                    }
                }
                inputText = ""
            }
        }
    }

    fun startSpeechRecognition() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US")
            putExtra(RecognizerIntent.EXTRA_PROMPT, "Habla en inglés para responder al examinador Cambridge...")
        }
        try {
            speechLauncher.launch(intent)
        } catch (e: ActivityNotFoundException) {
            showVoiceHelpDialog = true
        } catch (e: Exception) {
            Toast.makeText(context, "No se pudo iniciar el reconocimiento por voz", Toast.LENGTH_SHORT).show()
        }
    }

    // Scroll to bottom on new message
    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    val examLevels = listOf(
        "Pre A1 Starters" to listOf("Speaking Part 1: Scene & Objects", "Speaking Part 2: Q&A", "Speaking Part 3: Object Cards", "Speaking Part 4: Personal Questions"),
        "A1 Movers" to listOf("Speaking Part 1: 4 Differences", "Speaking Part 2: Picture Story (Fred)", "Speaking Part 3: Odd-One-Out", "Speaking Part 4: Personal Routine"),
        "A2 Flyers" to listOf("Speaking Part 1: 6 Differences", "Speaking Part 2: Information Exchange", "Speaking Part 3: Picture Story (Brave Teacher)", "Speaking Part 4: Future & Past Plans"),
        "B1 Preliminary" to listOf("Speaking Part 1: Personal Interview", "Speaking Part 2: Extended Turn (Photo)", "Speaking Part 3: Collaborative Task", "Speaking Part 4: Discussion"),
        "B2 First" to listOf("Speaking Part 1: Conversation", "Speaking Part 2: 1-Minute Long Turn", "Speaking Part 3: Two-Way Collaborative Task", "Speaking Part 4: In-Depth Discussion")
    )

    // Suggested response chips based on exam level
    val suggestions = when (selectedExam) {
        "Pre A1 Starters" -> listOf(
            "My name is Alex and I am 10 years old.",
            "My favourite colour is blue and I love dogs.",
            "I can see a red bicycle under the tree."
        )
        "A1 Movers" -> listOf(
            "In the first picture it is sunny, but here it is raining.",
            "Last weekend I went to the park with my family.",
            "The helicopter is different because it is not an animal."
        )
        "A2 Flyers" -> listOf(
            "Where did Robert spend his summer holiday?",
            "I usually travel to school by bus every morning.",
            "If I could travel anywhere, I would visit Canada."
        )
        "B1 Preliminary" -> listOf(
            "In my free time, I really enjoy reading novels and swimming.",
            "I prefer studying in a quiet group so we can share ideas.",
            "Going to the cinema is exciting because of the big screen."
        )
        "B2 First" -> listOf(
            "In my opinion, technology fundamentally changes how we communicate.",
            "Educational systems should balance academic theory with practical skills.",
            "To tackle climate change, sustainable energy must be prioritized globally."
        )
        else -> listOf(
            "Yes, I completely agree with that perspective.",
            "Could you explain that in a different way, please?",
            "In my experience, practice is the key to fluency."
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Conversación AI Cambridge",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Surface(
                                color = BrandCoral,
                                shape = RoundedCornerShape(4.dp)
                            ) {
                                Text(
                                    text = "EN VIVO",
                                    color = Color.White,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                )
                            }
                        }
                        Text(
                            text = "$selectedExam • $selectedPart",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = { viewModel.navigateToLearnSubScreen(LearnSubScreen.TOPICS_LIST) },
                        modifier = Modifier.testTag("conversation_back_btn")
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Regresar")
                    }
                },
                actions = {
                    IconButton(
                        onClick = { autoSpeakAi = !autoSpeakAi },
                        modifier = Modifier.testTag("toggle_auto_speak_btn")
                    ) {
                        Icon(
                            imageVector = if (autoSpeakAi) Icons.AutoMirrored.Filled.VolumeUp else Icons.Filled.VolumeMute,
                            contentDescription = "Voz automática del examinador",
                            tint = if (autoSpeakAi) BrandBlue else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    IconButton(
                        onClick = { showAssessmentInfo = true },
                        modifier = Modifier.testTag("speaking_rubric_info_btn")
                    ) {
                        Icon(Icons.Filled.Info, contentDescription = "Rúbrica de Evaluación", tint = BrandBlue)
                    }
                    IconButton(
                        onClick = { viewModel.resetConversation() },
                        modifier = Modifier.testTag("reset_conversation_btn")
                    ) {
                        Icon(Icons.Filled.Refresh, contentDescription = "Reiniciar conversación")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            // Level Selector Row
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(examLevels) { (exam, parts) ->
                    val isSelected = selectedExam == exam
                    FilterChip(
                        selected = isSelected,
                        onClick = {
                            viewModel.setConversationExam(exam, parts.first())
                        },
                        label = {
                            Text(
                                text = exam,
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = BrandBlue,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }

            // Part Selector Row
            val currentParts = examLevels.find { it.first == selectedExam }?.second ?: emptyList()
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
                    .padding(horizontal = 12.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                items(currentParts) { part ->
                    val isSelected = selectedPart == part
                    SuggestionChip(
                        onClick = {
                            viewModel.setConversationExam(selectedExam, part)
                        },
                        label = {
                            Text(
                                text = part,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) BrandBlue else MaterialTheme.colorScheme.onSurface
                            )
                        },
                        border = SuggestionChipDefaults.suggestionChipBorder(
                            enabled = true,
                            borderColor = if (isSelected) BrandBlue else Color.Transparent
                        )
                    )
                }
            }

            // Message List
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(vertical = 12.dp)
            ) {
                items(messages, key = { it.id }) { msg ->
                    ConversationBubble(
                        message = msg,
                        onSpeak = { text -> onSpeak(text, false) }
                    )
                }

                if (isLoading) {
                    item {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            shape = RoundedCornerShape(16.dp),
                            border = BorderStroke(1.dp, BrandBlue.copy(alpha = 0.3f)),
                            modifier = Modifier.fillMaxWidth(0.88f)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(14.dp)
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(20.dp),
                                    strokeWidth = 2.5.dp,
                                    color = BrandBlue
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = "Examinador Cambridge evaluando...",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        color = BrandBlue
                                    )
                                    Text(
                                        text = "Analizando gramática, pronunciación y fluidez...",
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Quick Suggestions Carousel
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(vertical = 4.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 2.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "💡 Respuestas sugeridas para practicar:",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(horizontal = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(suggestions) { phrase ->
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = BrandBlue.copy(alpha = 0.08f),
                            border = BorderStroke(1.dp, BrandBlue.copy(alpha = 0.25f)),
                            modifier = Modifier.clickable {
                                inputText = phrase
                            }
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = phrase,
                                    fontSize = 12.sp,
                                    color = BrandBlue,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }
            }

            // Input Bar
            Surface(
                tonalElevation = 6.dp,
                color = MaterialTheme.colorScheme.surface,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Voice Mic Button (Direct speech input)
                    IconButton(
                        onClick = { startSpeechRecognition() },
                        modifier = Modifier
                            .size(46.dp)
                            .background(BrandBlue.copy(alpha = 0.14f), CircleShape)
                            .testTag("conversation_mic_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Mic,
                            contentDescription = "Hablar por micrófono",
                            tint = BrandBlue,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    // Text input field
                    OutlinedTextField(
                        value = inputText,
                        onValueChange = { inputText = it },
                        placeholder = { Text("Escribe o presiona el micro para hablar...", fontSize = 13.sp) },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("conversation_text_input"),
                        shape = RoundedCornerShape(24.dp),
                        maxLines = 3,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                        keyboardActions = KeyboardActions(
                            onSend = {
                                if (inputText.isNotBlank()) {
                                    val textToSend = inputText
                                    inputText = ""
                                    focusManager.clearFocus()
                                    viewModel.sendConversationMessage(textToSend) { aiReply ->
                                        if (autoSpeakAi) onSpeak(aiReply, false)
                                    }
                                }
                            }
                        )
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    // Send Button
                    IconButton(
                        onClick = {
                            if (inputText.isNotBlank()) {
                                val textToSend = inputText
                                inputText = ""
                                focusManager.clearFocus()
                                viewModel.sendConversationMessage(textToSend) { aiReply ->
                                    if (autoSpeakAi) onSpeak(aiReply, false)
                                }
                            }
                        },
                        enabled = inputText.isNotBlank() && !isLoading,
                        modifier = Modifier
                            .size(46.dp)
                            .background(
                                if (inputText.isNotBlank()) BrandBlue else MaterialTheme.colorScheme.surfaceVariant,
                                CircleShape
                            )
                            .testTag("conversation_send_btn")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Send,
                            contentDescription = "Enviar",
                            tint = if (inputText.isNotBlank()) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }

    // Speech Help Dialog (if recognizer not available)
    if (showVoiceHelpDialog) {
        AlertDialog(
            onDismissRequest = { showVoiceHelpDialog = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.MicOff, contentDescription = null, tint = BrandCoral)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Reconocimiento de Voz", fontWeight = FontWeight.Bold)
                }
            },
            text = {
                Text(
                    "El servicio de voz del sistema no está disponible en este dispositivo. Puedes escribir tu respuesta o seleccionar una de las frases sugeridas abajo para que el examinador AI te evalúe y responda por voz.",
                    fontSize = 13.sp
                )
            },
            confirmButton = {
                Button(onClick = { showVoiceHelpDialog = false }) {
                    Text("Entendido")
                }
            }
        )
    }

    // Cambridge Speaking Rubric Dialog
    if (showAssessmentInfo) {
        AlertDialog(
            onDismissRequest = { showAssessmentInfo = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.Verified, contentDescription = null, tint = BrandBlue)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Criterios Oficiales Cambridge Speaking", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "El examinador califica tu respuesta utilizando los 3 criterios oficiales:",
                        fontSize = 13.sp
                    )
                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Column(modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text("1. Vocabulario y Gramática:", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = BrandBlue)
                            Text("Uso adecuado de palabras temáticas y estructuras correctas para el nivel.", fontSize = 12.sp)

                            Text("2. Pronunciación e Intonación:", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = SuccessGreen)
                            Text("Claridad al pronunciar sonidos individuales, acento en palabras y ritmo.", fontSize = 12.sp)

                            Text("3. Interacción y Fluidez:", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = BrandCoral)
                            Text("Capacidad para responder preguntas sin pausas excesivas y pedir aclaraciones.", fontSize = 12.sp)
                        }
                    }
                    Text(
                        text = "🛡️ Escudos Cambridge: 5 escudos representan el máximo dominio del nivel.",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = { showAssessmentInfo = false }) {
                    Text("Entendido")
                }
            }
        )
    }
}

@Composable
fun ConversationBubble(
    message: ChatMessage,
    onSpeak: (String) -> Unit
) {
    val isUser = message.role == "user"

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = if (isUser) Alignment.End else Alignment.Start
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(0.92f),
            horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start,
            verticalAlignment = Alignment.Top
        ) {
            if (!isUser) {
                Surface(
                    shape = CircleShape,
                    color = BrandBlue,
                    modifier = Modifier.size(36.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.School,
                            contentDescription = "Examinador",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.width(8.dp))
            }

            Column(modifier = Modifier.weight(1f, fill = false)) {
                Surface(
                    shape = RoundedCornerShape(
                        topStart = 16.dp,
                        topEnd = 16.dp,
                        bottomStart = if (isUser) 16.dp else 4.dp,
                        bottomEnd = if (isUser) 4.dp else 16.dp
                    ),
                    color = if (isUser) BrandBlue else MaterialTheme.colorScheme.surface,
                    tonalElevation = if (isUser) 0.dp else 2.dp,
                    border = if (!isUser) BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant) else null
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        if (!isUser) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "Cambridge Examiner",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp,
                                        color = BrandBlue
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Surface(
                                        color = SuccessGreen.copy(alpha = 0.15f),
                                        shape = RoundedCornerShape(4.dp)
                                    ) {
                                        Text(
                                            text = "AI TUTOR",
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = SuccessGreen,
                                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                        )
                                    }
                                }
                                IconButton(
                                    onClick = { onSpeak(message.text) },
                                    modifier = Modifier.size(28.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                                        contentDescription = "Escuchar pronunciación",
                                        tint = BrandBlue,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                        }

                        Text(
                            text = message.text,
                            fontSize = 14.sp,
                            color = if (isUser) Color.White else MaterialTheme.colorScheme.onSurface,
                            lineHeight = 21.sp
                        )

                        // Shields score if present
                        if (message.scoreShields != null && message.scoreShields > 0) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Surface(
                                color = BrandGold.copy(alpha = 0.12f),
                                shape = RoundedCornerShape(8.dp),
                                border = BorderStroke(1.dp, BrandGold.copy(alpha = 0.3f)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "Evaluación Cambridge:",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                                        repeat(message.scoreShields.coerceIn(1, 5)) {
                                            Icon(
                                                imageVector = Icons.Default.Shield,
                                                contentDescription = null,
                                                tint = BrandGold,
                                                modifier = Modifier.size(14.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        // Examiner feedback & correction box
                        if (!message.feedback.isNullOrBlank()) {
                            Spacer(modifier = Modifier.height(6.dp))
                            Surface(
                                color = BrandBlue.copy(alpha = 0.08f),
                                shape = RoundedCornerShape(8.dp),
                                border = BorderStroke(1.dp, BrandBlue.copy(alpha = 0.2f)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(8.dp),
                                    verticalAlignment = Alignment.Top
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Lightbulb,
                                        contentDescription = null,
                                        tint = BrandBlue,
                                        modifier = Modifier.size(15.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = message.feedback,
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        lineHeight = 16.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }

            if (isUser) {
                Spacer(modifier = Modifier.width(8.dp))
                Surface(
                    shape = CircleShape,
                    color = BrandBlue.copy(alpha = 0.2f),
                    modifier = Modifier.size(36.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Candidato",
                            tint = BrandBlue,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}
