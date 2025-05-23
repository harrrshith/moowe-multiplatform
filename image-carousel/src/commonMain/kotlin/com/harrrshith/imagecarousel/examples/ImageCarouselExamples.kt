package com.harrrshith.imagecarousel.examples

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.harrrshith.imagecarousel.ImageCarousel
import com.harrrshith.imagecarousel.items

@Composable
fun ImageCarouselExamples() {
    val colors = listOf(
        Color(0xFFFFA726), Color(0xFF66BB6A), Color(0xFF42A5F5),
        Color(0xFFAB47BC), Color(0xFFFF7043), Color(0xFF26C6DA)
    )

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = "With Aspect Ratio (1.5:1)",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(16.dp)
        )
        
        ImageCarousel(
            modifier = Modifier.fillMaxWidth()
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
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Aspect Ratio",
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "With Fixed Height (200dp)",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(16.dp)
        )
        
        ImageCarousel(
            modifier = Modifier.fillMaxWidth(),
            itemHeight = 200.dp
        ) {
            items(items = colors) { color ->
                Card(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                brush = Brush.linearGradient(
                                    colors = listOf(color.copy(alpha = 0.6f), color)
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Fixed Height",
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Accessing Item Dimensions in Scope",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(16.dp)
        )
        
        ImageCarousel(
            modifier = Modifier.fillMaxWidth(),
            itemHeight = 150.dp,
            itemWidthFraction = 0.8f
        ) {
            items(items = colors) { color ->
                Card(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(color),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Width: ${itemWidth.value.toInt()}dp",
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Height: ${itemHeight?.value?.toInt() ?: "Auto"}dp",
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Scale: ${String.format("%.2f", scale)}",
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}
