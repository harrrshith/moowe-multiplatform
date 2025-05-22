package com.harrrshith.moowe.ui.components.imageCarousel

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp

class ImageCarouselScopeImpl(
    private val screenWidth: Dp
) : ImageCarouselScope {
    val items = mutableListOf<(LazyListScope, LazyListState) -> Unit>()

    override fun item(
        key: Any?,
        contentType: Any?,
        content: @Composable ImageCarouselItemScope.() -> Unit
    ) {
        items.add { listScope, state ->
            listScope.item(key = key, contentType = contentType) {
                CarouselItemWrapper(
                    screenWidth = screenWidth,
                    state = state,
                    itemIndex = 0,
                    content = content
                )
            }
        }
    }

    override fun items(
        count: Int,
        key: ((index: Int) -> Any)?,
        contentType: (index: Int) -> Any?,
        itemContent: @Composable ImageCarouselItemScope.(index: Int) -> Unit
    ) {
        items.add { listScope, state ->
            listScope.items(
                count = count,
                key = key,
                contentType = contentType
            ) { index ->
                CarouselItemWrapper(
                    screenWidth = screenWidth,
                    state = state,
                    itemIndex = index,
                    content = { itemContent(index) }
                )
            }
        }
    }
}