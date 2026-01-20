package com.harrrshith.imagecarousel

import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.harrrshith.imagecarousel.utils.screenWidth

@Composable
fun ImageCarousel(
    modifier: Modifier = Modifier,
    state: LazyListState = rememberLazyListState(),
    carouselWidth: Dp = screenWidth * 0.5f,
    itemHeight: Dp? = null,
    itemWidthFraction: Float = 0.75f,
    spacing: Dp = 16.dp,
    contentPadding: PaddingValues = PaddingValues(
        horizontal = (carouselWidth - (carouselWidth * itemWidthFraction)) / 2
    ),
    flingBehavior: FlingBehavior = rememberSnapFlingBehavior(
        lazyListState = state,
        snapPosition = SnapPosition.Center
    ),
    content: ImageCarouselScope.() -> Unit
) {
    LazyRow(
        modifier = modifier,
        state = state,
        contentPadding = contentPadding,
        horizontalArrangement = Arrangement.spacedBy(spacing),
        verticalAlignment = Alignment.CenterVertically,
        flingBehavior = flingBehavior
    ) {
        val scope = ImageCarouselScopeImpl(
            lazyListScope = this,
            state = state,
            screenWidth = carouselWidth,
            itemHeight = itemHeight,
            itemWidthFraction = itemWidthFraction
        )
        scope.content()
    }
}

inline fun <T> ImageCarouselScope.items(
    items: List<T>,
    noinline key: ((item: T) -> Any)? = null,
    noinline contentType: (item: T) -> Any? = { null },
    crossinline itemContent: @Composable ImageCarouselItemScope.(item: T) -> Unit
) = items(
    count = items.size,
    key = if (key != null) { index: Int -> key(items[index]) } else null,
    contentType = { index: Int -> contentType(items[index]) }
) { index ->
    itemContent(items[index])
}
