package com.example.ui.screens.learn

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Launch
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.remote.OnlineLearningSpace
import com.example.data.remote.OnlineLearningSpacesData
import com.example.ui.theme.*
import com.example.ui.viewmodels.MainViewModel

@Composable
fun IntegratedSpacesScreen(
    viewModel: MainViewModel,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val isOnline by viewModel.networkMonitor.isOnline.collectAsState()
    val networkType by viewModel.networkMonitor.networkType.collectAsState()

    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("Todos") }
    var selectedLevel by remember { mutableStateOf("TODOS") }
    var activeExerciseSpace by remember { mutableStateOf<OnlineLearningSpace?>(null) }
    var userExerciseInput by remember { mutableStateOf("") }
    var exerciseSubmitted by remember { mutableStateOf(false) }

    val categories = listOf("Todos", "Simulación & Examen Oficial", "Writing & Corrección por IA", "Listening & Vocabulario", "Comprensión Integral", "Diccionario & Colocaciones", "Vocabulario de Frecuencia")
    val levels = listOf("TODOS", "A1", "A2", "B1", "B2")

    val filteredSpaces = remember(searchQuery, selectedCategory, selectedLevel) {
        OnlineLearningSpacesData.spaces.filter { space ->
            val matchQuery = searchQuery.isBlank() ||
                    space.title.contains(searchQuery, ignoreCase = true) ||
                    space.organization.contains(searchQuery, ignoreCase = true) ||
                    space.description.contains(searchQuery, ignoreCase = true) ||
                    space.keyFeatures.any { it.contains(searchQuery, ignoreCase = true) }

            val matchCategory = selectedCategory == "Todos" || space.category == selectedCategory
            val matchLevel = selectedLevel == "TODOS" || space.targetLevels.contains(selectedLevel)

            matchQuery && matchCategory && matchLevel
        }
    }

    Scaffold(
        topBar = {
            Surface(
                color = MaterialTheme.colorScheme.surface,
                shadowElevation = 2.dp
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = onBack,
                            modifier = Modifier.testTag("spaces_back_btn")
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Regresar al Plan de Estudio"
                            )
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Espacios Integrados en Línea",
                                fontWeight = FontWeight.Bold,
                                fontSize = 17.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "Plataformas oficiales y laboratorios conectados",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        // Live Network Pill
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (isOnline) SuccessGreenLight else MaterialTheme.colorScheme.errorContainer,
                            modifier = Modifier.padding(end = 8.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(7.dp)
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(if (isOnline) SuccessGreen else MaterialTheme.colorScheme.error)
                                )
                                Text(
                                    text = if (isOnline) networkType else "Offline",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isOnline) SuccessGreen else MaterialTheme.colorScheme.error
                                )
                            }
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            contentPadding = PaddingValues(bottom = 90.dp)
        ) {
            // Internet Integration Banner
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isOnline) BrandBlue.copy(alpha = 0.08f) else MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.5f)
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (isOnline) BrandBlue else MaterialTheme.colorScheme.error),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (isOnline) Icons.Default.CloudSync else Icons.Default.CloudOff,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = if (isOnline) "Integración a Internet Activa" else "Modo Sin Conexión",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = if (isOnline) BrandBlue else MaterialTheme.colorScheme.error
                            )
                            Text(
                                text = if (isOnline)
                                    "Consultas de diccionario en tiempo real, traducción en la nube y acceso directo a laboratorios Cambridge y BBC."
                                else
                                    "Las funciones locales continúan disponibles. Conéctate a internet para interactuar con los espacios en línea.",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            // Search Bar
            item {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Buscar espacios, Cambridge, BBC, Oxford...", fontSize = 13.sp) },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Buscar") },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { searchQuery = "" }) {
                                Icon(Icons.Default.Close, contentDescription = "Limpiar")
                            }
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth().testTag("spaces_search_input")
                )
            }

            // Level Filter Pills
            item {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        text = "Filtrar por Nivel de Certificación:",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        levels.forEach { level ->
                            FilterChip(
                                selected = selectedLevel == level,
                                onClick = { selectedLevel = level },
                                label = { Text(level, fontSize = 12.sp) },
                                modifier = Modifier.testTag("filter_level_$level")
                            )
                        }
                    }
                }
            }

            // Category Filter Scroll
            item {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        text = "Categoría del Espacio:",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(categories) { cat ->
                            FilterChip(
                                selected = selectedCategory == cat,
                                onClick = { selectedCategory = cat },
                                label = { Text(cat, fontSize = 12.sp) }
                            )
                        }
                    }
                }
            }

            // Interactive Exercise Modal / Box if open
            if (activeExerciseSpace != null) {
                item {
                    val space = activeExerciseSpace!!
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "🎯 Práctica Rápida: ${space.title}",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                                IconButton(onClick = { activeExerciseSpace = null }) {
                                    Icon(Icons.Default.Close, contentDescription = "Cerrar")
                                }
                            }
                            Text(
                                text = space.quickExercisePrompt ?: "Escribe una oración o respuesta relacionada con este tema.",
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )

                            OutlinedTextField(
                                value = userExerciseInput,
                                onValueChange = { userExerciseInput = it; exerciseSubmitted = false },
                                placeholder = { Text("Escribe tu respuesta en inglés...") },
                                modifier = Modifier.fillMaxWidth(),
                                minLines = 2,
                                shape = RoundedCornerShape(10.dp)
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Button(
                                    onClick = {
                                        if (userExerciseInput.isNotBlank()) {
                                            exerciseSubmitted = true
                                            viewModel.recordExerciseScore(space.id, "INTEGRATED_SPACE", 10, 10)
                                        }
                                    },
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text("Verificar Respuesta")
                                }
                                OutlinedButton(
                                    onClick = {
                                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(space.webUrl))
                                        context.startActivity(intent)
                                    }
                                ) {
                                    Icon(Icons.AutoMirrored.Filled.Launch, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Abrir Espacio", fontSize = 12.sp)
                                }
                            }

                            if (exerciseSubmitted) {
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = SuccessGreenLight,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = "✅ ¡Excelente trabajo! Tu práctica ha sido registrada y sumó 10 XP a tu progreso.",
                                        fontSize = 12.sp,
                                        color = SuccessGreen,
                                        fontWeight = FontWeight.SemiBold,
                                        modifier = Modifier.padding(10.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Results count
            item {
                Text(
                    text = "Mostrando ${filteredSpaces.size} espacios integrados:",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            // Spaces List
            items(filteredSpaces, key = { it.id }) { space ->
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Header: Badge & Levels
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = BrandBlueLight
                            ) {
                                Text(
                                    text = space.badge,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = BrandBlue,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                space.targetLevels.forEach { lvl ->
                                    Surface(
                                        shape = RoundedCornerShape(4.dp),
                                        color = MaterialTheme.colorScheme.surfaceVariant
                                    ) {
                                        Text(
                                            text = lvl,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                        )
                                    }
                                }
                            }
                        }

                        // Title & Org
                        Text(
                            text = space.title,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Por: ${space.organization} • ${space.category}",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Medium
                        )

                        // Description
                        Text(
                            text = space.description,
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            lineHeight = 18.sp
                        )

                        // Key Features
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                                .padding(10.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = "Características y Herramientas:",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            space.keyFeatures.forEach { feature ->
                                Row(
                                    verticalAlignment = Alignment.Top,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Text(text = "•", fontSize = 12.sp, color = BrandBlue)
                                    Text(
                                        text = feature,
                                        fontSize = 12.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }

                        // Actions: Launch in Browser & In-App Practice
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = {
                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(space.webUrl))
                                    context.startActivity(intent)
                                },
                                modifier = Modifier.weight(1f).testTag("open_web_${space.id}")
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.Launch,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Abrir en Línea", fontSize = 13.sp)
                            }

                            if (space.quickExercisePrompt != null) {
                                OutlinedButton(
                                    onClick = {
                                        activeExerciseSpace = space
                                        userExerciseInput = ""
                                        exerciseSubmitted = false
                                    },
                                    modifier = Modifier.testTag("practice_space_${space.id}")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.PlayArrow,
                                        contentDescription = null,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Reto In-App", fontSize = 13.sp)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
