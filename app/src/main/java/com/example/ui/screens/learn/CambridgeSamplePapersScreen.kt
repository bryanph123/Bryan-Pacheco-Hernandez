package com.example.ui.screens.learn

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.remote.CambridgeSamplePapersData
import com.example.data.remote.YleExamLevel
import com.example.data.remote.YleExamVolume
import com.example.data.remote.YleQuestionItem
import com.example.data.remote.YleSkill
import com.example.ui.theme.*
import com.example.ui.viewmodels.MainViewModel

@Composable
fun CambridgeSamplePapersScreen(
    viewModel: MainViewModel,
    onBack: () -> Unit,
    onSpeak: (String, Boolean) -> Unit
) {
    var selectedVolume by remember { mutableStateOf(YleExamVolume.VOLUME_2) }
    var selectedExamLevel by remember { mutableStateOf(YleExamLevel.STARTERS) }
    var selectedSkill by remember { mutableStateOf(YleSkill.LISTENING) }
    var showMarkingKey by remember { mutableStateOf(false) }

    // User answers map [questionId -> answer]
    val userAnswers = remember { mutableStateMapOf<String, String>() }
    // User verified status map [questionId -> Boolean (correct)]
    val answerStatus = remember { mutableStateMapOf<String, Boolean>() }

    val currentQuestions = remember(selectedVolume, selectedExamLevel, selectedSkill) {
        when (selectedVolume) {
            YleExamVolume.VOLUME_1 -> {
                when (selectedSkill) {
                    YleSkill.LISTENING -> when (selectedExamLevel) {
                        YleExamLevel.STARTERS -> CambridgeSamplePapersData.startersListeningV1
                        YleExamLevel.MOVERS -> CambridgeSamplePapersData.moversListeningV1
                        YleExamLevel.FLYERS -> CambridgeSamplePapersData.flyersListeningV1
                    }
                    YleSkill.READING_WRITING -> when (selectedExamLevel) {
                        YleExamLevel.STARTERS -> CambridgeSamplePapersData.startersReadingWritingV1
                        YleExamLevel.MOVERS -> CambridgeSamplePapersData.moversReadingWritingV1
                        YleExamLevel.FLYERS -> CambridgeSamplePapersData.flyersReadingWritingV1
                    }
                    YleSkill.SPEAKING -> CambridgeSamplePapersData.speakingSimulationsV1
                }
            }
            YleExamVolume.VOLUME_2 -> {
                when (selectedSkill) {
                    YleSkill.LISTENING -> when (selectedExamLevel) {
                        YleExamLevel.STARTERS -> CambridgeSamplePapersData.startersListeningV2
                        YleExamLevel.MOVERS -> CambridgeSamplePapersData.moversListeningV2
                        YleExamLevel.FLYERS -> CambridgeSamplePapersData.flyersListeningV2
                    }
                    YleSkill.READING_WRITING -> when (selectedExamLevel) {
                        YleExamLevel.STARTERS -> CambridgeSamplePapersData.startersReadingWritingV2
                        YleExamLevel.MOVERS -> CambridgeSamplePapersData.moversReadingWritingV2
                        YleExamLevel.FLYERS -> CambridgeSamplePapersData.flyersReadingWritingV2
                    }
                    YleSkill.SPEAKING -> CambridgeSamplePapersData.speakingSimulationsV2
                }
            }
        }
    }

    val correctCount = currentQuestions.count { answerStatus[it.id] == true }
    val totalQuestions = currentQuestions.size
    val shieldsEarned = if (totalQuestions > 0) ((correctCount.toFloat() / totalQuestions) * 5).toInt().coerceIn(1, 5) else 0

    Scaffold(
        topBar = {
            Surface(
                color = MaterialTheme.colorScheme.surface,
                shadowElevation = 3.dp
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = onBack,
                            modifier = Modifier.testTag("yle_back_btn")
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Regresar al Plan de Estudio"
                            )
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Cambridge English Qualifications",
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "${selectedVolume.title} • Starters, Movers & Flyers",
                                fontSize = 11.sp,
                                color = BrandBlue,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                        // Cambridge Shields Badge
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = WarningGold.copy(alpha = 0.15f),
                            modifier = Modifier.padding(end = 4.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Shield,
                                    contentDescription = null,
                                    tint = WarningGold,
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    text = "$shieldsEarned/5",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }

                    // Volume Selection Row (Vol 1 vs Vol 2)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 4.dp)
                            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f), RoundedCornerShape(10.dp))
                            .padding(3.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        YleExamVolume.values().forEach { volume ->
                            val isVolSelected = selectedVolume == volume
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = if (isVolSelected) BrandNavy else Color.Transparent,
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable {
                                        if (selectedVolume != volume) {
                                            selectedVolume = volume
                                            userAnswers.clear()
                                            answerStatus.clear()
                                        }
                                    }
                                    .testTag("tab_vol_${volume.badge.lowercase().replace(" ", "_")}")
                            ) {
                                Row(
                                    modifier = Modifier.padding(vertical = 6.dp),
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = if (volume == YleExamVolume.VOLUME_2) Icons.Default.AutoAwesome else Icons.Default.MenuBook,
                                        contentDescription = null,
                                        tint = if (isVolSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = volume.title,
                                        color = if (isVolSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                                        fontWeight = if (isVolSelected) FontWeight.Bold else FontWeight.Medium,
                                        fontSize = 12.sp
                                    )
                                }
                            }
                        }
                    }

                    // Level Selector Tabs (Starters, Movers, Flyers)
                    TabRow(
                        selectedTabIndex = selectedExamLevel.ordinal,
                        containerColor = MaterialTheme.colorScheme.surface,
                        contentColor = BrandBlue
                    ) {
                        YleExamLevel.values().forEach { level ->
                            Tab(
                                selected = selectedExamLevel == level,
                                onClick = {
                                    selectedExamLevel = level
                                    userAnswers.clear()
                                    answerStatus.clear()
                                },
                                text = {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text(
                                            text = level.title.replace("Pre A1 ", "").replace("A1 ", "").replace("A2 ", ""),
                                            fontWeight = if (selectedExamLevel == level) FontWeight.Bold else FontWeight.Normal,
                                            fontSize = 13.sp
                                        )
                                        Text(
                                            text = level.cefr,
                                            fontSize = 10.sp,
                                            color = if (selectedExamLevel == level) BrandBlue else MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                },
                                modifier = Modifier.testTag("tab_yle_${level.name.lowercase()}")
                            )
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
            // Level Hero Banner Image Card
            item {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                    modifier = Modifier.fillMaxWidth().height(160.dp)
                ) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        val bannerImage = if (selectedVolume == YleExamVolume.VOLUME_2) {
                            R.drawable.img_vol2_banner_1787928040545
                        } else {
                            selectedExamLevel.bannerResId
                        }
                        Image(
                            painter = painterResource(id = bannerImage),
                            contentDescription = selectedExamLevel.title,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.verticalGradient(
                                        colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.78f))
                                    )
                                )
                        )
                        Column(
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .padding(16.dp)
                        ) {
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = if (selectedVolume == YleExamVolume.VOLUME_2) BrandNavy else BrandBlue
                            ) {
                                Text(
                                    text = "OFFICIAL CAMBRIDGE ${selectedVolume.badge}",
                                    color = Color.White,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "${selectedExamLevel.title} (${selectedVolume.badge})",
                                color = Color.White,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 19.sp
                            )
                            Text(
                                text = "${selectedVolume.description} • Marco CEFR ${selectedExamLevel.cefr}",
                                color = Color.White.copy(alpha = 0.85f),
                                fontSize = 11.sp
                            )
                        }
                    }
                }
            }

            // Skill Filter Chips
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    YleSkill.values().forEach { skill ->
                        val isSelected = selectedSkill == skill
                        FilterChip(
                            selected = isSelected,
                            onClick = {
                                selectedSkill = skill
                                userAnswers.clear()
                                answerStatus.clear()
                            },
                            label = {
                                val shortName = when (skill) {
                                    YleSkill.LISTENING -> "Listening"
                                    YleSkill.READING_WRITING -> "Reading & Writing"
                                    YleSkill.SPEAKING -> "Speaking"
                                }
                                Text(
                                    text = shortName,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    fontSize = 12.sp
                                )
                            },
                            leadingIcon = {
                                val icon = when (skill) {
                                    YleSkill.LISTENING -> Icons.Default.Headphones
                                    YleSkill.READING_WRITING -> Icons.Default.EditNote
                                    YleSkill.SPEAKING -> Icons.Default.RecordVoiceOver
                                }
                                Icon(icon, contentDescription = null, modifier = Modifier.size(16.dp))
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = BrandBlueLight,
                                selectedLabelColor = BrandBlueDark,
                                selectedLeadingIconColor = BrandBlueDark
                            ),
                            modifier = Modifier.weight(1f).testTag("chip_skill_${skill.name.lowercase()}")
                        )
                    }
                }
            }

            // Info Bar with Progress & Marking Key Action
            item {
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = selectedSkill.title,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "Progreso: $correctCount / $totalQuestions correctas ($shieldsEarned/5 Escudos)",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Button(
                            onClick = { showMarkingKey = !showMarkingKey },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (showMarkingKey) BrandNavy else MaterialTheme.colorScheme.primary
                            ),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                            modifier = Modifier.height(34.dp).testTag("marking_key_toggle_btn")
                        ) {
                            Icon(
                                imageVector = if (showMarkingKey) Icons.Default.VisibilityOff else Icons.Default.Key,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (showMarkingKey) "Ocultar Claves" else "Claves Oficiales",
                                fontSize = 11.sp
                            )
                        }
                    }
                }
            }

            // Marking Key Explanation Box if opened
            if (showMarkingKey) {
                item {
                    OfficialMarkingKeyCard(
                        volume = selectedVolume,
                        level = selectedExamLevel,
                        skill = selectedSkill
                    )
                }
            }

            // Questions List
            if (currentQuestions.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No hay preguntas disponibles en esta sección.",
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            } else {
                items(currentQuestions, key = { it.id }) { question ->
                    YleQuestionCard(
                        question = question,
                        userAnswer = userAnswers[question.id] ?: "",
                        isCorrect = answerStatus[question.id],
                        showOfficialKey = showMarkingKey,
                        onAnswerChange = { userAnswers[question.id] = it },
                        onVerify = { ans ->
                            val isMatch = ans.trim().equals(question.correctAnswer.trim(), ignoreCase = true) ||
                                    question.acceptedAlternatives.any { alt ->
                                        ans.trim().equals(alt.trim(), ignoreCase = true)
                                    }
                            answerStatus[question.id] = isMatch
                        },
                        onSpeak = onSpeak
                    )
                }
            }

            // Bottom Exam Summary & CEFR Guidance Card
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = SuccessGreenLight.copy(alpha = 0.5f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Icon(Icons.Default.School, contentDescription = null, tint = SuccessGreenDark)
                            Text(
                                text = "Información del Examen Cambridge YLE (${selectedVolume.badge})",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = SuccessGreenDark
                            )
                        }
                        Text(
                            text = "Los exámenes de Cambridge English Qualifications para niños (Starters Pre-A1, Movers A1, Flyers A2) evalúan las 4 habilidades mediante tareas coloridas, contextuales e ilustradas. Todos los candidatos reciben un certificado oficial Cambridge que muestra de 1 a 5 escudos por cada competencia.",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun OfficialMarkingKeyCard(
    volume: YleExamVolume,
    level: YleExamLevel,
    skill: YleSkill
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = BrandBlueLight),
        modifier = Modifier.fillMaxWidth().testTag("official_marking_key_card")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Icon(Icons.Default.Verified, contentDescription = null, tint = BrandBlueDark)
                Text(
                    text = "Cambridge Official Marking Key (${volume.badge} - ${level.title})",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = BrandBlueDark
                )
            }
            Text(
                text = "Criterios de corrección oficiales de Cambridge Assessment English:",
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )

            val markingGuide = when (volume) {
                YleExamVolume.VOLUME_1 -> when (level) {
                    YleExamLevel.STARTERS -> when (skill) {
                        YleSkill.LISTENING -> "• Part 1: Lucy (Behind tree), Jill (Feeding ducks), Dan (Kite in red T-shirt), Ann (Reading on bench), Nick (New bike).\n• Part 2: 1. Wall (W-A-L-L), 2. Sun (S-U-N), 3. 15 / fifteen, 4. Tiger (T-I-G-E-R), 5. 7 / seven.\n• Part 3: 1. B, 2. A, 3. A, 4. B, 5. C."
                        YleSkill.READING_WRITING -> "• Part 1: 1. ✘ (motorbike), 2. ✔ (clock), 3. ✔ (shells), 4. ✘ (shoes), 5. ✔ (chairs).\n• Part 2: 1. yes, 2. yes, 3. no, 4. yes, 5. yes.\n• Part 3: 1. duck, 2. mouse, 3. hippo, 4. monkey, 5. chicken.\n• Part 4: 1. spiders, 2. legs, 3. tail, 4. trees, 5. sand."
                        YleSkill.SPEAKING -> "• Part 1: Point to scene objects and place object cards (e.g., robot on red chair).\n• Part 2: Answer 'What's this?' and color/quantity questions.\n• Part 3: Object card questions.\n• Part 4: Personal questions (age, family, friends, hobbies)."
                    }
                    YleExamLevel.MOVERS -> when (skill) {
                        YleSkill.LISTENING -> "• Part 1: Nick (carrying cake), Ben (playing with toy truck on mat).\n• Part 2: 1. Hill (H-I-L-L), 2. 89, 3. parrots, 4. café, 5. burgers.\n• Part 4: 1. B, 3. A (forest)."
                        YleSkill.READING_WRITING -> "• Part 1: 1. a nurse, 2. tea, 3. a city, 4. a sandwich, 5. a field.\n• Part 4: 1. than, 2. quickly, 3. out, 4. who, 5. swim."
                        YleSkill.SPEAKING -> "• Find 4 differences (diver with boat vs ball; sunny vs cloudy).\n• Picture story 'Fred loves food'.\n• Odd-one-out and personal questions."
                    }
                    YleExamLevel.FLYERS -> when (skill) {
                        YleSkill.LISTENING -> "• Part 1: Richard (striped sweater drinking), Sally (laughing on bike).\n• Part 2: 1. stars, 2. moon, 3. torch, 4. DVDs, 5. Bailey (B-A-I-L-E-Y)."
                        YleSkill.READING_WRITING -> "• Part 1: 1. jam, 2. baseball, 3. postcards, 4. a journalist, 5. stamps."
                        YleSkill.SPEAKING -> "• Information Exchange: Sarah's favourite restaurant (Rainbows, Hill Street, pizza, cheap).\n• Picture Story 'The Brave Teacher'."
                    }
                }
                YleExamVolume.VOLUME_2 -> when (level) {
                    YleExamLevel.STARTERS -> when (skill) {
                        YleSkill.LISTENING -> "• Part 1: Grace (girl with red racket), Alice (girl on chair with tablet), Bill (boy in grey shorts), Matt (boy holding bread), Kim (girl running in pink shirt).\n• Part 2: 1. 12 / twelve, 2. D-U-C-K, 3. S-H-E-L-L (Shell Beach), 4. 20 / twenty, 5. L-O-R-R-Y (Mr Lorry).\n• Part 3: 1. B (Grandpa), 2. A (Guitar), 3. B (Next to TV), 4. A (Brown trousers), 5. C (Goats).\n• Part 4: 1. box -> green, 2. under lemons -> purple, 3. computer -> orange, 4. between watermelons -> blue, 5. board -> red."
                        YleSkill.READING_WRITING -> "• Part 1: 1. ✘ (mirror), 2. ✔ (ear), 3. ✘ (potatoes), 4. ✔ (sofa), 5. ✔ (helicopters).\n• Part 2: 1. no, 2. no, 3. yes, 4. yes, 5. yes.\n• Part 3: 1. paper, 2. ruler, 3. eraser, 4. pencil, 5. teacher.\n• Part 4: 1. eyes, 2. tail, 3. people, 4. day, 5. food.\n• Part 5: 1. table, 2. children / kids, 3. hat, 4. 3 / three, 5. singing / whistling."
                        YleSkill.SPEAKING -> "• Part 1: Beach scene pointing (sun, yellow hat) & object cards (green apple in front of birds).\n• Part 2: Scene questions (pink fish, man in jeep).\n• Part 3: Object cards (spider, pencil, milk, ruler, wardrobe).\n• Part 4: Personal questions (age, classroom, friends)."
                    }
                    YleExamLevel.MOVERS -> when (skill) {
                        YleSkill.LISTENING -> "• Part 1: Fred (purple socks playing guitar), Vicky (mother laughing), Jane (writing words), Mark (playing piano), Daisy (wearing hat dancing).\n• Part 2: 1. floor(s), 2. 4 / four, 3. dress, 4. friends, 5. (ill) women / women who were ill.\n• Part 3: 1. brother -> G (wall), 2. daughter -> C (aeroplanes), 3. sister -> A (flowers), 4. cousin -> H (laptop), 5. uncle -> F (penguins).\n• Part 4: 1. A (beard), 2. B (blue towel), 3. B (salad), 4. B (basement bike), 5. A (map).\n• Part 5: Island trees yellow, boat green, boy sweater purple, write 'HELLO' in bird beak, shell brown."
                        YleSkill.READING_WRITING -> "• Part 1: 1. a balcony, 2. a panda, 3. a helmet, 4. stairs, 5. a dolphin.\n• Part 2: 1. C, 2. C, 3. A, 4. B, 5. C, 6. B.\n• Part 3: 1. hospital, 2. cook, 3. hungry, 4. vegetables, 5. bowl. (6) Matt has some nice soup.\n• Part 4: 1. which, 2. at, 3. Most, 4. eating, 5. These.\n• Part 5: 1. take a coat, 2. hot and sunny, 3. black clouds, 4. camera, 5. big leaves, 6. home, 7. surprised.\n• Part 6: 1. a scarf / bag, 2. riding a motorbike, 3. feeding birds / waving, 4. boy and girl."
                        YleSkill.SPEAKING -> "• Find 4 differences (bedroom: laptop on bed vs floor, 2 fish vs 1 fish, cat on rug vs bed, lamp pink).\n• Picture story 'The Windy Day' (Charlie, Jack, Mum cinema tickets).\n• Odd-one-out and personal questions."
                    }
                    YleExamLevel.FLYERS -> when (skill) {
                        YleSkill.LISTENING -> "• Part 1: Emma (pink skirt volleyball), William (angry man pointing to ball), Oliver (boy pushing into sea), Daisy (girl with hat and net), Jack (boy with shell).\n• Part 2: 1. Princes (P-R-I-N-C-E-S), 2. library, 3. downstairs, 4. swing, 5. basement.\n• Part 3: 1. Mrs Cook -> H (castle), 2. Mrs West -> C (picnic), 3. Miss Richards -> A (horses), 4. Mr Bridges -> D (snow), 5. Mrs Hill -> E (swimming lake).\n• Part 4: 1. C (black shirt), 2. A (desk next to book), 3. C (1.30), 4. C (Michael), 5. B (plane).\n• Part 5: Rock penguin grey, octopus pink, write 'QUEEN' on boat, write 'GOLD' on chest, fish purple."
                        YleSkill.READING_WRITING -> "• Part 1: 1. caves, 2. an ambulance, 3. a dictionary, 4. a waiter, 5. a desert, 6. woods, 7. a dentist, 8. a postcard, 9. a motorway, 10. a tyre.\n• Part 2: 1. E, 2. A, 3. C, 4. H, 5. F.\n• Part 3: 1. felt, 2. key, 3. nicer, 4. bridge, 5. sure. (6) A new home for David.\n• Part 4: 1. means, 2. of, 3. eating, 4. than, 5. has, 6. to, 7. so, 8. where, 9. for, 10. do.\n• Part 5: 1. breakfast(s), 2. tiger, 3. climb down, 4. surprised, 5. outside the village, 6. afraid of, 7. kind.\n• Part 6: 1. at/in, 2. watching, 3. been, 4. this/next, 5. which/that.\n• Part 7: Story writing (circus, clown, bikes)."
                        YleSkill.SPEAKING -> "• Find 6 differences (boat: octopus, whale, duck on boat, helicopter left vs right, hat, boy shoes).\n• Information Exchange: George's castle (mountain, Black Castle, queen, 500 years) vs Grace's castle (forest, Silver Castle, artist, 1000 years).\n• Picture Story 'Grandma's busy day' (playing tennis, hill walking, riding horses, video games)."
                    }
                }
            }

            Text(
                text = markingGuide,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurface,
                lineHeight = 18.sp
            )
        }
    }
}

