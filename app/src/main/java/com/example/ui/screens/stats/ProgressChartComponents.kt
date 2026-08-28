package com.example.ui.screens.stats

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import com.example.ui.viewmodels.CategoryAccuracyStat
import com.example.ui.viewmodels.DailyAccuracyStat
import com.example.ui.viewmodels.DailyPomodoroStat
import kotlin.math.max

/**
 * Recharts/D3-inspired Interactive Bar Chart for Daily Pomodoro Study Time.
 * Visualizes accumulated focus minutes, daily goal benchmark line, and interactive tooltips.
 */
@Composable
fun PomodoroStudyTimeChart(
    dailyStats: List<DailyPomodoroStat>,
    dailyGoalMinutes: Int,
    onAdjustGoalClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (dailyStats.isEmpty()) return

    var selectedIndex by remember { mutableStateOf<Int?>(dailyStats.indexOfLast { it.isToday }.takeIf { it >= 0 } ?: (dailyStats.size - 1)) }
    val selectedStat = selectedIndex?.let { if (it in dailyStats.indices) dailyStats[it] else null }

    val maxMinutesInData = dailyStats.maxOfOrNull { it.pomodoroMinutes + it.nonPomodoroMinutes } ?: 30
    val maxChartY = max(maxMinutesInData, dailyGoalMinutes + 15).coerceAtLeast(60)

    val totalPomoMins = dailyStats.sumOf { it.pomodoroMinutes }
    val totalHours = totalPomoMins / 60
    val remMinutes = totalPomoMins % 60
    val avgMins = if (dailyStats.isNotEmpty()) totalPomoMins / dailyStats.size else 0
    val goalsMet = dailyStats.count { it.pomodoroMinutes >= dailyGoalMinutes }
    val totalSessions = dailyStats.sumOf { it.sessionsCount }

    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier
            .fillMaxWidth()
            .testTag("chart_pomodoro_study_time")
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Tiempo de Estudio Diario",
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Acumulado mediante Temporizador Pomodoro",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = BrandBlue.copy(alpha = 0.12f),
                    modifier = Modifier.clickable { onAdjustGoalClick() }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Tune,
                            contentDescription = "Ajustar meta",
                            tint = BrandBlue,
                            modifier = Modifier.size(15.dp)
                        )
                        Text(
                            text = "Meta: ${dailyGoalMinutes}m",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = BrandBlue
                        )
                    }
                }
            }

            // Quick Aggregate KPI Cards
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                MetricKpiPill(
                    label = "Total Período",
                    value = "${totalHours}h ${remMinutes}m",
                    icon = Icons.Default.Timer,
                    color = BrandBlue,
                    modifier = Modifier.weight(1f)
                )
                MetricKpiPill(
                    label = "Promedio Diario",
                    value = "${avgMins} min",
                    icon = Icons.Default.TrendingUp,
                    color = SuccessGreen,
                    modifier = Modifier.weight(1f)
                )
                MetricKpiPill(
                    label = "Días Cumplidos",
                    value = "$goalsMet/${dailyStats.size}",
                    icon = Icons.Default.CheckCircle,
                    color = WarningAmber,
                    modifier = Modifier.weight(1f)
                )
            }

            // Interactive Tooltip Card (Recharts style floating inspection)
            AnimatedVisibility(
                visible = selectedStat != null,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                selectedStat?.let { stat ->
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, BrandBlue.copy(alpha = 0.3f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Text(
                                        text = "${stat.dayLabel} (${stat.fullDate})",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    if (stat.isToday) {
                                        Surface(
                                            shape = RoundedCornerShape(6.dp),
                                            color = BrandBlue,
                                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                        ) {
                                            Text("HOY", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Color.White, modifier = Modifier.padding(horizontal = 4.dp))
                                        }
                                    }
                                }
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = if (stat.sessionsCount > 0) "${stat.sessionsCount} sesiones Pomodoro completadas" else "Sin sesiones registradas",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                if (stat.topicsStudied.isNotEmpty()) {
                                    Text(
                                        text = "Temas: " + stat.topicsStudied.take(2).joinToString(", "),
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = BrandBlue
                                    )
                                }
                            }

                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = "${stat.pomodoroMinutes} min",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = if (stat.pomodoroMinutes >= dailyGoalMinutes) SuccessGreen else BrandBlue
                                )
                                Text(
                                    text = if (stat.pomodoroMinutes >= dailyGoalMinutes) "Meta Lograda ✓" else "Faltaron ${max(0, dailyGoalMinutes - stat.pomodoroMinutes)}m",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = if (stat.pomodoroMinutes >= dailyGoalMinutes) SuccessGreen else WarningAmber
                                )
                            }
                        }
                    }
                }
            }

            // Recharts-style Canvas Bar Chart
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(210.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.25f))
                    .padding(top = 16.dp, bottom = 8.dp, start = 8.dp, end = 8.dp)
            ) {
                Canvas(
                    modifier = Modifier
                        .fillMaxSize()
                        .pointerInput(dailyStats) {
                            detectTapGestures { offset ->
                                val barWidthTotal = size.width / dailyStats.size
                                val index = (offset.x / barWidthTotal).toInt().coerceIn(0, dailyStats.size - 1)
                                selectedIndex = index
                            }
                        }
                ) {
                    val width = size.width
                    val height = size.height - 30.dp.toPx() // Reserve space for bottom labels
                    val count = dailyStats.size
                    val slotWidth = width / count
                    val barWidth = (slotWidth * 0.58f).coerceAtMost(36.dp.toPx())

                    // Draw Horizontal Gridlines (0m, 30m, 60m, 90m, etc.)
                    val gridSteps = 3
                    val stepMinutes = maxChartY / gridSteps
                    for (step in 0..gridSteps) {
                        val minVal = step * stepMinutes
                        val y = height - (minVal.toFloat() / maxChartY * height)
                        drawLine(
                            color = Color.LightGray.copy(alpha = 0.35f),
                            start = Offset(0f, y),
                            end = Offset(width, y),
                            strokeWidth = 1.dp.toPx(),
                            pathEffect = PathEffect.dashPathEffect(floatArrayOf(6f, 6f), 0f)
                        )
                    }

                    // Draw Daily Goal Reference Line (Dashed)
                    val goalY = height - (dailyGoalMinutes.toFloat() / maxChartY * height)
                    if (goalY in 0f..height) {
                        drawLine(
                            color = WarningAmber.copy(alpha = 0.8f),
                            start = Offset(0f, goalY),
                            end = Offset(width, goalY),
                            strokeWidth = 2.dp.toPx(),
                            pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 6f), 0f)
                        )
                    }

                    // Draw Bars for each day
                    dailyStats.forEachIndexed { i, stat ->
                        val centerX = (i * slotWidth) + (slotWidth / 2f)
                        val left = centerX - (barWidth / 2f)
                        val barHeight = ((stat.pomodoroMinutes.toFloat() / maxChartY) * height).coerceAtLeast(6.dp.toPx())
                        val top = height - barHeight
                        val isSelected = (i == selectedIndex)

                        // Bar background slot
                        drawRoundRect(
                            color = Color.LightGray.copy(alpha = 0.12f),
                            topLeft = Offset(left, 0f),
                            size = Size(barWidth, height),
                            cornerRadius = CornerRadius(8.dp.toPx(), 8.dp.toPx())
                        )

                        // Main Pomodoro Focus Bar Gradient
                        val gradientBrush = if (stat.pomodoroMinutes >= dailyGoalMinutes) {
                            Brush.verticalGradient(
                                colors = listOf(BrandBlue, SuccessGreen),
                                startY = top,
                                endY = height
                            )
                        } else {
                            Brush.verticalGradient(
                                colors = listOf(BrandBlue.copy(alpha = 0.9f), BrandBlue.copy(alpha = 0.5f)),
                                startY = top,
                                endY = height
                            )
                        }

                        // Draw animated bar
                        drawRoundRect(
                            brush = gradientBrush,
                            topLeft = Offset(left, top),
                            size = Size(barWidth, barHeight),
                            cornerRadius = CornerRadius(6.dp.toPx(), 6.dp.toPx())
                        )

                        // Highlight Border if selected or isToday
                        if (isSelected) {
                            drawRoundRect(
                                color = BrandBlue,
                                topLeft = Offset(left - 2.dp.toPx(), top - 2.dp.toPx()),
                                size = Size(barWidth + 4.dp.toPx(), barHeight + 4.dp.toPx()),
                                cornerRadius = CornerRadius(8.dp.toPx(), 8.dp.toPx()),
                                style = Stroke(width = 2.dp.toPx())
                            )
                        }
                    }
                }

                // Bottom Day Labels Row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .height(26.dp),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    dailyStats.forEachIndexed { i, stat ->
                        val isSelected = (i == selectedIndex)
                        Text(
                            text = stat.dayLabel,
                            fontSize = 11.sp,
                            fontWeight = if (isSelected || stat.isToday) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSelected) BrandBlue else if (stat.isToday) BrandCoral else MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .weight(1f)
                                .clickable { selectedIndex = i }
                        )
                    }
                }
            }

            // Legend
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(modifier = Modifier.size(10.dp).background(BrandBlue, RoundedCornerShape(2.dp)))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Pomodoro Focus", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(modifier = Modifier.width(16.dp))
                Box(modifier = Modifier.size(10.dp).background(SuccessGreen, RoundedCornerShape(2.dp)))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Meta Cumplida", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(modifier = Modifier.width(16.dp))
                Box(modifier = Modifier.width(14.dp).height(2.dp).background(WarningAmber))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Meta (${dailyGoalMinutes}m)", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

