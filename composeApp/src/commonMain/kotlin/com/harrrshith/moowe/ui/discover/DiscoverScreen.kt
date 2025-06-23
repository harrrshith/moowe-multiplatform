@file:OptIn(ExperimentalMaterial3Api::class)
package com.harrrshith.moowe.ui.discover

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.harrrshith.imagecarousel.ImageCarousel
import com.harrrshith.imagecarousel.items
import com.harrrshith.moowe.LocalHazeState
import com.harrrshith.moowe.domain.model.Movie
import com.harrrshith.moowe.ui.components.AppTopBar
import com.harrrshith.moowe.ui.components.ImageCard
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeSource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun DiscoverRoute(
    viewModel: DiscoverViewModel = (koinViewModel())
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val hazeState = LocalHazeState.current
    when {
        uiState.isLoading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .padding(16.dp)
                        .height(48.dp),
                    color = MaterialTheme.colorScheme.primary,
                    strokeWidth = 4.dp
                )
            }
        } else -> {
            DiscoverScreen(
                modifier = Modifier
                    .fillMaxSize(),
                hazeState = hazeState,
                movies = uiState.trendingMovies
            )
        }
    }

}

@Composable
private fun DiscoverScreen(
    modifier: Modifier = Modifier,
    hazeState: HazeState,
    movies: List<Movie>,
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
                TrendingList(
                    title = "Trending",
                    movies = movies
                ) {}
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

@Composable
private fun TrendingList(
    title: String,
    movies: List<Movie>,
    onMovieClick: (Movie) -> Unit,
){
    Text(
        modifier = Modifier.padding(start = 32.dp),
        text = title,
        style = MaterialTheme.typography.headlineMedium,
    )

    Spacer(modifier = Modifier.height(8.dp))
    ImageCarousel(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 32.dp),
    ) {
        items(items = movies) { movie ->
            ImageCard(
                modifier = Modifier
                    .aspectRatio(16f / 9)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.Gray),
                imageUrl = movie.backdropPath,
                movieTitle = movie.title,
                onClick = { onMovieClick(movie) },
            )
        }

    }
}