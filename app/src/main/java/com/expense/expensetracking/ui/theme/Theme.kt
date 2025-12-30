package com.expense.expensetracking.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryGreen,
    onPrimary = Color.White,
    primaryContainer = PrimaryGreen.copy(alpha = 0.2f),
    onPrimaryContainer = PrimaryGreen,
    secondary = PrimaryGreen,
    onSecondary = Color.White,
    background = BackgroundDark,
    onBackground = TextWhite,
    surface = SurfaceDark,
    onSurface = TextWhite,
    surfaceVariant = Color.White.copy(alpha = 0.1f),
    onSurfaceVariant = TextGrayLight,
    outline = BorderColor,
    error = Color(0xFFFF5252)
)

private val LightColorScheme = lightColorScheme(
    primary = PrimaryGreenDark,
    onPrimary = Color.White,
    primaryContainer = PrimaryGreenDark.copy(alpha = 0.1f),
    onPrimaryContainer = PrimaryGreenDark,
    secondary = PrimaryGreenDark,
    onSecondary = Color.White,
    background = BackgroundLight,
    onBackground = TextBlack,
    surface = SurfaceLight,
    onSurface = TextBlack,
    surfaceVariant = Color(0xFFF8FAFC),
    onSurfaceVariant = TextGrayDark,
    outline = Color(0xFFE2E8F0),
    error = Color(0xFFD32F2F)
)

@Composable
fun ExpenseTrackingTheme(
    darkTheme: Boolean,
    content: @Composable () -> Unit
) {
    // Uygulama içi ayara göre tema seçimi (sistem teması yok sayılır)
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        content = content
    )
}
