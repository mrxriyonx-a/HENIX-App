package com.henix.app

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val BrandGreen = Color(0xFF00E676)
val BrandPink = Color(0xFFE91E63)
val BrandDark = Color(0xFF0B0C10)
val BrandCard = Color(0xFF16181F)
val BrandNav = Color(0xFF0E1017)
val Amber = Color(0xFFF59E0B)
val Purple = Color(0xFFA855F7)

@Composable
fun HenixTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = darkColorScheme(
            primary = BrandGreen, secondary = BrandPink, background = BrandDark,
            surface = BrandCard, onBackground = Color(0xFFE4E4E7), onSurface = Color(0xFFE4E4E7)
        ),
        content = content
    )
}
