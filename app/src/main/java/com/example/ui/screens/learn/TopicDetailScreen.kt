package com.example.ui.screens.learn

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.entities.TopicEntity
import com.example.data.local.entities.TopicNoteEntity
import com.example.ui.components.AudioSpeakButton
import com.example.ui.components.CategoryIcon
import com.example.ui.components.ScreenTopBar
import com.example.ui.components.StatusBadge
import com.example.ui.theme.*
import com.example.ui.viewmodels.LearnSubScreen
import com.example.ui.viewmodels.MainViewModel
import org.json.JSONArray
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun TopicDetailScreen(
    viewModel: MainViewModel,
    onSpeak: (String, Boolean) -> Unit
) {
    val topic by viewModel.selectedTopic.collectAsState()
    val notes by viewModel.selectedTopicNotes.collectAsState()

    var selectedTabIndex by remember { mutableIntStateOf(0) }
    var newNoteText by remember { mutableStateOf("") }
    var showDeleteConfirm by remember { mutableStateOf(false) }

    if (topic == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    val currentTopic = topic!!
    val tabs = listOf("Teoría", "Ejemplos", "Errores Típicos", "Bitácora & Glosario")

    Scaffold(
        topBar = {
            ScreenTopBar(
                title = currentTopic.title,
                subtitle = currentTopic.category,
                onBack = { viewModel.navigateToLearnSubScreen(LearnSubScreen.TOPICS_LIST) },
                actions = {
                    if (currentTopic.isCustom) {
                        IconButton(onClick = { showDeleteConfirm = true }) {
                            Icon(Icons.Default.Delete, contentDescription = "Eliminar tema", tint = ErrorRed)
                        }
                    }
                }
            )
        },
        bottomBar = {
            Surface(
                color = MaterialTheme.colorScheme.surface,
                shadowElevation = 8.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Start Pomodoro linked to this topic
                    OutlinedButton(
                        onClick = { viewModel.navigateToLearnSubScreen(LearnSubScreen.POMODORO, currentTopic.id) },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("start_topic_pomodoro_btn")
                    ) {
                        Icon(Icons.Default.Timer, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Pomodoro", fontSize = 13.sp)
                    }

                    // Practice exercises for this topic
                    Button(
                        onClick = { viewModel.navigateToLearnSubScreen(LearnSubScreen.EXERCISES, currentTopic.id) },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = BrandBlue),
                        modifier = Modifier
                            .weight(1.2f)
                            .testTag("practice_exercises_btn")
                    ) {
                        Icon(Icons.Default.Quiz, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Practicar Ejercicios", fontSize = 13.sp)
                    }
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            // Header Info Card
            Card(
                shape = RoundedCornerShape(0.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = currentTopic.titleSpanish,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = currentTopic.moduleGroup,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Status Dropdown / Pills Selector
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        val statusList = listOf(
                            "NOT_STARTED" to "No iniciado",
                            "IN_PROGRESS" to "En progreso",
                            "COMPLETED" to "Completado",
                            "MASTERED" to "Dominado"
                        )
                        statusList.forEach { (statKey, label) ->
                            val isSelected = currentTopic.status == statKey
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = if (isSelected) BrandBlue else MaterialTheme.colorScheme.surfaceVariant,
                                modifier = Modifier
                                    .clickable { viewModel.updateTopicStatus(currentTopic.id, statKey) }
                                    .testTag("status_selector_$statKey")
                            ) {
                                Text(
                                    text = label,
                                    fontSize = 10.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }
                }
            }

            // Tab bar
            ScrollableTabRow(
                selectedTabIndex = selectedTabIndex,
                edgePadding = 16.dp,
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = BrandBlue
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = { Text(title, fontSize = 13.sp, fontWeight = if (selectedTabIndex == index) FontWeight.Bold else FontWeight.Normal) }
                    )
                }
            }

            // Tab Content
            when (selectedTabIndex) {
                0 -> TheoryTab(currentTopic, viewModel)
                1 -> ExamplesTab(currentTopic, onSpeak, viewModel)
                2 -> MistakesTab(currentTopic)
                3 -> NotesAndGlossaryTab(
                    topic = currentTopic,
                    notes = notes,
                    newNoteText = newNoteText,
                    onNoteChange = { newNoteText = it },
                    onAddNote = {
                        viewModel.addNoteToTopic(currentTopic.id, newNoteText)
                        newNoteText = ""
                    },
                    onSpeak = onSpeak
                )
            }
        }
    }

    if (showDeleteConfirm) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirm = false },
            title = { Text("Eliminar Tema") },
            text = { Text("¿Estás seguro de que deseas eliminar este tema personalizado?") },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.deleteCustomTopic(currentTopic.id)
                        showDeleteConfirm = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ErrorRed)
                ) {
                    Text("Eliminar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirm = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Composable
fun TheoryTab(topic: TopicEntity, viewModel: MainViewModel) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(Icons.Default.MenuBook, contentDescription = null, tint = BrandBlue)
                        Text(
                            text = "Explicación Conceptual B2",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = topic.explanation,
                        fontSize = 14.sp,
                        lineHeight = 22.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }

        item {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = BrandBlueLight.copy(alpha = 0.5f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "💡 Consejo para Hispanohablantes",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = BrandBlueDark
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Evita traducir palabra por palabra desde el español. Fíjate en la estructura completa del patrón gramatical en inglés y escucha el ritmo de la frase.",
                        fontSize = 13.sp,
                        lineHeight = 19.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }

        item {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "🎧 Práctica Multimedia Relacionada",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = BrandPurpleDark
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = BrandPurpleLight,
                            modifier = Modifier
                                .weight(1f)
                                .clickable { viewModel.navigateToLearnSubScreen(LearnSubScreen.AUDIOBOOKS) }
                        ) {
                            Row(
                                modifier = Modifier.padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(Icons.Default.Headphones, contentDescription = null, tint = BrandPurple, modifier = Modifier.size(20.dp))
                                Column {
                                    Text("Audiolibros", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = BrandPurpleDark)
                                    Text("Lectura guiada", fontSize = 10.sp, color = BrandPurpleDark.copy(alpha = 0.8f))
                                }
                            }
                        }

                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = BrandCoralLight,
                            modifier = Modifier
                                .weight(1f)
                                .clickable { viewModel.navigateToLearnSubScreen(LearnSubScreen.VIDEO_LESSONS) }
                        ) {
                            Row(
                                modifier = Modifier.padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(Icons.Default.SmartDisplay, contentDescription = null, tint = BrandCoral, modifier = Modifier.size(20.dp))
                                Column {
                                    Text("Videoclases", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = BrandCoralDark)
                                    Text("Masterclasses", fontSize = 10.sp, color = BrandCoralDark.copy(alpha = 0.8f))
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
fun ExamplesTab(
    topic: TopicEntity,
    onSpeak: (String, Boolean) -> Unit,
    viewModel: MainViewModel
) {
    val examples = remember(topic.examplesJson) {
        val list = mutableListOf<Pair<String, String>>()
        try {
            val arr = JSONArray(topic.examplesJson)
            for (i in 0 until arr.length()) {
                val obj = arr.getJSONObject(i)
                list.add(Pair(obj.getString("en"), obj.getString("es")))
            }
        } catch (_: Exception) {}
        list
    }

    var savedSnackbarMessage by remember { mutableStateOf<String?>(null) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        if (examples.isEmpty()) {
            item {
                Text(
                    text = "No hay ejemplos adicionales para este tema.",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 14.sp
                )
            }
        } else {
            items(examples) { (en, es) ->
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = en,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.weight(1f)
                            )
                            AudioSpeakButton(
                                text = en,
                                onSpeak = onSpeak
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = es,
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Save to SRS Button
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            TextButton(
                                onClick = {
                                    viewModel.reviewSrsCard(
                                        com.example.data.local.entities.SavedVocabItemEntity(
                                            id = "vocab_ex_" + UUID.randomUUID().toString(),
                                            sourceText = en,
                                            translation = es,
                                            sourceModule = "curriculum",
                                            linkedTopicId = topic.id
                                        ),
                                        3
                                    )
                                    savedSnackbarMessage = "¡Guardado en tus tarjetas SRS de repaso!"
                                },
                                modifier = Modifier.testTag("save_example_to_srs_btn")
                            ) {
                                Icon(Icons.Default.BookmarkAdd, contentDescription = null, modifier = Modifier.size(16.dp), tint = BrandCoral)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Guardar en SRS", fontSize = 12.sp, color = BrandCoral)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MistakesTab(topic: TopicEntity) {
    val mistakes = remember(topic.commonMistakesJson) {
        val list = mutableListOf<Pair<String, String>>()
        try {
            val arr = JSONArray(topic.commonMistakesJson)
            for (i in 0 until arr.length()) {
                val obj = arr.getJSONObject(i)
                list.add(Pair(obj.getString("error"), obj.getString("fix")))
            }
        } catch (_: Exception) {}
        list
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        if (mistakes.isEmpty()) {
            item {
                Text(
                    text = "No se registraron errores críticos específicos.",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 14.sp
                )
            }
        } else {
            items(mistakes) { (err, fix) ->
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(Icons.Default.Cancel, contentDescription = "Error común", tint = ErrorRed, modifier = Modifier.size(20.dp))
                            Text(
                                text = err,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 13.sp,
                                color = ErrorRed
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(Icons.Default.CheckCircle, contentDescription = "Forma correcta", tint = SuccessGreen, modifier = Modifier.size(20.dp))
                            Text(
                                text = fix,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = SuccessGreen
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun NotesAndGlossaryTab(
    topic: TopicEntity,
    notes: List<TopicNoteEntity>,
    newNoteText: String,
    onNoteChange: (String) -> Unit,
    onAddNote: () -> Unit,
    onSpeak: (String, Boolean) -> Unit
) {
    val glossary = remember(topic.miniGlossaryJson) {
        val list = mutableListOf<Pair<String, String>>()
        try {
            val arr = JSONArray(topic.miniGlossaryJson)
            for (i in 0 until arr.length()) {
                val obj = arr.getJSONObject(i)
                list.add(Pair(obj.getString("term"), obj.getString("def")))
            }
        } catch (_: Exception) {}
        list
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Mini Glossary Section
        if (glossary.isNotEmpty()) {
            item {
                Text(
                    text = "Mini-Glosario Clave",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
            items(glossary) { (term, def) ->
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = term,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = BrandBlue,
                                modifier = Modifier.weight(1f)
                            )
                            AudioSpeakButton(text = term, onSpeak = onSpeak, size = 32)
                        }
                        Text(
                            text = def,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        // Notes Input Section
        item {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Bitácora de Estudio Personal",
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(6.dp))
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    OutlinedTextField(
                        value = newNoteText,
                        onValueChange = onNoteChange,
                        placeholder = { Text("Escribe tus dudas, oraciones propias o apuntes clave...", fontSize = 13.sp) },
                        minLines = 2,
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("topic_note_input")
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = onAddNote,
                        enabled = newNoteText.isNotBlank(),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = BrandBlue),
                        modifier = Modifier
                            .align(Alignment.End)
                            .testTag("save_topic_note_btn")
                    ) {
                        Icon(Icons.Default.NoteAdd, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Guardar Apunte", fontSize = 12.sp)
                    }
                }
            }
        }

        // Existing Notes List
        if (notes.isNotEmpty()) {
            item {
                Text(
                    text = "Apuntes Guardados (${notes.size})",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
            items(notes, key = { it.id }) { note ->
                Card(
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Text(
                            text = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault()).format(Date(note.timestamp)),
                            fontSize = 10.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = note.noteContent,
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }
    }
}
