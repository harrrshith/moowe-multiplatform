package com.harrrshith.moowe.ui.discover

import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.harrrshith.moowe.ui.components.ImageCarousel
import com.harrrshith.moowe.width

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
    ImageCarousel(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 16.dp)
            .statusBarsPadding(),
        colors = colors,
        screenWidth = width,
        itemWidth = 0.7f,
        horizontalArrangement = Arrangement.spacedBy(32.dp),
        lazyListState = lazyListState,
        flingBehavior = flingBehavior,
    )
}

