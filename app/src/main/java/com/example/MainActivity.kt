package com.example

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.AppHeader
import com.example.ui.screens.dictionary.DictionaryScreen
import com.example.ui.screens.learn.*
import com.example.ui.screens.profile.ProfileScreen
import com.example.ui.screens.stats.StatsScreen
import com.example.ui.screens.translate.TranslateScreen
import com.example.ui.theme.BrandBlue
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodels.LearnSubScreen
import com.example.ui.viewmodels.MainTab
import com.example.ui.viewmodels.MainViewModel
import com.example.util.AlarmScheduler
import com.example.util.NotificationHelper
import com.example.util.TtsManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private lateinit var ttsManager: TtsManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        ttsManager = TtsManager(applicationContext)

        // Asynchronously initialize notification channels and schedule active reminders on IO thread
        CoroutineScope(Dispatchers.IO).launch {
            NotificationHelper.createNotificationChannel(applicationContext)
            AlarmScheduler.rescheduleAllActiveReminders(applicationContext)
        }

        setContent {
            MyApplicationTheme {
                val context = LocalContext.current
                val mainViewModel: MainViewModel = viewModel()

                // Request POST_NOTIFICATIONS permission on Android 13+
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    val permissionLauncher = rememberLauncherForActivityResult(
                        contract = ActivityResultContracts.RequestPermission()
                    ) { _ -> }

                    LaunchedEffect(Unit) {
                        if (ContextCompat.checkSelfPermission(
                                context,
                                Manifest.permission.POST_NOTIFICATIONS
                            ) != PackageManager.PERMISSION_GRANTED
                        ) {
                            permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                        }
                    }
                }

                MainAppScaffold(
                    viewModel = mainViewModel,
                    onSpeak = { text, isSpanish ->
                        ttsManager.speak(text, isSpanish)
                    }
                )
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        ttsManager.shutdown()
    }
}

data class NavItem(
    val tab: MainTab,
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val testTag: String
)

@Composable
fun MainAppScaffold(
    viewModel: MainViewModel,
    onSpeak: (String, Boolean) -> Unit
) {
    val currentTab by viewModel.currentTab.collectAsState()
    val currentLearnSubScreen by viewModel.learnSubScreen.collectAsState()
    val userSettings by viewModel.userSettings.collectAsState()
    val todayStudyMinutes by viewModel.todayStudyMinutes.collectAsState()
    val todayExercisesCompleted by viewModel.todayExercisesCompleted.collectAsState()
    val dailyExerciseGoal by viewModel.dailyExerciseGoal.collectAsState()

    val navItems = listOf(
        NavItem(MainTab.LEARN, "Aprender", Icons.Filled.MenuBook, Icons.Outlined.MenuBook, "nav_tab_learn"),
        NavItem(MainTab.TRANSLATE, "Traducir", Icons.Filled.Translate, Icons.Outlined.Translate, "nav_tab_translate"),
        NavItem(MainTab.DICTIONARY, "Diccionario", Icons.Filled.AutoStories, Icons.Outlined.AutoStories, "nav_tab_dictionary"),
        NavItem(MainTab.STATS, "Progreso", Icons.Filled.BarChart, Icons.Outlined.BarChart, "nav_tab_stats"),
        NavItem(MainTab.PROFILE, "Mi Perfil", Icons.Filled.Person, Icons.Outlined.Person, "nav_tab_profile")
    )

    val streakDays = userSettings?.streakDays ?: 1
    val totalXp = userSettings?.totalXp ?: 340
    val dailyGoal = userSettings?.dailyGoalMinutes ?: 25
    val isOnline by viewModel.networkMonitor.isOnline.collectAsState()
    val networkType by viewModel.networkMonitor.networkType.collectAsState()

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        topBar = {
            // Show global streak/XP header on top level screens
            if (currentTab != MainTab.LEARN || currentLearnSubScreen == LearnSubScreen.TOPICS_LIST) {
                AppHeader(
                    streakDays = streakDays,
                    xp = totalXp,
                    dailyMinutesStudied = todayStudyMinutes,
                    dailyMinutesGoal = dailyGoal,
                    completedExercises = todayExercisesCompleted,
                    dailyExerciseGoal = dailyExerciseGoal,
                    isOnline = isOnline,
                    networkType = networkType,
                    onAlarmsClick = {
                        viewModel.setTab(MainTab.LEARN)
                        viewModel.navigateToLearnSubScreen(LearnSubScreen.ALARMS)
                    },
                    onNetworkClick = {
                        viewModel.refreshNetworkStatus()
                    }
                )
            }
        },
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 6.dp,
                modifier = Modifier.testTag("main_bottom_nav")
            ) {
                navItems.forEach { item ->
                    val isSelected = currentTab == item.tab
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = {
                            viewModel.setTab(item.tab)
                        },
                        icon = {
                            Icon(
                                imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                                contentDescription = item.label
                            )
                        },
                        label = {
                            Text(
                                text = item.label,
                                fontSize = 11.sp
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = BrandBlue,
                            selectedTextColor = BrandBlue,
                            indicatorColor = BrandBlue.copy(alpha = 0.15f)
                        ),
                        modifier = Modifier.testTag(item.testTag)
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentTab) {
                MainTab.LEARN -> {
                    when (currentLearnSubScreen) {
                        LearnSubScreen.TOPICS_LIST -> CurriculumScreen(viewModel, onSpeak)
                        LearnSubScreen.TOPIC_DETAIL -> TopicDetailScreen(viewModel, onSpeak)
                        LearnSubScreen.EXERCISES -> ExercisesScreen(viewModel, onSpeak)
                        LearnSubScreen.SRS_FLASHCARDS -> SrsFlashcardsScreen(viewModel, onSpeak)
                        LearnSubScreen.POMODORO -> PomodoroScreen(viewModel)
                        LearnSubScreen.EXAM_SIMULATION -> ExamSimulationScreen(viewModel)
                        LearnSubScreen.ALARMS -> AlarmsScreen(viewModel)
                        LearnSubScreen.CAMBRIDGE_GUIDE -> CambridgeGuideScreen(viewModel, onSpeak)
                        LearnSubScreen.INTEGRATED_SPACES -> IntegratedSpacesScreen(
                            viewModel = viewModel,
                            onBack = { viewModel.navigateToLearnSubScreen(LearnSubScreen.TOPICS_LIST) }
                        )
                        LearnSubScreen.CAMBRIDGE_SAMPLE_PAPERS -> CambridgeSamplePapersScreen(
                            viewModel = viewModel,
                            onBack = { viewModel.navigateToLearnSubScreen(LearnSubScreen.TOPICS_LIST) },
                            onSpeak = onSpeak
                        )
                        LearnSubScreen.CONVERSATIONS -> ConversationScreen(
                            viewModel = viewModel,
                            onSpeak = onSpeak
                        )
                        LearnSubScreen.AUDIOBOOKS -> AudiobooksScreen(
                            viewModel = viewModel,
                            onSpeak = onSpeak
                        )
                        LearnSubScreen.VIDEO_LESSONS -> VideoLessonsScreen(
                            viewModel = viewModel,
                            onSpeak = onSpeak
                        )
                    }
                }
                MainTab.TRANSLATE -> TranslateScreen(viewModel, onSpeak)
                MainTab.DICTIONARY -> DictionaryScreen(viewModel, onSpeak)
                MainTab.STATS -> StatsScreen(viewModel)
                MainTab.PROFILE -> ProfileScreen(viewModel, onSpeak)
            }
        }
    }
}
