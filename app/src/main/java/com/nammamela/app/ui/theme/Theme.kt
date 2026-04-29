package com.nammamela.app.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = NammaMaroon,
    onPrimary = NammaGold,
    primaryContainer = NammaDeepMaroon,
    onPrimaryContainer = NammaWarmWhite,
    secondary = NammaGold,
    onSecondary = NammaDarkBrown,
    secondaryContainer = NammaSurfaceHigh,
    onSecondaryContainer = NammaGold,
    tertiary = NammaSoftGold,
    background = NammaDarkBrown,
    surface = NammaSurfaceLow,
    onBackground = NammaWarmWhite,
    onSurface = NammaWarmWhite,
    surfaceVariant = NammaSurfaceContainer,
    onSurfaceVariant = NammaWarmWhite.copy(0.7f),
    outline = Color(0xFFA98984)
)

@Composable
fun NammaMelaTheme(
    content: @Composable () -> Unit
) {
    val colorScheme = DarkColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            window.navigationBarColor = colorScheme.background.toArgb()
            
            val controller = WindowCompat.getInsetsController(window, view)
            controller.isAppearanceLightStatusBars = false
            controller.isAppearanceLightNavigationBars = false
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
