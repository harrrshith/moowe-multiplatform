package com.harrrshith.imagecarousel

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyListItemInfo
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp

@Composable
fun CarouselItemWrapper(
    screenWidth: Dp,
    itemHeight: Dp?,
    itemWidthFraction: Float,
    state: LazyListState,
    itemIndex: Int,
    content: @Composable ImageCarouselItemScope.() -> Unit
) {
    val itemWidth = screenWidth * itemWidthFraction
    val density = LocalDensity.current
    val screenWidthPx = with(density) { screenWidth.toPx() }

    val scaleState = remember(itemIndex, state, screenWidthPx) {
        derivedStateOf {
            val visibleItems = state.layoutInfo.visibleItemsInfo
            val itemInfo = visibleItems.find { it.index == itemIndex }
            calculateScale(itemInfo, screenWidthPx)
        }
    }

    val scope = remember(itemWidth, itemHeight, scaleState) {
        object : ImageCarouselItemScope {
            override val itemWidth = itemWidth
            override val itemHeight = itemHeight
            override val scale: Float
                get() = scaleState.value
        }
    }

    Box(
        modifier = Modifier
            .width(itemWidth)
            .then(if (itemHeight != null) Modifier.height(itemHeight) else Modifier.wrapContentHeight())
            .graphicsLayer {
                val s = scaleState.value
                scaleX = s
                scaleY = s
                alpha = 0.6f + 0.4f * ((s - 0.8f) / 0.2f).coerceIn(0f, 1f)
            },
        contentAlignment = Alignment.Center
    ) {
        scope.content()
    }
}

private fun calculateScale(
    itemInfo: LazyListItemInfo?,
    screenWidthPx: Float
): Float {
    if (itemInfo == null) return 0.8f

    val center = screenWidthPx / 2f
    val itemCenter = itemInfo.offset + (itemInfo.size / 2f)
    val distanceFromCenter = kotlin.math.abs(itemCenter - center)
    
    val fraction = (distanceFromCenter / center).coerceIn(0f, 1f)
    return 1f - (0.2f * fraction)
}