/**
 * Recharts/D3-inspired Smooth Bezier Area & Line Chart for Daily Exercise Accuracy Rate (Tasa de Aciertos %).
 */
@Composable
fun ExerciseAccuracyRateChart(
    accuracyStats: List<DailyAccuracyStat>,
    modifier: Modifier = Modifier
) {
    if (accuracyStats.isEmpty()) return

    var selectedIndex by remember { mutableStateOf<Int?>(accuracyStats.indexOfLast { it.isToday }.takeIf { it >= 0 } ?: (accuracyStats.size - 1)) }
    val selectedStat = selectedIndex?.let { if (it in accuracyStats.indices) accuracyStats[it] else null }

    val validStats = accuracyStats.filter { it.totalAttempts > 0 }
    val avgAccuracy = if (validStats.isNotEmpty()) validStats.sumOf { it.accuracyPercentage } / validStats.size else 88
    val totalCorrect = accuracyStats.sumOf { it.totalScore }
    val totalMax = accuracyStats.sumOf { it.totalMaxScore }
    val totalExercises = accuracyStats.sumOf { it.totalAttempts }

    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier
            .fillMaxWidth()
            .testTag("chart_exercise_accuracy_rate")
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Tasa de Aciertos en Ejercicios",
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Evolución porcentual de precisión Cambridge",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = SuccessGreen.copy(alpha = 0.14f)
                ) {
                    Text(
                        text = "$avgAccuracy% Promedio",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = SuccessGreen,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                    )
                }
            }

            // Quick KPI Pills
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                MetricKpiPill(
                    label = "Aciertos Globales",
                    value = "$totalCorrect / $totalMax",
                    icon = Icons.Default.Grade,
                    color = SuccessGreen,
                    modifier = Modifier.weight(1f)
                )
                MetricKpiPill(
                    label = "Ejercicios Hechos",
                    value = "$totalExercises tests",
                    icon = Icons.Default.AssignmentTurnedIn,
                    color = BrandBlue,
                    modifier = Modifier.weight(1f)
                )
                MetricKpiPill(
                    label = "Nivel Cambridge",
                    value = if (avgAccuracy >= 85) "Aprobado (B2+)" else "En progreso",
                    icon = Icons.Default.Verified,
                    color = BrandCoral,
                    modifier = Modifier.weight(1f)
                )
            }

            // Tooltip Card for Selected Day
            AnimatedVisibility(
                visible = selectedStat != null,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                selectedStat?.let { stat ->
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, SuccessGreen.copy(alpha = 0.3f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(
                                    text = "${stat.dayLabel} (${stat.fullDate})",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = if (stat.totalAttempts > 0) "${stat.totalAttempts} ejercicios completados (${stat.totalScore}/${stat.totalMaxScore} puntos)" else "Sin tests este día",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = "${stat.accuracyPercentage}%",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = when {
                                        stat.accuracyPercentage >= 90 -> SuccessGreen
                                        stat.accuracyPercentage >= 70 -> BrandBlue
                                        else -> WarningAmber
                                    }
                                )
                                Text(
                                    text = if (stat.accuracyPercentage >= 70) "Superado ✓" else "Repaso sugerido",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = if (stat.accuracyPercentage >= 70) SuccessGreen else WarningAmber
                                )
                            }
                        }
                    }
                }
            }

            val surfaceColor = MaterialTheme.colorScheme.surface

            // D3/Recharts-style Smooth Spline / Area Chart
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.25f))
                    .padding(top = 16.dp, bottom = 8.dp, start = 8.dp, end = 8.dp)
            ) {
                Canvas(
                    modifier = Modifier
                        .fillMaxSize()
                        .pointerInput(accuracyStats) {
                            detectTapGestures { offset ->
                                val slotWidth = size.width / accuracyStats.size
                                val index = (offset.x / slotWidth).toInt().coerceIn(0, accuracyStats.size - 1)
                                selectedIndex = index
                            }
                        }
                ) {
                    val width = size.width
                    val height = size.height - 30.dp.toPx()
                    val count = accuracyStats.size
                    if (count < 2) return@Canvas

                    val slotWidth = width / (count - 1).coerceAtLeast(1)

                    // Draw Horizontal Percentage Gridlines (0%, 25%, 50%, 75%, 100%)
                    val percentages = listOf(0, 25, 50, 75, 100)
                    percentages.forEach { pct ->
                        val y = height - (pct / 100f * height)
                        drawLine(
                            color = Color.LightGray.copy(alpha = 0.35f),
                            start = Offset(0f, y),
                            end = Offset(width, y),
                            strokeWidth = 1.dp.toPx(),
                            pathEffect = PathEffect.dashPathEffect(floatArrayOf(6f, 6f), 0f)
                        )
                    }

                    // Passing Score Benchmark Line (70% Cambridge Passing standard)
                    val passY = height - (70 / 100f * height)
                    drawLine(
                        color = SuccessGreen.copy(alpha = 0.5f),
                        start = Offset(0f, passY),
                        end = Offset(width, passY),
                        strokeWidth = 1.5.dp.toPx(),
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(8f, 6f), 0f)
                    )

                    // Build Smooth Cubic Bezier Line & Area
                    val points = accuracyStats.mapIndexed { i, stat ->
                        val x = i * slotWidth
                        val effectiveAccuracy = if (stat.totalAttempts > 0) stat.accuracyPercentage else 80
                        val y = height - (effectiveAccuracy / 100f * height)
                        Offset(x, y)
                    }

                    val linePath = Path()
                    val areaPath = Path()

                    linePath.moveTo(points.first().x, points.first().y)
                    areaPath.moveTo(points.first().x, height)
                    areaPath.lineTo(points.first().x, points.first().y)

                    for (i in 0 until points.size - 1) {
                        val p0 = points[i]
                        val p1 = points[i + 1]
                        val controlPoint1 = Offset(p0.x + (p1.x - p0.x) / 2f, p0.y)
                        val controlPoint2 = Offset(p0.x + (p1.x - p0.x) / 2f, p1.y)

                        linePath.cubicTo(
                            controlPoint1.x, controlPoint1.y,
                            controlPoint2.x, controlPoint2.y,
                            p1.x, p1.y
                        )
                        areaPath.cubicTo(
                            controlPoint1.x, controlPoint1.y,
                            controlPoint2.x, controlPoint2.y,
                            p1.x, p1.y
                        )
                    }

                    areaPath.lineTo(points.last().x, height)
                    areaPath.close()

                    // Draw Gradient Area Under Curve
                    drawPath(
                        path = areaPath,
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                SuccessGreen.copy(alpha = 0.35f),
                                BrandBlue.copy(alpha = 0.15f),
                                Color.Transparent
                            ),
                            startY = 0f,
                            endY = height
                        )
                    )

                    // Draw Spline Line
                    drawPath(
                        path = linePath,
                        brush = Brush.horizontalGradient(listOf(BrandBlue, SuccessGreen)),
                        style = Stroke(width = 3.5.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round)
                    )

                    // Draw Interactive Circular Points
                    points.forEachIndexed { i, pt ->
                        val isSelected = (i == selectedIndex)
                        val pointRadius = if (isSelected) 7.dp.toPx() else 4.5.dp.toPx()

                        // Outer glowing ring if selected
                        if (isSelected) {
                            drawCircle(
                                color = SuccessGreen.copy(alpha = 0.3f),
                                radius = pointRadius + 5.dp.toPx(),
                                center = pt
                            )
                        }

                        drawCircle(
                            color = surfaceColor,
                            radius = pointRadius + 1.5.dp.toPx(),
                            center = pt
                        )
                        drawCircle(
                            color = if (isSelected) SuccessGreen else BrandBlue,
                            radius = pointRadius,
                            center = pt
                        )
                    }
                }

                // Bottom Labels Row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .height(26.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    accuracyStats.forEachIndexed { i, stat ->
                        val isSelected = (i == selectedIndex)
                        Text(
                            text = stat.dayLabel,
                            fontSize = 11.sp,
                            fontWeight = if (isSelected || stat.isToday) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSelected) SuccessGreen else if (stat.isToday) BrandCoral else MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .weight(1f)
                                .clickable { selectedIndex = i }
                        )
                    }
                }
            }

            // Legend
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(modifier = Modifier.size(10.dp).background(SuccessGreen, CircleShape))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Tasa de Aciertos (%)", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(modifier = Modifier.width(16.dp))
                Box(modifier = Modifier.width(14.dp).height(2.dp).background(SuccessGreen.copy(alpha = 0.6f)))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Aprobado Cambridge (70%)", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

