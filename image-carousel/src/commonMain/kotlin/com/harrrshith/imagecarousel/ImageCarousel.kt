package com.harrrshith.imagecarousel

import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun ImageCarousel(
    modifier: Modifier = Modifier,
    state: LazyListState = rememberLazyListState(),
    screenWidth: Dp = com.harrrshith.imagecarousel.utils.screenWidth,
    itemHeight: Dp? = null,
    itemWidthFraction: Float = 0.8f,
    spacing: Dp = 2.dp,
    flingBehavior: FlingBehavior = rememberSnapFlingBehavior(
        lazyListState = state,
        snapPosition = SnapPosition.Center
    ),
    userScrollEnabled: Boolean = true,
    content: ImageCarouselScope.() -> Unit
) {
    LazyRow(
        modifier = modifier,
        state = state,
        userScrollEnabled = userScrollEnabled,
        horizontalArrangement = Arrangement.spacedBy(spacing),
        verticalAlignment = Alignment.CenterVertically,
        flingBehavior = flingBehavior
    ) {
        val scope = ImageCarouselScopeImpl(
            lazyListScope = this,
            state = state,
            screenWidth = screenWidth,
            itemHeight = itemHeight,
            itemWidthFraction = itemWidthFraction,
        )
        scope.content()
    }
}
