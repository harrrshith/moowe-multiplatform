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

    val transformation by remember(itemIndex, state, screenWidthPx) {
        derivedStateOf {
            val visibleItems = state.layoutInfo.visibleItemsInfo
            val itemInfo = visibleItems.find { it.index == itemIndex }
            calculateTransformation(itemInfo, screenWidthPx)
        }
    }

    val scope = remember(itemWidth, itemHeight, transformation) {
        object : ImageCarouselItemScope {
            override val itemWidth = itemWidth
            override val itemHeight = itemHeight
            override val scale: Float
                get() = transformation.scale
        }
    }

    Box(
        modifier = Modifier
            .width(itemWidth)
            .then(if (itemHeight != null) Modifier.height(itemHeight) else Modifier.wrapContentHeight())
            .graphicsLayer {
                scaleX = transformation.scale
                scaleY = transformation.scale
                alpha = transformation.alpha
            },
        contentAlignment = Alignment.Center
    ) {
        scope.content()
    }
}

private data class ItemTransformation(
    val scale: Float,
    val alpha: Float
)

private fun calculateTransformation(
    itemInfo: LazyListItemInfo?,
    screenWidthPx: Float
): ItemTransformation {
    if (itemInfo == null) return ItemTransformation(0.8f, 0.6f)

    val center = screenWidthPx / 2f
    val itemCenter = itemInfo.offset + (itemInfo.size / 2f)
    val distanceFromCenter = itemCenter - center
    val absDistance = kotlin.math.abs(distanceFromCenter)
    
    // Scale range 1.0 to 0.8
    val fraction = (absDistance / center).coerceIn(0f, 1f)
    val scaleCurve = 1f - (fraction * fraction)
    val scale = 0.8f + (0.2f * scaleCurve)
    
    // Alpha range 1.0 to 0.6
    val alpha = 0.6f + (0.4f * scaleCurve)
    
    return ItemTransformation(scale, alpha)
}
