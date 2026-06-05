package com.rajk2007.kino.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = KinoColors.Red,
    secondary = KinoColors.Purple,
    tertiary = KinoColors.Gold,
    background = KinoColors.Black,
    surface = KinoColors.Surface,
    onPrimary = KinoColors.Text,
    onSecondary = KinoColors.Text,
    onTertiary = KinoColors.Black,
    onBackground = KinoColors.Text,
    onSurface = KinoColors.Text
)

@Composable
fun KinoTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        content = content
    )
}
