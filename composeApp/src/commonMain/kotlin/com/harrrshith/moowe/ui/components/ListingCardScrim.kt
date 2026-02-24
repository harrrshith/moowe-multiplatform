package com.harrrshith.moowe.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

@Composable
fun ListingCardScrim(
    modifier: Modifier = Modifier,
    intensity: Float = 1f,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .drawWithCache {
                val brush = Brush.linearGradient(
                    colorStops = arrayOf(
                        0.00f to Color.Transparent,
                        0.52f to Color.Transparent,
                        0.72f to Color.Black.copy(alpha = 0.10f * intensity),
                        0.86f to Color.Black.copy(alpha = 0.34f * intensity),
                        1.00f to Color.Black.copy(alpha = 0.68f * intensity),
                    ),
                    start = Offset(x = size.width * 0.18f, y = size.height * 0.14f),
                    end = Offset(x = size.width * 0.82f, y = size.height),
                )

                onDrawBehind {
                    drawRect(brush = brush)
                }
            }
            .background(Color.Transparent),
    )
}
