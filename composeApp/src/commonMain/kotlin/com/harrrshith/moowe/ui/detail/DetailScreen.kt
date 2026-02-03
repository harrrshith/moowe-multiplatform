@file:OptIn(ExperimentalMaterial3Api::class)
package com.harrrshith.moowe.ui.detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.rememberAsyncImagePainter
import com.harrrshith.moowe.Constants.IMAGE_BASE_URL
import com.harrrshith.moowe.domain.model.Movie
import com.harrrshith.moowe.ui.components.composeVectors.ArrowBackIcon
import com.harrrshith.moowe.ui.components.composeVectors.LikeIcon
import com.harrrshith.moowe.ui.components.composeVectors.ShareIcon
import com.harrrshith.moowe.ui.components.modifierExtensions.bottomGradientOverlay
import com.harrrshith.moowe.ui.detail.mock.mockMovie
import com.harrrshith.moowe.ui.theme.AppTheme
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun DetailRoute(
    viewModel: DetailScreenViewModel = koinViewModel(),
    onBackPressed: () -> Unit
){
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    uiState.movie?.let { movie ->
        DetailScreen(
            movie = movie,
            onBackPressed = onBackPressed
        )
    }
}

@Composable
private fun DetailScreen(
    movie: Movie,
    onBackPressed: () -> Unit
){
    BoxWithConstraints {
        val screenWidth = maxWidth
        val headerHeight = screenWidth * (9f / 16f)
        val scrollState = rememberLazyListState()
        val density = LocalDensity.current

        val headerScrollOffset by remember {
            derivedStateOf {
                if (scrollState.firstVisibleItemIndex == 0) {
                    scrollState.firstVisibleItemScrollOffset.toFloat()
                } else {
                    with(density) { headerHeight.toPx() }
                }
            }
        }

        val topBarAlpha by remember {
            derivedStateOf {
                val collapsedHeight = with(density) { (headerHeight - 64.dp).toPx() }
                if (headerScrollOffset > collapsedHeight) 1f else (headerScrollOffset / collapsedHeight).coerceIn(0f, 1f)
            }
        }

        Box(modifier = Modifier.fillMaxSize()) {
            // Parallax Header (Background)
            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(headerHeight)
                    .graphicsLayer {
                        translationY = -headerScrollOffset / 2f
                        alpha = 1f - topBarAlpha
                    }
                    .bottomGradientOverlay(),
                painter = rememberAsyncImagePainter("$IMAGE_BASE_URL/${movie.backdropPath}"),
                contentDescription = null,
                contentScale = ContentScale.Fit
            )

            LazyColumn(
                state = scrollState,
                modifier = Modifier.fillMaxSize()
            ) {
                item {
                    Spacer(modifier = Modifier.height(headerHeight))
                }
                title(title = movie.title)
                description(description = movie.overview)
                item {
                    Spacer(modifier = Modifier.height(headerHeight))
                }
                title(title = movie.title)
                description(description = movie.overview)
                item {
                    Spacer(modifier = Modifier.height(headerHeight))
                }
                title(title = movie.title)
                description(description = movie.overview)
            }

            // Top App Bar (Foreground)
            MooweTopAppBar(
                modifier = Modifier.statusBarsPadding(),
                title = movie.title,
                backdropPath = movie.backdropPath,
                alpha = topBarAlpha,
                onBackPressed = onBackPressed,
                onLikeClicked = { },
                onShareClicked = { }
            )
        }
    }
}

private fun LazyListScope.title(
    title: String
) {
    item {
        Text(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            text = title,
            style = MaterialTheme.typography.headlineLarge
        )
    }
}

private fun LazyListScope.description(
    description: String
) {
    item {
        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = description,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}


@Composable
private fun MooweTopAppBar(
    modifier: Modifier = Modifier,
    title: String,
    backdropPath: String?,
    alpha: Float,
    onBackPressed: () -> Unit,
    onLikeClicked: (Int) -> Unit,
    onShareClicked: (Int) -> Unit
) {
    Box(modifier = modifier) {
        // Dynamic Background
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp) // Standard TopAppBar height
                .graphicsLayer { this.alpha = alpha }
                .background(MaterialTheme.colorScheme.surface)
        ) {
             if (backdropPath != null) {
                 Image(
                     painter = rememberAsyncImagePainter("$IMAGE_BASE_URL/$backdropPath"),
                     contentDescription = null,
                     contentScale = ContentScale.Crop,
                     modifier = Modifier.fillMaxSize(),
                     alpha = 0.3f // Subtle background image in toolbar
                 )
             }
        }

        TopAppBar(
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Transparent,
                scrolledContainerColor = Color.Transparent
            ),
            navigationIcon = {
                IconButton(onClick = onBackPressed) {
                    Image(
                        imageVector = ArrowBackIcon,
                        contentDescription = "Back",
                        colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.onSurface)
                    )
                }
            },
            title = {
                if (alpha > 0.8f) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 1
                    )
                }
            },
            actions = {
                IconButton(onClick = { onLikeClicked(0) }) {
                    Image(
                        imageVector = LikeIcon,
                        contentDescription = "Like",
                        colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.onSurface)
                    )
                }
                IconButton(onClick = { onShareClicked(0) }) {
                    Image(
                        imageVector = ShareIcon,
                        contentDescription = "Share",
                        colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.onSurface)
                    )
                }
            }
        )
    }
}

@Preview
@Composable
fun PreviewDetailScreen() {
    AppTheme {
        Surface {
            DetailScreen(
                movie = mockMovie,
                onBackPressed = { }
            )
        }
    }
}