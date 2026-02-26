package com.harrrshith.moowe.ui.yours

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.rememberAsyncImagePainter
import com.harrrshith.moowe.LocalHazeState
import com.harrrshith.moowe.domain.model.MediaType
import com.harrrshith.moowe.domain.model.Movie
import com.harrrshith.moowe.ui.components.SegmentedAppTopBar
import com.harrrshith.moowe.ui.theme.AppTheme
import com.harrrshith.moowe.utils.posterUrl
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun YoursRoute(
    modifier: Modifier = Modifier,
    viewModel: YoursViewModel = koinViewModel(),
    navigateToSearch: () -> Unit,
    navigateToDetail: (Int, MediaType, String, String, String) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val hazeState = LocalHazeState.current
    var selectedMediaType by remember { mutableStateOf(MediaType.MOVIE) }
    val filteredSections = uiState.sections
        .map { section -> section.copy(movies = section.movies.filter { it.mediaType == selectedMediaType }) }
        .filter { it.movies.isNotEmpty() }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = AppTheme.colorScheme.surface,
        topBar = {
            SegmentedAppTopBar(
                hazeState = hazeState,
                selectedMediaType = selectedMediaType,
                onMediaTypeSelected = { selectedMediaType = it },
                onSearchClick = navigateToSearch,
            )
        },
    ) { innerPadding ->
        if (filteredSections.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "No favorites yet",
                    style = AppTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = AppTheme.colorScheme.onSurfaceVariant,
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentPadding = PaddingValues(top = 8.dp, bottom = 120.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                filteredSections.forEach { section ->
                    item(key = "header-${section.header}") {
                        Text(
                            text = section.header,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                            style = AppTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                            color = AppTheme.colorScheme.onSurface,
                        )
                    }

                    items(
                        items = section.movies,
                        key = { movie -> "favorite-${movie.mediaType.name}-${movie.id}" },
                    ) { movie ->
                        FavoriteMovieRow(
                            movie = movie,
                            onClick = {
                                navigateToDetail(
                                    movie.id,
                                    movie.mediaType,
                                    "favorite-${movie.mediaType.name}-${movie.id}",
                                    movie.title,
                                    movie.posterPath,
                                )
                            },
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun FavoriteMovieRow(
    movie: Movie,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(14.dp))
            .clickable(onClick = onClick)
            .background(AppTheme.colorScheme.surfaceContainerLow)
            .padding(10.dp),
    ) {
        Image(
            painter = rememberAsyncImagePainter(posterUrl(movie.posterPath, size = "w342")),
            contentDescription = movie.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .width(72.dp)
                .height(100.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(AppTheme.colorScheme.surfaceContainerHigh),
        )

        Column(
            modifier = Modifier
                .padding(start = 12.dp)
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Text(
                text = movie.title,
                style = AppTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
                color = AppTheme.colorScheme.onSurface,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = movie.releaseDate.take(4).ifBlank { "N/A" },
                style = AppTheme.typography.bodySmall,
                color = AppTheme.colorScheme.onSurfaceVariant,
            )
            Text(
                text = movie.overview,
                style = AppTheme.typography.bodySmall,
                color = AppTheme.colorScheme.onSurfaceVariant,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}
