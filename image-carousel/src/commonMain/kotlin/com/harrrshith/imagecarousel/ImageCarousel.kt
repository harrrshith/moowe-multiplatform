package com.harrrshith.imagecarousel

import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
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
    carouselWidth: Dp = screenWidth,
    itemHeight: Dp? = null,
    itemWidthFraction: Float = 0.68f, // Reduced to allow more peek space
    spacing: Dp = 12.dp, // Tighter spacing
    flingBehavior: FlingBehavior = rememberSnapFlingBehavior(
        lazyListState = state,
        snapPosition = SnapPosition.Center
    ),
    userScrollEnabled: Boolean = true,
    content: ImageCarouselScope.() -> Unit
) {
    val sidePadding = (carouselWidth - (carouselWidth * itemWidthFraction)) / 2
    val spacerWidth = (sidePadding - spacing).coerceAtLeast(0.dp)

    LazyRow(
        modifier = modifier,
        state = state,
        userScrollEnabled = userScrollEnabled,
        horizontalArrangement = Arrangement.spacedBy(spacing),
        verticalAlignment = Alignment.CenterVertically,
        flingBehavior = flingBehavior
    ) {
        // Start spacer to center the first item
        item {
            Spacer(modifier = Modifier.width(spacerWidth))
        }

        val scope = ImageCarouselScopeImpl(
            lazyListScope = this,
            state = state,
            screenWidth = carouselWidth,
            itemHeight = itemHeight,
            itemWidthFraction = itemWidthFraction,
            indexOffset = 1 // Account for the spacer
        )
        scope.content()

        // End spacer to center the last item
        item {
            Spacer(modifier = Modifier.width(spacerWidth))
        }
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
