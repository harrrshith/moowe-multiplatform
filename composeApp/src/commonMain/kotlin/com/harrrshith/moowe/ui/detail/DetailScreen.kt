@file:OptIn(ExperimentalMaterial3Api::class)
package com.harrrshith.moowe.ui.detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
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
    Scaffold(
        topBar = {
            MooweTopAppBar(
                movie = movie,
                onBackPressed = onBackPressed,
                onLikeClicked = {},
                onShareClicked = {}
            )
        }
    ) { paddingValues ->
        LazyColumn(
            contentPadding = paddingValues
        ) {
            banner(url = movie.backdropPath)
            title(title = movie.title)
            description(description = movie.overview)
        }

    }
}

private fun LazyListScope.banner(
    url: String
){
    item {
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16 / 9f)
                .bottomGradientOverlay()
            ,
            painter = rememberAsyncImagePainter("$IMAGE_BASE_URL/$url"),
            contentDescription = null,
            contentScale = ContentScale.Fit
        )
    }
}

private fun LazyListScope.title(
    title: String
) {
    item {
        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
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
    movie: Movie,
    onBackPressed: () -> Unit,
    onLikeClicked: (Int) -> Unit,
    onShareClicked: (Int) -> Unit
) {
    TopAppBar(
        navigationIcon = {
            IconButton(onClick = onBackPressed) {
                Image(
                    imageVector = ArrowBackIcon,
                    contentDescription = "Back",
                    colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.secondary)
                )
            }
        },
        title = {  },
        actions = {
            IconButton(onClick = { onLikeClicked(movie.id) }) {
                Image(
                    imageVector = LikeIcon,
                    contentDescription = "Like",
                    colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.secondary)
                )
            }
            IconButton(onClick = { onShareClicked(movie.id) }) {
                Image(
                    imageVector = ShareIcon,
                    contentDescription = "Share",
                    colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.secondary)
                )
            }
        }
    )
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
