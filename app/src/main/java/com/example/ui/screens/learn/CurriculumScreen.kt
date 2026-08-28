package com.example.ui.screens.learn

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.entities.TopicEntity
import com.example.ui.components.AnimatedExerciseProgressBar
import com.example.ui.components.AnimatedExerciseProgressCard
import com.example.ui.components.CategoryIcon
import com.example.ui.components.StatusBadge
import com.example.ui.theme.*
import com.example.ui.viewmodels.LearnSubScreen
import com.example.ui.viewmodels.MainViewModel
import com.example.ui.viewmodels.PomodoroMode
import java.util.Locale

@Composable
fun CurriculumScreen(
    viewModel: MainViewModel,
    onSpeak: (String, Boolean) -> Unit
) {
    val topics by viewModel.filteredTopics.collectAsState()
    val allTopics by viewModel.allTopics.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val selectedStatus by viewModel.selectedStatus.collectAsState()
    val selectedLevel by viewModel.selectedLevel.collectAsState()
    val dueVocabCount by viewModel.dueVocabItems.collectAsState()
    val todayExercisesCompleted by viewModel.todayExercisesCompleted.collectAsState()
    val totalExercisesCompleted by viewModel.totalExercisesCompleted.collectAsState()
    val dailyExerciseGoal by viewModel.dailyExerciseGoal.collectAsState()

    var showAddCustomDialog by remember { mutableStateOf(false) }

    val levels = listOf(
        "A1" to "A1 (Principiante)",
        "A2" to "A2 (Básico)",
        "B1" to "B1 (Intermedio)",
        "B2" to "B2 (Intermedio Alto)",
        "TODOS" to "Todos (A1-B2)"
    )

    val categories = listOf(
        "Todas", "Gramática", "Vocabulario", "Listening",
        "Speaking", "Reading", "Writing", "Pronunciación", "Funciones Comunicativas"
    )

    val statuses = listOf(
        "TODOS" to "Todos",
        "NOT_STARTED" to "No iniciado",
        "IN_PROGRESS" to "En progreso",
        "COMPLETED" to "Completados",
        "MASTERED" to "Dominados"
    )

    // Progress calculation for selected level or all
    val relevantTopics = if (selectedLevel == "TODOS") allTopics else allTopics.filter { it.difficulty.startsWith(selectedLevel) }
    val completedCount = relevantTopics.count { it.status == "COMPLETED" || it.status == "MASTERED" }
    val progressPercent = if (relevantTopics.isNotEmpty()) (completedCount.toFloat() / relevantTopics.size * 100).toInt() else 0

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddCustomDialog = true },
                containerColor = BrandBlue,
                contentColor = Color.White,
                modifier = Modifier.testTag("add_custom_topic_fab")
            ) {
                Icon(Icons.Default.Add, contentDescription = "Agregar tema personalizado")
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background),
            contentPadding = PaddingValues(bottom = 80.dp)
        ) {
            // 0. Level Switcher Bar
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = "Nivel de Aprendizaje",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            levels.forEach { (levelKey, levelLabel) ->
                                val isSelected = levelKey == selectedLevel
                                Surface(
                                    shape = RoundedCornerShape(10.dp),
                                    color = if (isSelected) BrandBlue else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                                    modifier = Modifier
                                        .weight(1f)
                                        .clickable { viewModel.setSelectedLevel(levelKey) }
                                        .testTag("level_selector_$levelKey")
                                ) {
                                    Box(
                                        modifier = Modifier.padding(vertical = 10.dp, horizontal = 4.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = if (levelKey == "TODOS") "Todos" else levelKey,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 13.sp,
                                            color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // 0.5 Animated Exercise Progress Card (Dynamic Material 3 progress tracker)
            item {
                AnimatedExerciseProgressCard(
                    todayExercises = todayExercisesCompleted,
                    dailyGoal = dailyExerciseGoal,
                    levelCompletedCount = completedCount,
                    levelTotalCount = relevantTopics.size.coerceAtLeast(1),
                    selectedLevel = selectedLevel,
                    totalExercisesAllTime = totalExercisesCompleted,
                    onPracticeClick = {
                        val pendingTopic = relevantTopics.firstOrNull { it.status != "MASTERED" } ?: relevantTopics.firstOrNull()
                        if (pendingTopic != null) {
                            viewModel.navigateToLearnSubScreen(LearnSubScreen.TOPIC_DETAIL, topicId = pendingTopic.id)
                        } else {
                            viewModel.navigateToLearnSubScreen(LearnSubScreen.CAMBRIDGE_GUIDE)
                        }
                    },
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                )
            }

            // 0.6 Main Screen Live Functional Pomodoro Widget
            item {
                MainScreenPomodoroWidget(
                    viewModel = viewModel,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                )
            }

            // 1. Quick Study Hub Cards (SRS, Pomodoro, Exam, Alarms)
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = if (selectedLevel == "TODOS") "Herramientas de Estudio (A1 - B2)" else "Herramientas de Estudio $selectedLevel",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    // Cambridge AI Conversations Hero Card
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { viewModel.navigateToLearnSubScreen(LearnSubScreen.CONVERSATIONS) }
                            .testTag("quick_conversations_hero_card")
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                shape = CircleShape,
                                color = BrandCoral,
                                modifier = Modifier.size(46.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.Mic,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(14.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "Conversaciones Cambridge con IA",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp,
                                        color = Color.White
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Surface(
                                        color = BrandCoral,
                                        shape = RoundedCornerShape(4.dp)
                                    ) {
                                        Text(
                                            text = "VOZ & LIVE",
                                            color = Color.White,
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "Simulador de Speaking (Starters, Movers, Flyers, B1, B2) con evaluación en vivo",
                                    fontSize = 12.sp,
                                    color = Color.White.copy(alpha = 0.85f)
                                )
                            }
                            Icon(
                                imageVector = Icons.Default.ChevronRight,
                                contentDescription = null,
                                tint = Color.White
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Cambridge Official Guide Hero Card
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = BrandBlue),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { viewModel.navigateToLearnSubScreen(LearnSubScreen.CAMBRIDGE_GUIDE) }
                            .testTag("quick_cambridge_guide_hero")
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                shape = CircleShape,
                                color = Color.White.copy(alpha = 0.2f),
                                modifier = Modifier.size(44.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.AutoStories,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "Guía Cambridge A1–B2",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp,
                                        color = Color.White
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Surface(
                                        color = Color.White.copy(alpha = 0.25f),
                                        shape = RoundedCornerShape(4.dp)
                                    ) {
                                        Text(
                                            text = "OFICIAL",
                                            color = Color.White,
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                        )
                                    }
                                }
                                Text(
                                    text = "Gramática, vocabulario y ejercicios estilo Cambridge interactivos",
                                    fontSize = 12.sp,
                                    color = Color.White.copy(alpha = 0.85f)
                                )
                            }
                            Icon(
                                imageVector = Icons.Default.ChevronRight,
                                contentDescription = null,
                                tint = Color.White
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Audiobooks & Video Lessons Dual Hub Cards
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Audiobooks Card
                        QuickToolCard(
                            title = "Audiolibros",
                            subtitle = "Lectura & Audio TTS",
                            icon = Icons.Default.Headphones,
                            badgeColor = BrandPurple,
                            modifier = Modifier
                                .weight(1f)
                                .testTag("quick_audiobooks_btn"),
                            onClick = { viewModel.navigateToLearnSubScreen(LearnSubScreen.AUDIOBOOKS) }
                        )

                        // Video Lessons Card
                        QuickToolCard(
                            title = "Videoclases",
                            subtitle = "Masterclasses HD",
                            icon = Icons.Default.SmartDisplay,
                            badgeColor = BrandCoral,
                            modifier = Modifier
                                .weight(1f)
                                .testTag("quick_video_lessons_btn"),
                            onClick = { viewModel.navigateToLearnSubScreen(LearnSubScreen.VIDEO_LESSONS) }
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // SRS Flashcards Card
                        QuickToolCard(
                            title = "Repaso SRS",
                            subtitle = if (dueVocabCount.isNotEmpty()) "${dueVocabCount.size} listas hoy" else "Al día",
                            icon = Icons.Default.Style,
                            badgeColor = BrandCoral,
                            modifier = Modifier
                                .weight(1f)
                                .testTag("quick_srs_btn"),
                            onClick = { viewModel.navigateToLearnSubScreen(LearnSubScreen.SRS_FLASHCARDS) }
                        )

                        // Pomodoro Timer Card
                        QuickToolCard(
                            title = "Pomodoro",
                            subtitle = "25 min foco",
                            icon = Icons.Default.Timer,
                            badgeColor = BrandBlue,
                            modifier = Modifier
                                .weight(1f)
                                .testTag("quick_pomodoro_btn"),
                            onClick = { viewModel.navigateToLearnSubScreen(LearnSubScreen.POMODORO) }
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Exam Simulation Card
                        QuickToolCard(
                            title = when (selectedLevel) {
                                "A1" -> "Test A1"
                                "A2" -> "Simulacro A2"
                                "B1" -> "Simulacro B1"
                                else -> "Simulacro B2"
                            },
                            subtitle = when (selectedLevel) {
                                "A1" -> "Fundamentos A1"
                                "A2" -> "Cambridge KET"
                                "B1" -> "Cambridge PET"
                                else -> "Cambridge First"
                            },
                            icon = Icons.Default.FactCheck,
                            badgeColor = Color(0xFF8B5CF6),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("quick_exam_btn"),
                            onClick = { viewModel.navigateToLearnSubScreen(LearnSubScreen.EXAM_SIMULATION) }
                        )

                        // Alarms / Discipline Card
                        QuickToolCard(
                            title = "Alarmas y Avisos",
                            subtitle = "Protector de racha",
                            icon = Icons.Default.Alarm,
                            badgeColor = WarningAmber,
                            modifier = Modifier
                                .weight(1f)
                                .testTag("quick_alarms_btn"),
                            onClick = { viewModel.navigateToLearnSubScreen(LearnSubScreen.ALARMS) }
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Cambridge Sample Papers Volume 1 Card
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.85f)
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { viewModel.navigateToLearnSubScreen(LearnSubScreen.CAMBRIDGE_SAMPLE_PAPERS) }
                            .testTag("quick_cambridge_sample_papers_card")
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Surface(
                                shape = CircleShape,
                                color = BrandNavy,
                                modifier = Modifier.size(42.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.MenuBook,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(22.dp)
                                    )
                                }
                            }
                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                    Text(
                                        text = "Sample Papers Vol. 1 & 2 (YLE)",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        color = MaterialTheme.colorScheme.onTertiaryContainer
                                    )
                                    Surface(
                                        color = BrandNavy,
                                        shape = RoundedCornerShape(4.dp)
                                    ) {
                                        Text(
                                            text = "EXÁMENES",
                                            color = Color.White,
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                        )
                                    }
                                }
                                Text(
                                    text = "Starters (Pre A1), Movers (A1) y Flyers (A2): Listening, Reading & Speaking interactivo",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onTertiaryContainer.copy(alpha = 0.85f)
                                )
                            }
                            Icon(
                                imageVector = Icons.Default.ChevronRight,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onTertiaryContainer
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Espacios Integrados en Línea Card
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.7f)
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { viewModel.navigateToLearnSubScreen(LearnSubScreen.INTEGRATED_SPACES) }
                            .testTag("quick_integrated_spaces_card")
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Surface(
                                shape = CircleShape,
                                color = BrandBlue,
                                modifier = Modifier.size(40.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.Public,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(22.dp)
                                    )
                                }
                            }
                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                    Text(
                                        text = "Espacios Integrados en Línea",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                    Surface(
                                        color = BrandBlue,
                                        shape = RoundedCornerShape(4.dp)
                                    ) {
                                        Text(
                                            text = "INTERNET",
                                            color = Color.White,
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                        )
                                    }
                                }
                                Text(
                                    text = "Simulador Cambridge, BBC 6-Minute, Write & Improve, Oxford Hub",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                                )
                            }
                            Icon(
                                imageVector = Icons.Default.ChevronRight,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }
                }
            }

            // 2. Global Progress Card
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = if (selectedLevel == "TODOS") "Progreso General (B1 & B2)" else "Progreso Temario $selectedLevel",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "$completedCount de ${relevantTopics.size} temas completados",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            Text(
                                text = "$progressPercent%",
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 20.sp,
                                color = BrandBlue
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        LinearProgressIndicator(
                            progress = { progressPercent / 100f },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp)),
                            color = BrandBlue,
                            trackColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    }
                }
            }

            // 3. Search Bar
            item {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { viewModel.setSearchQuery(it) },
                    placeholder = { Text("Buscar tema por nombre o contenido...", fontSize = 13.sp) },
                    leadingIcon = {
                        Icon(
                            Icons.Default.Search,
                            contentDescription = "Buscar",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { viewModel.setSearchQuery("") }) {
                                Icon(Icons.Default.Close, contentDescription = "Limpiar búsqueda")
                            }
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp)
                        .testTag("curriculum_search_input")
                )
            }

            // 4. Horizontal Category Filter Chips
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    categories.forEach { cat ->
                        val isSelected = cat == selectedCategory
                        FilterChip(
                            selected = isSelected,
                            onClick = { viewModel.setSelectedCategory(cat) },
                            label = { Text(cat, fontSize = 12.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = BrandBlueLight,
                                selectedLabelColor = BrandBlue
                            ),
                            modifier = Modifier.testTag("category_chip_$cat")
                        )
                    }
                }
            }

            // 5. Status Filter Chips
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    statuses.forEach { (statKey, statLabel) ->
                        val isSelected = statKey == selectedStatus
                        AssistChip(
                            onClick = { viewModel.setSelectedStatus(statKey) },
                            label = {
                                Text(
                                    text = statLabel,
                                    fontSize = 11.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                            },
                            colors = AssistChipDefaults.assistChipColors(
                                containerColor = if (isSelected) BrandBlue.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surface,
                                labelColor = if (isSelected) BrandBlue else MaterialTheme.colorScheme.onSurfaceVariant
                            ),
                            border = if (isSelected) AssistChipDefaults.assistChipBorder(true, borderColor = BrandBlue) else null,
                            modifier = Modifier.testTag("status_filter_$statKey")
                        )
                    }
                }
            }

            // 6. Section Header
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Temas (${topics.size})",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }

            // 7. Topics List
            if (topics.isEmpty()) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            Icons.Default.SearchOff,
                            contentDescription = null,
                            modifier = Modifier.size(48.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "No se encontraron temas con esos filtros",
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 14.sp
                        )
                    }
                }
            } else {
                items(topics, key = { it.id }) { topic ->
                    TopicItemCard(
                        topic = topic,
                        onClick = { viewModel.navigateToLearnSubScreen(LearnSubScreen.TOPIC_DETAIL, topic.id) },
                        onQuickToggleStatus = {
                            val newStatus = when (topic.status) {
                                "NOT_STARTED" -> "IN_PROGRESS"
                                "IN_PROGRESS" -> "COMPLETED"
                                "COMPLETED" -> "MASTERED"
                                else -> "NOT_STARTED"
                            }
                            viewModel.updateTopicStatus(topic.id, newStatus)
                        }
                    )
                }
            }
        }
    }

    // Modal to add custom topic
    if (showAddCustomDialog) {
        AddCustomTopicDialog(
            onDismiss = { showAddCustomDialog = false },
            onAdd = { title, titleEs, cat, group, exp, exEn, exEs ->
                val examples = if (exEn.isNotBlank()) listOf(exEn to exEs) else emptyList()
                viewModel.addCustomTopic(title, titleEs, cat, group, exp, examples)
                showAddCustomDialog = false
            }
        )
    }
}

