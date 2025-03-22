package com.harrrshith.moowe.ui.discover

import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.harrrshith.moowe.data.remote.MovieApi
import com.harrrshith.moowe.data.repository.MovieRepositoryImpl
import com.harrrshith.moowe.domain.repository.MovieRepository
import com.harrrshith.moowe.log
import com.harrrshith.moowe.ui.components.ImageCarousel
import com.harrrshith.moowe.ui.components.ImageSlider
import com.harrrshith.moowe.width
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject

@Composable
fun DiscoverRoute(){
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
    val lazyListState = rememberLazyListState()
    val flingBehavior = rememberSnapFlingBehavior(lazyListState)
    val screenWidth = width
    val verticalScrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .verticalScroll(verticalScrollState),
    ){
        ImageCarousel(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            colors = colors,
            screenWidth = screenWidth,
            itemWidth = 0.75f,
            horizontalArrangement = Arrangement.spacedBy(32.dp),
            lazyListState = lazyListState,
            flingBehavior = flingBehavior,
        )


        repeat(5){
            val imageSliderList = rememberLazyListState()
            Spacer(modifier = Modifier.height(16.dp))
            ImageSlider(
                listState = imageSliderList,
                colors = colors,
                screenWidth = screenWidth,
                contentHorizontalPadding = 16.dp,
                itemsShownPerScreen = 4,
            )
        }
    }
    val scope  = rememberCoroutineScope()
    val repository = koinInject<MovieRepository>()
    LaunchedEffect(Unit) {
        scope.launch {
            val result = repository.getTrendingMovies()
            result.collect { response ->
                log("$response")
            }
        }
    }
}

@Preview
@Composable
fun AppPreview(){
    DiscoverRoute()
}

