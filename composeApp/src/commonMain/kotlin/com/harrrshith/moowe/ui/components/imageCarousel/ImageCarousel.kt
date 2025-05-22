package com.harrrshith.moowe.ui.components.imageCarousel

import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.harrrshith.moowe.utils.screenWidth

@Composable
fun ImageCarousel(
    modifier: Modifier = Modifier,
    state: LazyListState = rememberLazyListState(),
    contentPadding: PaddingValues = PaddingValues(0.dp),
    itemWidthFraction: Float = 0.75f,
    content: ImageCarouselScope.() -> Unit
) {
    val screenWidth = screenWidth
    val itemWidth = screenWidth * itemWidthFraction
    val sideContentPadding = (screenWidth - itemWidth) / 2
    val scope = ImageCarouselScopeImpl(screenWidth)
    LazyRow(
        modifier = modifier,
        state = state,
        contentPadding = PaddingValues(
            start = sideContentPadding + contentPadding.calculateStartPadding(LayoutDirection.Ltr),
            end = sideContentPadding + contentPadding.calculateEndPadding(LayoutDirection.Ltr),
            top = contentPadding.calculateTopPadding(),
            bottom = contentPadding.calculateBottomPadding()
        ),
        horizontalArrangement = Arrangement.Center,
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