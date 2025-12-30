package com.expense.expensetracking.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

data class CustomColors(
    val surface: Color,
    val inputBackground: Color,
    val inputText: Color,
    val borderColor: Color,
    val customTextGray: Color
)

@Composable
fun getCustomColors(darkTheme: Boolean): CustomColors {
    return if (darkTheme) {
        CustomColors(
            surface = SurfaceDark,
            inputBackground = InputBg,
            inputText = InputText,
            borderColor = BorderColor, // 0x0DFFFFFF
            customTextGray = TextGrayLight
        )
    } else {
        CustomColors(
            surface = SurfaceLight,
            inputBackground = Color(0xFFE8F5E9), // Açık tema için uygun input bg
            inputText = Color(0xFF23482F),      // Açık tema için uygun input text
            borderColor = Color(0x1A000000),    // Açık tema için koyu border
            customTextGray = TextGrayDark
        )
    }
}

val PrimaryGreenDark = Color(0xFF0A9E3D)
val PrimaryGreen = Color(0xFF13EC5B)
val BackgroundDark = Color(0xFF102216)
val BackgroundLight = Color(0xFFF6F8F6)

val BorderColor = Color(0x0DFFFFFF)

val TextDark = Color(0xFF111827) // Gray-900
val TextLight = Color.White

// Surface Colors (Derived)
val SurfaceDark = Color(0xFF1C2D22) // Slightly lighter than bg
val SurfaceLight = Color(0xFFFFFFFF)

// Text Colors
val TextWhite = Color(0xFFFFFFFF)
val TextGrayLight = Color(0xFF9CA3AF) // zinc-400
val TextGrayDark = Color(0xFF4B5563)  // zinc-600
val TextBlack = Color(0xFF111827)     // zinc-900

// Chart/Category Colors
val ChartAmber = Color(0xFFFBBF24) // amber-400
val ChartSky = Color(0xFF38BDF8)   // sky-400
val ChartPink = Color(0xFFF472B6)  // pink-400

val GreenPrimary = Color(0xFF13EC5B)
// Card gradients vs için
val InputBg = Color(0xFF23482F)
val InputText = Color(0xFF92C9A4)
val TextGray = Color(0xFFAAAAAA)