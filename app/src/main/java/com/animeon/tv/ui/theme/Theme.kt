package com.animeon.tv.ui.theme

import androidx.compose.runtime.Composable
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.darkColorScheme

@OptIn(ExperimentalTvMaterial3Api::class)
private val DarkColorPalette = darkColorScheme(
    primary = RedAccent,
    onPrimary = TextWhite,
    primaryContainer = SurfaceDarkVariant,
    onPrimaryContainer = TextWhite,
    secondary = FocusGold,
    background = BgDark,
    onBackground = TextWhite,
    surface = SurfaceDark,
    onSurface = TextWhite,
    surfaceVariant = SurfaceDarkVariant,
    onSurfaceVariant = TextMuted,
    border = FocusGold
)

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun AnimeOnTvTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorPalette,
        content = content
    )
}
