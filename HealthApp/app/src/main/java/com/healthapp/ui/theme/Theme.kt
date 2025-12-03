package com.healthapp.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFB88BFF),
    onPrimary = Color(0xFF2B0B5E),
    primaryContainer = Color(0xFF3A2E5B),
    onPrimaryContainer = Color(0xFFECDDFF),
    secondary = Color(0xFFFFA8D9),
    onSecondary = Color(0xFF4D0F33),
    secondaryContainer = Color(0xFF5E2149),
    onSecondaryContainer = Color(0xFFFFD9EB),
    tertiary = Color(0xFF81D6FF),
    onTertiary = Color(0xFF00344D),
    tertiaryContainer = Color(0xFF004D6D),
    onTertiaryContainer = Color(0xFFBCE9FF),
    background = Color(0xFF0F1224),
    onBackground = Color(0xFFE4E7F5),
    surface = Color(0xFF13172C),
    onSurface = Color(0xFFE4E7F5),
    surfaceVariant = Color(0xFF2D3253),
    onSurfaceVariant = Color(0xFFB8C0E0),
    error = JaguarError,
    onError = Color.White
)

private val LightColorScheme = lightColorScheme(
    primary = JaguarGold,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFE8DDFF),
    onPrimaryContainer = JaguarGoldDim,
    secondary = JaguarAccent,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFFFE0F2),
    onSecondaryContainer = Color(0xFFA12D72),
    tertiary = Color(0xFF5EC8FF),
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFD7F1FF),
    onTertiaryContainer = Color(0xFF0B4F7A),
    background = Color(0xFFF7F9FF),
    onBackground = JaguarTextPrimary,
    surface = Color.White,
    onSurface = JaguarTextPrimary,
    surfaceVariant = Color(0xFFE9EDFF),
    onSurfaceVariant = JaguarTextSecondary,
    error = JaguarError,
    onError = Color.White,
    outline = Color(0xFFD5DBFF)
)

@Composable
fun HealthAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
