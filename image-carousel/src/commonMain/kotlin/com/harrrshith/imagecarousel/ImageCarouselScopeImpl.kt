package com.harrrshith.imagecarousel

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp

internal class ImageCarouselScopeImpl(
    private val lazyListScope: LazyListScope,
    private val state: LazyListState,
    private val screenWidth: Dp,
    private val itemHeight: Dp?,
    private val itemWidthFraction: Float
) : ImageCarouselScope {
    private var totalItems = 0

    override fun item(key: Any?, contentType: Any?, content: @Composable ImageCarouselItemScope.() -> Unit) {
        val currentIndex = totalItems
        totalItems += 1
        lazyListScope.item(key, contentType) {
            CarouselItemWrapper(
                screenWidth = screenWidth,
                itemHeight = itemHeight,
                itemWidthFraction = itemWidthFraction,
                state = state,
                itemIndex = currentIndex,
                content = content
            )
        }
    }

    override fun items(count: Int, key: ((index: Int) -> Any)?, contentType: (index: Int) -> Any?, itemContent: @Composable ImageCarouselItemScope.(index: Int) -> Unit) {
        val startIndex = totalItems
        totalItems += count
        lazyListScope.items(count, key, contentType) { index ->
            CarouselItemWrapper(
                screenWidth = screenWidth,
                itemHeight = itemHeight,
                itemWidthFraction = itemWidthFraction,
                state = state,
                itemIndex = startIndex + index
            ) {
                itemContent(index)
            }
        }
    }
}
