package com.tomatoapp.application.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val ZomatoRed = Color(0xFFE23744)
val ZomatoDarkRed = Color(0xFFCB202D)
val ZomatoRedLight = Color(0xFFFFF0F1)
val ZomatoGreen = Color(0xFF24963F)
val ZomatoGreenLight = Color(0xFFEDF8EE)
val ZomatoNonVegRed = Color(0xFFB52326)
val ZomatoGold = Color(0xFFD4A30B)
val ZomatoGoldLight = Color(0xFFFFF9E6)
val ZomatoTextDark = Color(0xFF1C1C1C)
val ZomatoTextMuted = Color(0xFF6B6E82)
val ZomatoTextLight = Color(0xFF9C9EA8)
val ZomatoBackground = Color(0xFFF6F7F9)
val ZomatoCardWhite = Color(0xFFFFFFFF)
val ZomatoBorder = Color(0xFFE5E7EB)

private val LightColorScheme = lightColorScheme(
    primary = ZomatoRed,
    onPrimary = Color.White,
    primaryContainer = ZomatoRedLight,
    onPrimaryContainer = ZomatoDarkRed,
    secondary = ZomatoGreen,
    onSecondary = Color.White,
    background = ZomatoBackground,
    surface = ZomatoCardWhite,
    onSurface = ZomatoTextDark,
    outline = ZomatoBorder
)

private val DarkColorScheme = darkColorScheme(
    primary = ZomatoRed,
    onPrimary = Color.White,
    primaryContainer = Color(0xFF381215),
    onPrimaryContainer = Color(0xFFFFB4AB),
    secondary = ZomatoGreen,
    onSecondary = Color.White,
    background = Color(0xFF121212),
    surface = Color(0xFF1E1E1E),
    onSurface = Color(0xFFE0E0E0),
    outline = Color(0xFF2E2E2E)
)

@Composable
fun TomatoTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}
