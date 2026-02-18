@file:OptIn(ExperimentalMaterial3Api::class)
package com.harrrshith.moowe.ui.detail

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.harrrshith.imagecarousel.utils.screenWidth
import com.harrrshith.moowe.domain.model.Movie
import com.harrrshith.moowe.ui.detail.components.DetailTopAppBar
import com.harrrshith.moowe.ui.detail.components.detailImage
import com.harrrshith.moowe.ui.detail.components.detailLineTwo
import com.harrrshith.moowe.ui.detail.components.detailOverview
import com.harrrshith.moowe.ui.detail.mock.mockMovie
import com.harrrshith.moowe.ui.theme.AppTheme
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun DetailRoute(
    sharedKey: String,
    animatedContentScope: AnimatedContentScope,
    sharedTransitionScope: SharedTransitionScope,
    viewModel: DetailScreenViewModel = koinViewModel(),
    onBackPressed: () -> Unit
){
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    uiState.movie?.let { movie ->
        DetailScreen(
            sharedKey = sharedKey,
            animatedContentScope = animatedContentScope,
            sharedTransitionScope = sharedTransitionScope,
            movie = movie,
            onBackPressed = onBackPressed
        )
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun DetailScreen(
    sharedKey: String,
    animatedContentScope: AnimatedContentScope,
    sharedTransitionScope: SharedTransitionScope,
    movie: Movie,
    onBackPressed: () -> Unit
){
    val scrollState = rememberLazyListState()
    val density = LocalDensity.current
    val posterHeight = screenWidth * 1.4f
    val topBarStartThreshold = with(density) { (posterHeight - 120.dp).toPx() }
    val topBarTransitionRange = with(density) { 120.dp.toPx() }

    val scrollOffset by remember {
        derivedStateOf {
            if (scrollState.firstVisibleItemIndex == 0) {
                scrollState.firstVisibleItemScrollOffset.toFloat()
            } else {
                with(density) { (posterHeight + 100.dp).toPx() }
            }
        }
    }

    val topBarAlpha by remember {
        derivedStateOf {
            when {
                scrollState.firstVisibleItemIndex > 0 -> 1f
                scrollOffset < topBarStartThreshold -> 0f
                else -> ((scrollOffset - topBarStartThreshold) / topBarTransitionRange).coerceIn(0f, 1f)
            }
        }
    }

    Scaffold(
        topBar = {
            DetailTopAppBar(
                title = movie.title,
                alpha = topBarAlpha,
                onBackPressed = onBackPressed,
                onLikeClicked = { },
                onShareClicked = { }
            )
        },
        containerColor = AppTheme.colorScheme.surface
    ) { _ ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
            state = scrollState
        ) {
            detailImage(
                movie = movie,
                scrollOffset = scrollOffset,
                posterHeight = posterHeight,
                sharedKey = sharedKey,
                animatedContentScope = animatedContentScope,
                sharedTransitionScope = sharedTransitionScope
            )

            item {
                Text(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth(),
                    text = movie.title,
                    style = AppTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold, letterSpacing = 0.5.sp)
                )
            }

            detailLineTwo(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 20.dp),
                movie = movie
            )


            detailOverview(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 20.dp),
                movie = movie
            )

            item {
                Spacer(modifier = Modifier.height(100.dp))
            }
        }
    }
}


@Preview
@Composable
fun DetailScreenPreview() {
    AppTheme {
        Surface {
            SharedTransitionLayout {
                AnimatedContent(
                    targetState = mockMovie,
                    label = "preview"
                ) { movie ->
                    DetailScreen(
                        sharedKey = "movie-${movie.id}",
                        animatedContentScope = this,
                        sharedTransitionScope = this@SharedTransitionLayout,
                        movie = movie,
                        onBackPressed = {}
                    )
                }
            }
        }
    }
}