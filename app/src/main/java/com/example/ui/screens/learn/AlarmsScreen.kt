package com.example.ui.screens.learn

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
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
import com.example.data.local.entities.AlarmReminderEntity
import com.example.ui.components.ScreenTopBar
import com.example.ui.theme.*
import com.example.ui.viewmodels.LearnSubScreen
import com.example.ui.viewmodels.MainViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun AlarmsScreen(
    viewModel: MainViewModel
) {
    val reminders by viewModel.allReminders.collectAsState()
    val userSettings by viewModel.userSettings.collectAsState()
    val todayStudyMinutes by viewModel.todayStudyMinutes.collectAsState()
    val dueVocabItems by viewModel.dueVocabItems.collectAsState()

    val context = LocalContext.current

    var selectedReminderForTimeEdit by remember { mutableStateOf<AlarmReminderEntity?>(null) }
    var showAddReminderDialog by remember { mutableStateOf(false) }
    var testNotificationResult by remember { mutableStateOf<String?>(null) }

    val todayDateStr = remember {
        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
    }
    val hasCompletedToday = userSettings?.lastActiveDate == todayDateStr
    val streakDays = userSettings?.streakDays ?: 1
    val dailyGoal = userSettings?.dailyGoalMinutes ?: 25
    val dueCardsCount = dueVocabItems.size

    Scaffold(
        topBar = {
            ScreenTopBar(
                title = "Notificaciones y Alarmas Diarias",
                subtitle = "Protección de racha y recordatorios de inactividad",
                onBack = { viewModel.navigateToLearnSubScreen(LearnSubScreen.TOPICS_LIST) }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddReminderDialog = true },
                containerColor = BrandBlue,
                contentColor = Color.White,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.testTag("add_alarm_fab")
            ) {
                Icon(Icons.Default.AddAlarm, contentDescription = "Agregar Recordatorio")
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Live Daily Activity Status Banner
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (hasCompletedToday) SuccessGreenLight.copy(alpha = 0.6f) else WarningAmberLight
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(CircleShape)
                                    .background(if (hasCompletedToday) SuccessGreen else WarningAmber),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = if (hasCompletedToday) Icons.Default.CheckCircle else Icons.Default.Warning,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = if (hasCompletedToday) "¡Actividad de hoy al día!" else "¡Actividad diaria pendiente!",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp,
                                    color = if (hasCompletedToday) SuccessGreenDark else WarningAmberDark
                                )
                                Text(
                                    text = if (hasCompletedToday)
                                        "Ya registraste estudio hoy. Tu racha de $streakDays días está a salvo."
                                    else
                                        "Si no realizas actividades hoy, recibirás notificaciones para proteger tu racha de $streakDays días.",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        Divider(color = Color.Black.copy(alpha = 0.06f))

                        // Stats Quick Badges
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "$todayStudyMinutes / $dailyGoal min",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "Estudio Hoy",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "🔥 $streakDays días",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "Racha Actual",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "$dueCardsCount tarjetas",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = if (dueCardsCount > 0) WarningAmberDark else MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "SRS Pendientes",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }

            // Inactivity System Explanation Card
            item {
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
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
                                .background(BrandBlueLight),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.NotificationsActive, contentDescription = null, tint = BrandBlue)
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Protector Inteligente de Inactividad",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "El sistema programa alarmas en tu teléfono. Si no has hecho actividades al llegar la hora, te enviará una notificación para que no pierdas tu avance.",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            // Section Header: Recordatorios Configurados
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "RECORDATORIOS PROGRAMADOS",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = "${reminders.count { it.isEnabled }} activos",
                        fontSize = 12.sp,
                        color = BrandBlue,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            // Reminders List
            items(reminders, key = { it.id }) { reminder ->
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                modifier = Modifier.weight(1f),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(
                                            when (reminder.type) {
                                                "STREAK_WARNING" -> WarningAmberLight
                                                "SRS_REVIEW" -> BrandPurpleLight
                                                "STUCK_TOPIC" -> BrandBlueLight
                                                else -> SuccessGreenLight
                                            }
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = when (reminder.type) {
                                            "STREAK_WARNING" -> "🔥"
                                            "SRS_REVIEW" -> "🧠"
                                            "STUCK_TOPIC" -> "📚"
                                            else -> "⏰"
                                        },
                                        fontSize = 18.sp
                                    )
                                }

                                Column {
                                    Text(
                                        text = reminder.label,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = when (reminder.type) {
                                            "STREAK_WARNING" -> "Alerta si no hay actividad antes de finalizar el día"
                                            "DAILY_STUDY" -> "Aviso de práctica diaria de temas y ejercicios"
                                            "SRS_REVIEW" -> "Aviso matutino para repasar tarjetas pendientes"
                                            "STUCK_TOPIC" -> "Recordatorio para completar lecciones iniciadas"
                                            else -> "Recordatorio personalizado"
                                        },
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }

                            Switch(
                                checked = reminder.isEnabled,
                                onCheckedChange = { viewModel.toggleReminder(reminder) },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = BrandBlue,
                                    checkedTrackColor = BrandBlueLight
                                ),
                                modifier = Modifier.testTag("alarm_switch_${reminder.id}")
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Bottom row with time badge and edit button
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = if (reminder.isEnabled) BrandBlueLight else MaterialTheme.colorScheme.surfaceVariant
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Icon(
                                        Icons.Default.AccessTime,
                                        contentDescription = null,
                                        modifier = Modifier.size(14.dp),
                                        tint = if (reminder.isEnabled) BrandBlue else MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Text(
                                        text = "${reminder.timeString} hrs",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        color = if (reminder.isEnabled) BrandBlue else MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }

                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                TextButton(
                                    onClick = { selectedReminderForTimeEdit = reminder },
                                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                                ) {
                                    Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Cambiar hora", fontSize = 12.sp)
                                }

                                if (reminder.id.startsWith("reminder_")) {
                                    IconButton(
                                        onClick = { viewModel.deleteReminder(reminder) },
                                        modifier = Modifier.size(32.dp)
                                    ) {
                                        Icon(
                                            Icons.Default.DeleteOutline,
                                            contentDescription = "Eliminar",
                                            tint = ErrorRed,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Smart Test Inactivity Button
            item {
                Spacer(modifier = Modifier.height(6.dp))
                Button(
                    onClick = {
                        val result = viewModel.triggerInactivityCheckNow()
                        testNotificationResult = result
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = WarningAmberDark),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("test_inactivity_notification_btn")
                ) {
                    Icon(Icons.Default.NotificationsActive, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("⚡ Comprobar Inactividad y Notificar al Teléfono", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }
            }

            item {
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }

    // Dialog to change time
    if (selectedReminderForTimeEdit != null) {
        val currentReminder = selectedReminderForTimeEdit!!
        var hourInput by remember { mutableStateOf(currentReminder.timeString.split(":").getOrElse(0) { "20" }) }
        var minInput by remember { mutableStateOf(currentReminder.timeString.split(":").getOrElse(1) { "00" }) }

        AlertDialog(
            onDismissRequest = { selectedReminderForTimeEdit = null },
            title = {
                Text(
                    text = "Configurar Hora para:\n${currentReminder.label}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Introduce la hora en formato 24 horas (ej. 20:30)",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = hourInput,
                            onValueChange = { if (it.length <= 2) hourInput = it.filter { c -> c.isDigit() } },
                            label = { Text("Hora (0-23)") },
                            modifier = Modifier.width(110.dp),
                            singleLine = true
                        )
                        Text(" : ", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                        OutlinedTextField(
                            value = minInput,
                            onValueChange = { if (it.length <= 2) minInput = it.filter { c -> c.isDigit() } },
                            label = { Text("Min (0-59)") },
                            modifier = Modifier.width(110.dp),
                            singleLine = true
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val h = (hourInput.toIntOrNull() ?: 20).coerceIn(0, 23)
                        val m = (minInput.toIntOrNull() ?: 0).coerceIn(0, 59)
                        val formatted = "${h.toString().padStart(2, '0')}:${m.toString().padStart(2, '0')}"
                        viewModel.updateReminderTime(currentReminder, formatted)
                        selectedReminderForTimeEdit = null
                    }
                ) {
                    Text("Guardar Hora")
                }
            },
            dismissButton = {
                TextButton(onClick = { selectedReminderForTimeEdit = null }) {
                    Text("Cancelar")
                }
            }
        )
    }

    // Dialog to add custom reminder
    if (showAddReminderDialog) {
        var labelInput by remember { mutableStateOf("") }
        var hourInput by remember { mutableStateOf("19") }
        var minInput by remember { mutableStateOf("00") }

        AlertDialog(
            onDismissRequest = { showAddReminderDialog = false },
            title = { Text("Nuevo Recordatorio de Actividad", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedTextField(
                        value = labelInput,
                        onValueChange = { labelInput = it },
                        label = { Text("Nombre del recordatorio") },
                        placeholder = { Text("Ej: Práctica de Listening B2") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedTextField(
                            value = hourInput,
                            onValueChange = { if (it.length <= 2) hourInput = it.filter { c -> c.isDigit() } },
                            label = { Text("Hora") },
                            modifier = Modifier.width(90.dp),
                            singleLine = true
                        )
                        Text(" : ", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                        OutlinedTextField(
                            value = minInput,
                            onValueChange = { if (it.length <= 2) minInput = it.filter { c -> c.isDigit() } },
                            label = { Text("Min") },
                            modifier = Modifier.width(90.dp),
                            singleLine = true
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val h = (hourInput.toIntOrNull() ?: 19).coerceIn(0, 23)
                        val m = (minInput.toIntOrNull() ?: 0).coerceIn(0, 59)
                        val formatted = "${h.toString().padStart(2, '0')}:${m.toString().padStart(2, '0')}"
                        val label = labelInput.ifBlank { "Práctica de estudio B2" }
                        viewModel.addCustomReminder(label, formatted, "DAILY_STUDY")
                        showAddReminderDialog = false
                    }
                ) {
                    Text("Crear y Programar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddReminderDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

    // Result alert
    if (testNotificationResult != null) {
        AlertDialog(
            onDismissRequest = { testNotificationResult = null },
            icon = { Icon(Icons.Default.NotificationsActive, contentDescription = null, tint = BrandBlue) },
            title = { Text("Comprobación de Notificación") },
            text = { Text(testNotificationResult ?: "") },
            confirmButton = {
                Button(onClick = { testNotificationResult = null }) {
                    Text("Aceptar")
                }
            }
        )
    }
}