@Composable
fun YleQuestionCard(
    question: YleQuestionItem,
    userAnswer: String,
    isCorrect: Boolean?,
    showOfficialKey: Boolean,
    onAnswerChange: (String) -> Unit,
    onVerify: (String) -> Unit,
    onSpeak: (String, Boolean) -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    var showTranscript by remember { mutableStateOf(false) }

    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("question_card_${question.id}")
            .then(
                if (isCorrect == true) Modifier.border(1.5.dp, SuccessGreen, RoundedCornerShape(18.dp))
                else if (isCorrect == false) Modifier.border(1.5.dp, ErrorRed, RoundedCornerShape(18.dp))
                else Modifier
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Header Part Badge & Title
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = BrandBlueLight
                ) {
                    Text(
                        text = "Parte ${question.partNumber}",
                        color = BrandBlueDark,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }

                if (isCorrect != null) {
                    Surface(
                        shape = CircleShape,
                        color = if (isCorrect) SuccessGreenLight else ErrorRedLight
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = if (isCorrect) Icons.Default.CheckCircle else Icons.Default.Cancel,
                                contentDescription = null,
                                tint = if (isCorrect) SuccessGreenDark else ErrorRedDark,
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                text = if (isCorrect) "¡Correcto!" else "Incorrecto",
                                color = if (isCorrect) SuccessGreenDark else ErrorRedDark,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp
                            )
                        }
                    }
                }
            }

            Text(
                text = question.partTitle,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = question.instructions,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            // Audio Player Bar if audio is present
            if (question.audioTranscript != null) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            FilledTonalIconButton(
                                onClick = {
                                    val audioToRead = question.audioTranscript.replace("F:", "Female:").replace("M:", "Male:").replace("Fch:", "Girl:").replace("Mch:", "Boy:")
                                    onSpeak(audioToRead, false)
                                },
                                modifier = Modifier.size(34.dp).testTag("play_audio_${question.id}")
                            ) {
                                Icon(Icons.AutoMirrored.Filled.VolumeUp, contentDescription = "Reproducir Audio", tint = BrandBlue)
                            }
                            Text(
                                text = "Audio de Examen (TTS)",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        TextButton(
                            onClick = { showTranscript = !showTranscript },
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                            modifier = Modifier.height(30.dp)
                        ) {
                            Text(
                                text = if (showTranscript) "Ocultar Guion" else "Ver Guion (Tapescript)",
                                fontSize = 11.sp
                            )
                        }
                    }
                }

                // Transcript Box
                AnimatedVisibility(visible = showTranscript, enter = fadeIn(), exit = fadeOut()) {
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text(
                                text = "Cambridge Tapescript:",
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                                color = BrandNavy
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = question.audioTranscript,
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurface,
                                lineHeight = 16.sp
                            )
                        }
                    }
                }
            }

            // Question prompt
            Text(
                text = question.questionText,
                fontWeight = FontWeight.SemiBold,
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurface
            )

            // Multiple Choice Options OR Free Text Field
            if (question.options.isNotEmpty()) {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    question.options.forEach { opt ->
                        val isSelected = userAnswer == opt
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = if (isSelected) BrandBlueLight else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                            border = if (isSelected) androidx.compose.foundation.BorderStroke(1.5.dp, BrandBlue) else null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onAnswerChange(opt)
                                    onVerify(opt)
                                }
                                .testTag("option_${question.id}_${opt.take(10).replace(" ", "_")}")
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 12.dp, vertical = 10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = isSelected,
                                    onClick = {
                                        onAnswerChange(opt)
                                        onVerify(opt)
                                    },
                                    colors = RadioButtonDefaults.colors(selectedColor = BrandBlue)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = opt,
                                    fontSize = 13.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) BrandBlueDark else MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }
            } else {
                // Text input
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = userAnswer,
                        onValueChange = { onAnswerChange(it) },
                        placeholder = { Text("Escribe tu respuesta aquí...", fontSize = 12.sp) },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                keyboardController?.hide()
                                onVerify(userAnswer)
                            }
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("input_${question.id}")
                    )
                    Button(
                        onClick = {
                            keyboardController?.hide()
                            onVerify(userAnswer)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = BrandBlue),
                        modifier = Modifier.testTag("verify_btn_${question.id}")
                    ) {
                        Text("Verificar", fontSize = 12.sp)
                    }
                }
            }

            // Explanation / Key reveal
            if (isCorrect != null || showOfficialKey) {
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = if (isCorrect == true) SuccessGreenLight.copy(alpha = 0.4f) else WarningGold.copy(alpha = 0.15f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = null,
                                tint = if (isCorrect == true) SuccessGreenDark else WarningAmberDark,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = "Respuesta oficial: ${question.correctAnswer}",
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                color = if (isCorrect == true) SuccessGreenDark else WarningAmberDark
                            )
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = question.explanation,
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}
