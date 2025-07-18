package com.harrrshith.imagecarousel

import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import com.harrrshith.imagecarousel.utils.screenWidth

@Composable
fun ImageCarousel(
    modifier: Modifier = Modifier,
    state: LazyListState = rememberLazyListState(),
    itemWidthFraction: Float = 0.75f,
    width: Dp = screenWidth,
    itemHeight: Dp? = null,
    content: ImageCarouselScope.() -> Unit
) {
    val itemWidth = width * itemWidthFraction
    val contentPadding = (width - itemWidth) / 2
    val scope = ImageCarouselScopeImpl(width, itemHeight)
    LazyRow(
        modifier = modifier.wrapContentHeight(),
        state = state,
        contentPadding = PaddingValues(horizontal = contentPadding, vertical = contentPadding / 2),
        horizontalArrangement = Arrangement.spacedBy(contentPadding * 0.6f),
        verticalAlignment = Alignment.Top,
        flingBehavior = rememberSnapFlingBehavior(lazyListState = state, snapPosition = SnapPosition.Center)
    ) {
        scope.content()
        scope.items.forEach { item ->
            item(this, state)
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
