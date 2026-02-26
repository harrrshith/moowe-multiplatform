@file:OptIn(ExperimentalFoundationApi::class)

package com.harrrshith.moowe.ui.search

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items as rowItems
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.rememberAsyncImagePainter
import com.harrrshith.moowe.LocalHazeState
import com.harrrshith.moowe.domain.model.MediaType
import com.harrrshith.moowe.domain.model.Movie
import com.harrrshith.moowe.ui.components.ListingCardScrim
import com.harrrshith.moowe.ui.components.SegmentedAppTopBar
import com.harrrshith.moowe.ui.theme.AppTheme
import com.harrrshith.moowe.utils.posterUrl
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SearchRoute(
    modifier: Modifier = Modifier,
    viewModel: SearchViewModel = koinViewModel(),
    navigateToDetail: (Int, MediaType, String, String, String) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val hazeState = LocalHazeState.current

    LaunchedEffect(Unit) {
        viewModel.uiEvents.collect { event ->
            when (event) {
                is SearchUiEvent.NavigateToDetail -> {
                    navigateToDetail(
                        event.id,
                        event.mediaType,
                        event.sharedKey,
                        event.title,
                        event.posterPath,
                    )
                }
            }
        }
    }

    SearchScreen(
        modifier = modifier,
        hazeState = hazeState,
        uiState = uiState,
        onQueryChanged = viewModel::onQueryChange,
        onMediaTypeChanged = viewModel::onMediaTypeChanged,
        onMovieClick = viewModel::onMovieClick,
    )
}

@Composable
private fun SearchScreen(
    modifier: Modifier = Modifier,
    hazeState: dev.chrisbanes.haze.HazeState,
    uiState: SearchUiState,
    onQueryChanged: (String) -> Unit,
    onMediaTypeChanged: (MediaType) -> Unit,
    onMovieClick: (Movie) -> Unit,
) {
    val filteredRecent = uiState.recentSearches.filter { it.mediaType == uiState.selectedMediaType }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = AppTheme.colorScheme.surface,
        topBar = {
            SearchTopBar(
                hazeState = hazeState,
                selectedMediaType = uiState.selectedMediaType,
                query = uiState.query,
                onMediaTypeChanged = onMediaTypeChanged,
                onQueryChanged = onQueryChanged,
            )
        },
    ) { innerPadding ->
        when {
            uiState.query.isBlank() -> {
                if (filteredRecent.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize().padding(innerPadding))
                } else {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                    ) {
                        Text(
                            text = "Recently searched",
                            style = AppTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                            color = AppTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 14.dp, bottom = 10.dp),
                        )
                        LazyRow(
                            contentPadding = PaddingValues(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                        ) {
                            rowItems(
                                items = filteredRecent.take(10),
                                key = { movie -> "recent-${movie.mediaType.name}-${movie.id}" },
                            ) { movie ->
                                RecentSearchCard(
                                    movie = movie,
                                    onClick = { onMovieClick(movie) },
                                )
                            }
                        }
                    }
                }
            }

            uiState.isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator(
                        color = AppTheme.colorScheme.primary,
                        strokeWidth = 3.dp,
                    )
                }
            }

            uiState.results.isEmpty() -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(horizontal = 20.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = uiState.error ?: "No movies found",
                        style = AppTheme.typography.bodyMedium,
                        color = AppTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }

            else -> {
                LazyVerticalStaggeredGrid(
                    columns = StaggeredGridCells.Fixed(2),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentPadding = PaddingValues(start = 12.dp, end = 12.dp, bottom = 120.dp),
                    verticalItemSpacing = 10.dp,
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    items(
                        items = uiState.results,
                        key = { movie -> "search-${movie.mediaType.name}-${movie.id}" },
                    ) { movie ->
                        SearchMovieCard(
                            movie = movie,
                            onClick = { onMovieClick(movie) },
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun RecentSearchCard(
    movie: Movie,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .width(132.dp)
            .height(190.dp)
            .clip(RoundedCornerShape(14.dp))
            .clickable(onClick = onClick),
    ) {
        Image(
            painter = rememberAsyncImagePainter(posterUrl(movie.posterPath)),
            contentDescription = movie.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(),
        )

        ListingCardScrim()

        Text(
            text = movie.title,
            color = Color.White,
            style = AppTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(horizontal = 10.dp, vertical = 9.dp),
        )
    }
}

@Composable
private fun SearchTopBar(
    hazeState: dev.chrisbanes.haze.HazeState,
    selectedMediaType: MediaType,
    query: String,
    onMediaTypeChanged: (MediaType) -> Unit,
    onQueryChanged: (String) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        SegmentedAppTopBar(
            hazeState = hazeState,
            selectedMediaType = selectedMediaType,
            onMediaTypeSelected = onMediaTypeChanged,
        )
        PremiumSearchInput(
            value = query,
            onValueChange = onQueryChanged,
        )
    }
}

@Composable
private fun PremiumSearchInput(
    value: String,
    onValueChange: (String) -> Unit,
) {
    Surface(
        shape = RoundedCornerShape(18.dp),
        color = AppTheme.colorScheme.surfaceContainerLow,
        border = BorderStroke(1.dp, AppTheme.colorScheme.outlineVariant.copy(alpha = 0.55f)),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "⌕",
                style = AppTheme.typography.titleMedium,
                color = AppTheme.colorScheme.onSurfaceVariant,
            )
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                singleLine = true,
                textStyle = AppTheme.typography.bodyLarge.copy(color = AppTheme.colorScheme.onSurface),
                cursorBrush = SolidColor(AppTheme.colorScheme.primary),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                modifier = Modifier.weight(1f),
                decorationBox = { innerTextField ->
                    if (value.isBlank()) {
                        Text(
                            text = "Search movies",
                            style = AppTheme.typography.bodyLarge,
                            color = AppTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                    innerTextField()
                },
            )
            if (value.isNotBlank()) {
                Text(
                    text = "Clear",
                    style = AppTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = AppTheme.colorScheme.primary,
                    modifier = Modifier.clickable { onValueChange("") },
                )
            }
        }
    }
}

@Composable
private fun SearchMovieCard(
    movie: Movie,
    onClick: () -> Unit,
) {
    val height = when (movie.id % 5) {
        0 -> 260.dp
        1 -> 220.dp
        2 -> 300.dp
        3 -> 240.dp
        else -> 280.dp
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(height)
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onClick),
    ) {
        Image(
            painter = rememberAsyncImagePainter(posterUrl(movie.posterPath)),
            contentDescription = movie.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(),
        )

        ListingCardScrim()

        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(horizontal = 12.dp, vertical = 10.dp)
                .widthIn(max = 220.dp),
        ) {
            Text(
                text = movie.title,
                color = Color.White,
                style = AppTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
            if (movie.releaseDate.isNotBlank()) {
                Text(
                    text = movie.releaseDate.take(4),
                    color = Color.White.copy(alpha = 0.86f),
                    style = AppTheme.typography.bodySmall,
                )
            }
        }
    }
}
