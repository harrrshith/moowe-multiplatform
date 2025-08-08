package com.harrrshith.moowe.ui.components.modifierExtensions

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode

fun Modifier.bottomGradientOverlay(
    fraction: Float = 0.3f,
    color: Color = Color.Black,
    alpha: Float = 0.4f
): Modifier = this.drawWithContent {
    val gradientHeight = size.height * fraction
    val topY = size.height - gradientHeight
    val bottomY = size.height
    drawContent()
    drawRect(
        brush = Brush.linearGradient(
            colors = listOf(
                color.copy(alpha = alpha),
                Color.Transparent
            ),
            start = Offset(0f, bottomY),
            end = Offset(0f, topY),
            tileMode = TileMode.Decal
        ),
        topLeft = Offset(0f, topY),
        size = Size(size.width, gradientHeight)
    )
}