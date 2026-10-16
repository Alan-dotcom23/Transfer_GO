package com.proyecto.tgo.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = TgoPrimary,
    onPrimary = TgoOnPrimary,
    secondary = TgoSecondary,
    onSecondary = TgoOnSecondary,
    background = TgoBackground,
    onBackground = TgoOnBackground,
    surface = TgoSurface,
    onSurface = TgoOnSurface,
    error = TgoError
)

@Composable
fun TGOTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        content = content
    )
}