/**
 * Breakdown of Accuracy by Cambridge Competence Skill.
 */
@Composable
fun SkillsAccuracyBreakdownCard(
    categoryStats: List<CategoryAccuracyStat>,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier
            .fillMaxWidth()
            .testTag("card_skills_accuracy_breakdown")
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Aciertos por Destreza Cambridge",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "CEFR B2 Standard",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = BrandBlue
                )
            }

            categoryStats.forEach { stat ->
                SkillAccuracyBarRow(stat = stat)
            }
        }
    }
}

@Composable
private fun SkillAccuracyBarRow(stat: CategoryAccuracyStat) {
    val barColor = when {
        stat.accuracyPercentage >= 90 -> SuccessGreen
        stat.accuracyPercentage >= 75 -> BrandBlue
        stat.accuracyPercentage >= 60 -> WarningAmber
        else -> BrandCoral
    }

    val badgeLabel = when {
        stat.accuracyPercentage >= 90 -> "Excelente"
        stat.accuracyPercentage >= 75 -> "Dominado"
        stat.accuracyPercentage >= 60 -> "Aceptable"
        else -> "Reforzar"
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = stat.category,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = barColor.copy(alpha = 0.15f)
                ) {
                    Text(
                        text = badgeLabel,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = barColor,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Text(
                text = "${stat.accuracyPercentage}%",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = barColor
            )
        }

        LinearProgressIndicator(
            progress = { stat.accuracyPercentage / 100f },
            modifier = Modifier
                .fillMaxWidth()
                .height(7.dp)
                .clip(RoundedCornerShape(4.dp)),
            color = barColor,
            trackColor = MaterialTheme.colorScheme.surfaceVariant
        )
    }
}

@Composable
private fun MetricKpiPill(
    label: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier.size(13.dp)
                )
                Text(
                    text = label,
                    fontSize = 10.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Text(
                text = value,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}
