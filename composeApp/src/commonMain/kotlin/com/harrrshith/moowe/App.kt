package com.harrrshith.moowe

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.harrrshith.moowe.ui.components.imageCarousel.ImageCarousel
import com.harrrshith.moowe.ui.components.imageCarousel.items
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    MaterialTheme {
        val colors = remember {
            listOf(
                Color(0xFFFFA726), Color(0xFF66BB6A), Color(0xFF42A5F5),
                Color(0xFFAB47BC), Color(0xFFFF7043), Color(0xFF26C6DA),
                Color(0xFFD4E157), Color(0xFFEC407A), Color(0xFF7E57C2),
                Color(0xFF26A69A)
            )
        }
        Surface {
            ImageCarousel(
                modifier = Modifier
                    .fillMaxWidth()
                    .safeDrawingPadding(),
                contentPadding = PaddingValues(horizontal = 32.dp),
            ) {
                items(items = colors) { color ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1.5f)
                            .padding(horizontal = 12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    brush = Brush.linearGradient(
                                        colors = listOf(color.copy(alpha = 0.6f), color)
                                    )
                                )
                        )
                    }
                }
            }
        }
    }
}