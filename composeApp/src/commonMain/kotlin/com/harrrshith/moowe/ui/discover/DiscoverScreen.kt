package com.harrrshith.moowe.ui.discover

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.harrrshith.moowe.ui.components.ImageCarousel
import com.harrrshith.moowe.ui.components.ImageSlider
import com.harrrshith.moowe.width
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun DiscoverRoute(
    viewModel: DiscoverViewModel = koinViewModel()
){
    val uiState by viewModel.uiState.collectAsState()
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

    println("Movies: ${uiState.movies}")
    LaunchedEffect(uiState) {
        println("Movies: ${uiState.movies}")
    }
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
            screenWidth = screenWidth,
            horizontalArrangement = Arrangement.spacedBy(32.dp),
            lazyListState = lazyListState,
            flingBehavior = flingBehavior,
            items = colors,
        ) { item, index ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f/9)
                    .clip(RoundedCornerShape(16.dp))
                    .background(item)
            )
        }


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
}

@Preview
@Composable
fun AppPreview(){
    DiscoverRoute()
}