@Composable
fun QuickToolCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    badgeColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 1.dp,
        modifier = modifier.clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(badgeColor.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = badgeColor,
                    modifier = Modifier.size(20.dp)
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = subtitle,
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
fun TopicItemCard(
    topic: TopicEntity,
    onClick: () -> Unit,
    onQuickToggleStatus: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 5.dp)
            .clickable { onClick() }
            .testTag("topic_card_${topic.id}")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Category Icon
            CategoryIcon(category = topic.category)

            // Content
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = topic.title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                }

                Text(
                    text = topic.titleSpanish,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    StatusBadge(status = topic.status)

                    Text(
                        text = "• ${topic.difficulty}",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Text(
                        text = "• ~${topic.estimatedMinutes} min",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Quick State Toggle Button ("Ya lo sé / Toggle")
            IconButton(
                onClick = onQuickToggleStatus,
                modifier = Modifier.testTag("quick_toggle_status_${topic.id}")
            ) {
                val icon = when (topic.status) {
                    "MASTERED" -> Icons.Default.Star
                    "COMPLETED" -> Icons.Default.CheckCircle
                    "IN_PROGRESS" -> Icons.Default.HourglassTop
                    else -> Icons.Default.RadioButtonUnchecked
                }
                val iconColor = when (topic.status) {
                    "MASTERED" -> WarningAmber
                    "COMPLETED" -> SuccessGreen
                    "IN_PROGRESS" -> WarningAmber
                    else -> MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                }
                Icon(
                    imageVector = icon,
                    contentDescription = "Cambiar estado",
                    tint = iconColor
                )
            }
        }
    }
}

