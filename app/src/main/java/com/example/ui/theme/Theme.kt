package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = BrandBlue,
    onPrimary = LightSurface,
    primaryContainer = BrandBlueDark,
    onPrimaryContainer = LightSurface,
    secondary = BrandCoral,
    onSecondary = LightSurface,
    secondaryContainer = BrandCoralDark,
    onSecondaryContainer = LightSurface,
    tertiary = SuccessGreen,
    onTertiary = LightSurface,
    background = DarkBg,
    onBackground = DarkTextPrimary,
    surface = DarkSurface,
    onSurface = DarkTextPrimary,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = DarkTextSecondary,
    error = ErrorRed,
    onError = LightSurface,
    outline = DarkBorder
)

private val LightColorScheme = lightColorScheme(
    primary = BrandBlue,
    onPrimary = LightSurface,
    primaryContainer = BrandBlueLight,
    onPrimaryContainer = BrandBlueDark,
    secondary = BrandCoral,
    onSecondary = LightSurface,
    secondaryContainer = BrandCoralLight,
    onSecondaryContainer = BrandCoralDark,
    tertiary = SuccessGreen,
    onTertiary = LightSurface,
    background = LightBg,
    onBackground = LightTextPrimary,
    surface = LightSurface,
    onSurface = LightTextPrimary,
    surfaceVariant = LightSurfaceVariant,
    onSurfaceVariant = LightTextSecondary,
    error = ErrorRed,
    onError = LightSurface,
    outline = LightBorder
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

