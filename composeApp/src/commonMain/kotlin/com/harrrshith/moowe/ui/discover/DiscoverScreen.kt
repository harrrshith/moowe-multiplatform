@file:OptIn(ExperimentalMaterial3Api::class)
package com.harrrshith.moowe.ui.discover

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
        uiState.trendingLoading -> {
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
            trendingList(
                title = "Trending Now",
                movies = movies,
                onMovieClick = {}
            )

            trendingList(
                title = "Other",
                movies = movies,
                onMovieClick = {}
            )
        }
    }
}


private fun LazyListScope.trendingList(
    title: String,
    movies: List<Movie>,
    onMovieClick: (Movie) -> Unit,
){
    item {
        Text(
            modifier = Modifier.padding(start = 32.dp, top = 16.dp),
            text = title,
            style = MaterialTheme.typography.headlineMedium,
        )

        Spacer(modifier = Modifier.height(8.dp))
        ImageCarousel(
            modifier = Modifier.fillMaxWidth()
        ) {
            items(items = movies) { movie ->
                ImageCard(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .aspectRatio(1.5f)
                        .clip(RoundedCornerShape(8.dp)),
                    imageUrl = movie.backdropPath,
                    movieTitle = movie.title,
                    onClick = { onMovieClick(movie) },
                )
            }

        }
    }
}