@Composable
fun AddCustomTopicDialog(
    onDismiss: () -> Unit,
    onAdd: (title: String, titleEs: String, category: String, moduleGroup: String, explanation: String, exEn: String, exEs: String) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var titleEs by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Gramática") }
    var explanation by remember { mutableStateOf("") }
    var exEn by remember { mutableStateOf("") }
    var exEs by remember { mutableStateOf("") }

    val categories = listOf("Gramática", "Vocabulario", "Listening", "Speaking", "Reading", "Writing", "Pronunciación", "Funciones Comunicativas")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Agregar Tema Personalizado B2", fontWeight = FontWeight.Bold, fontSize = 16.sp) },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Título en Inglés *") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = titleEs,
                    onValueChange = { titleEs = it },
                    label = { Text("Título en Español *") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = explanation,
                    onValueChange = { explanation = it },
                    label = { Text("Explicación Teórica") },
                    minLines = 2,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = exEn,
                    onValueChange = { exEn = it },
                    label = { Text("Ejemplo en Inglés") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = exEs,
                    onValueChange = { exEs = it },
                    label = { Text("Traducción del Ejemplo") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (title.isNotBlank() && titleEs.isNotBlank()) {
                        onAdd(title, titleEs, category, "Mis Temas Personalizados", explanation, exEn, exEs)
                    }
                },
                enabled = title.isNotBlank() && titleEs.isNotBlank()
            ) {
                Text("Guardar Tema")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreenPomodoroWidget(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val pomodoroMode by viewModel.pomodoroMode.collectAsState()
    val isRunning by viewModel.isPomodoroRunning.collectAsState()
    val totalSeconds by viewModel.pomodoroTotalSeconds.collectAsState()
    val remainingSeconds by viewModel.pomodoroRemainingSeconds.collectAsState()
    val completedSessions by viewModel.pomodoroCompletedSessions.collectAsState()
    val linkedTopic by viewModel.pomodoroLinkedTopic.collectAsState()

    val minutes = remainingSeconds / 60
    val seconds = remainingSeconds % 60
    val timeFormatted = String.format(Locale.US, "%02d:%02d", minutes, seconds)
    val progress = if (totalSeconds > 0) (remainingSeconds.toFloat() / totalSeconds).coerceIn(0f, 1f) else 0f

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (pomodoroMode.isBreak) SuccessGreen.copy(alpha = 0.08f) else BrandBlue.copy(alpha = 0.08f)
        ),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (pomodoroMode.isBreak) SuccessGreen.copy(alpha = 0.3f) else BrandBlue.copy(alpha = 0.3f)
        ),
        modifier = modifier
            .fillMaxWidth()
            .testTag("main_screen_pomodoro_card")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            // Header Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = CircleShape,
                        color = if (pomodoroMode.isBreak) SuccessGreen else BrandBlue,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = if (pomodoroMode.isBreak) Icons.Default.Coffee else Icons.Default.Timer,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = if (pomodoroMode.isBreak) "Descanso Pomodoro" else "Temporizador Pomodoro",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = if (isRunning) "⏱️ En ejecución (Persistente)" else "Sesión de estudio / descanso",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                // Expand to Full Screen button
                TextButton(
                    onClick = { viewModel.navigateToLearnSubScreen(LearnSubScreen.POMODORO) },
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                    modifier = Modifier.testTag("pomodoro_expand_fullscreen_btn")
                ) {
                    Text("Ver Completo", fontSize = 12.sp, color = BrandBlue, fontWeight = FontWeight.SemiBold)
                    Icon(
                        imageVector = Icons.Default.OpenInFull,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp),
                        tint = BrandBlue
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Mode Selector Chips
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                FilterChip(
                    selected = pomodoroMode == PomodoroMode.STUDY_25,
                    onClick = { viewModel.setPomodoroMode(PomodoroMode.STUDY_25) },
                    label = { Text("Estudio 25m", fontSize = 11.sp) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = BrandBlue,
                        selectedLabelColor = Color.White
                    ),
                    modifier = Modifier.weight(1f)
                )
                FilterChip(
                    selected = pomodoroMode == PomodoroMode.SHORT_BREAK_5,
                    onClick = { viewModel.setPomodoroMode(PomodoroMode.SHORT_BREAK_5) },
                    label = { Text("Descanso 5m", fontSize = 11.sp) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = SuccessGreen,
                        selectedLabelColor = Color.White
                    ),
                    modifier = Modifier.weight(1f)
                )
                FilterChip(
                    selected = pomodoroMode == PomodoroMode.LONG_BREAK_15,
                    onClick = { viewModel.setPomodoroMode(PomodoroMode.LONG_BREAK_15) },
                    label = { Text("Descanso 15m", fontSize = 11.sp) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Color(0xFF0D9488),
                        selectedLabelColor = Color.White
                    ),
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Timer Digital Counter & Progress
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = timeFormatted,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (pomodoroMode.isBreak) SuccessGreen else BrandBlue,
                        modifier = Modifier.testTag("main_screen_pomodoro_timer_display")
                    )
                    if (linkedTopic != null) {
                        Text(
                            text = "📖 ${linkedTopic?.title}",
                            fontSize = 11.sp,
                            maxLines = 1,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    } else {
                        Text(
                            text = if (completedSessions > 0) "🎉 $completedSessions sesiones completadas hoy" else "💡 25m estudio + 5m descanso",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                // Controls: Reset & Play/Pause
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { viewModel.resetPomodoro() },
                        modifier = Modifier
                            .size(40.dp)
                            .background(MaterialTheme.colorScheme.surface, CircleShape)
                            .testTag("main_pomodoro_reset_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Replay,
                            contentDescription = "Reiniciar",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Button(
                        onClick = { viewModel.togglePomodoro() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (pomodoroMode.isBreak) SuccessGreen else BrandBlue
                        ),
                        shape = RoundedCornerShape(20.dp),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                        modifier = Modifier.testTag("main_pomodoro_toggle_btn")
                    ) {
                        Icon(
                            imageVector = if (isRunning) Icons.Default.Pause else Icons.Default.PlayArrow,
                            contentDescription = if (isRunning) "Pausar" else "Iniciar",
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (isRunning) "Pausar" else "Iniciar",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Progress Bar
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = if (pomodoroMode.isBreak) SuccessGreen else BrandBlue,
                trackColor = MaterialTheme.colorScheme.surfaceVariant
            )
        }
    }
}
