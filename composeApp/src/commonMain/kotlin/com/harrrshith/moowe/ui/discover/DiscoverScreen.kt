package com.harrrshith.moowe.ui.discover

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.harrrshith.moowe.domain.model.Movie
import com.harrrshith.moowe.ui.components.ImageCarousel
import com.harrrshith.moowe.ui.components.ImageSlider
import com.harrrshith.moowe.ui.discover.components.ImageCard
import com.harrrshith.moowe.width
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun DiscoverRoute(
    viewModel: DiscoverViewModel = koinViewModel()
){
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val lazyListState = rememberLazyListState()
    val flingBehavior = rememberSnapFlingBehavior(lazyListState)
    val verticalScrollState = rememberScrollState()

    DiscoverScreen(
        screenWidth = width,
        verticalScrollState = verticalScrollState,
        lazyListState = lazyListState,
        flingBehavior = flingBehavior,
        movies = uiState.movies
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DiscoverScreen(
    screenWidth: Dp,
    verticalScrollState: ScrollState,
    lazyListState: LazyListState,
    flingBehavior: FlingBehavior,
    movies: List<Movie>
){
    val scrollBarBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Moowe",
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Bold
                    )
                },
                scrollBehavior = scrollBarBehavior,
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .verticalScroll(verticalScrollState)
                .nestedScroll(scrollBarBehavior.nestedScrollConnection),
        ){
            ImageCarousel(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                screenWidth = screenWidth,
                horizontalArrangement = Arrangement.spacedBy(32.dp),
                lazyListState = lazyListState,
                flingBehavior = flingBehavior,
                items = movies,
            ) { item, index ->
                ImageCard(
                    modifier = Modifier
                        .aspectRatio(16f/9)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.Gray),
                    imageUrl = item.backdropPath,
                    movieTitle = item.title
                )
            }
            val colors = listOf(
                Color(0xFFFF0000),
                Color(0xFF00FF00),
                Color(0xFF0000FF),
                Color(0xFFFFFF00),
                Color(0xFF00FFFF),
                Color(0xFFFF00FF),
                Color(0xFFFFA500),
                Color(0xFFFFC0CB),
                Color(0xFFBFFF00),
                Color(0xFF40E0D0)
            )

            repeat(10){
                val imageSliderList = rememberLazyListState()
                Spacer(modifier = Modifier.height(16.dp))
                ImageSlider(
                    listState = imageSliderList,
                    colors = colors,
                    screenWidth = screenWidth,
                    contentHorizontalPadding = 16.dp,
                    itemsShownPerScreen = 3,
                )
            }
        }
    }
}

@Preview
@Composable
fun AppPreview(){
    DiscoverScreen(
        screenWidth = width,
        verticalScrollState = rememberScrollState(),
        lazyListState = rememberLazyListState(),
        flingBehavior = rememberSnapFlingBehavior(rememberLazyListState()),
        movies = movies
    )
}

val movies: List<Movie>
    get() = listOf(
        Movie(
            id = 1,
            title = "Movie 1",
            overview = "Overview",
            backdropPath = "backdropPath",
            posterPath = "posterPath",
            releaseDate = "2022-01-01",
            voteAverage = 7.5,
            voteCount = 1000,
            popularity = 1000.0,
            adult = false,
            genreIds = listOf(1, 2, 3),
        )
    )
