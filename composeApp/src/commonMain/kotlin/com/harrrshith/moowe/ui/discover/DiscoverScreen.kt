@file:OptIn(ExperimentalMaterial3Api::class)
package com.harrrshith.moowe.ui.discover

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.harrrshith.imagecarousel.ImageCarousel
import com.harrrshith.imagecarousel.items
import com.harrrshith.moowe.LocalHazeState
import com.harrrshith.moowe.ui.components.AppTopBar
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeSource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun DiscoverRoute(
    discoverViewModel: DiscoverViewModel = (koinViewModel())
) {
    val hazeState = LocalHazeState.current
    DiscoverScreen(
        modifier = Modifier
            .fillMaxSize(),
        hazeState = hazeState
    )
    LaunchedEffect(true) {
        discoverViewModel.fetchTrendingMovies()
    }
}

@Composable
private fun DiscoverScreen(
    modifier: Modifier = Modifier,
    hazeState: HazeState
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            AppTopBar(
                title = "moowe",
                hazeState = hazeState
            )
        }
    ) {
        LazyColumn(
            modifier = modifier
                .hazeSource(state = hazeState),
            contentPadding = PaddingValues(
                top = 100.dp,
                bottom = 80.dp
            )
        ) {
            item {
                Text(
                    text = "Hello",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
            item {
                AppImageCarousel()
            }
            item {
                AppImageCarousel()
            }
            item {
                AppImageCarousel()
            }
            item {
                AppImageCarousel()
            }
            item {
                AppImageCarousel()
            }
        }
    }
}

@Composable
fun AppImageCarousel() {
    val colors = remember {
        listOf(
            Color(0xFFFFA726), Color(0xFF66BB6A), Color(0xFF42A5F5),
            Color(0xFFAB47BC), Color(0xFFFF7043), Color(0xFF26C6DA),
            Color(0xFFD4E157), Color(0xFFEC407A), Color(0xFF7E57C2),
            Color(0xFF26A69A)
        )
    }
    ImageCarousel(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 32.dp),
    ) {
        items(items = colors) { color ->
            Card(
                modifier = Modifier
                    .fillMaxWidth(0.95f)
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