package com.harrrshith.imagecarousel

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyListItemInfo
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp

@Composable
fun CarouselItemWrapper(
    screenWidth: Dp,
    itemHeight: Dp?,
    state: LazyListState,
    itemIndex: Int,
    content: @Composable ImageCarouselItemScope.() -> Unit
) {
    val itemWidth = screenWidth * 0.75f
    val density = LocalDensity.current
    val screenWidthPx = with(density) { screenWidth.toPx() }

    val itemInfo = state.layoutInfo.visibleItemsInfo.find { it.index == itemIndex }

    val scale by animateFloatAsState(
        targetValue = calculateScale(
            itemInfo = itemInfo,
            screenWidthPx = screenWidthPx
        ),
        animationSpec = tween(100)
    )

    val scope = remember(itemWidth, itemHeight, scale) {
        object : ImageCarouselItemScope {
            override val itemWidth = itemWidth
            override val itemHeight = itemHeight
            override val scale = scale
        }
    }

    Box(
        modifier = Modifier
            .width(itemWidth)
            .then(if (itemHeight != null) Modifier.height(itemHeight) else Modifier)
            .scale(scale),
        contentAlignment = Alignment.Center
    ) {
        scope.content()
    }
}

private fun calculateScale(
    itemInfo: LazyListItemInfo?,
    screenWidthPx: Float
): Float {
    if (itemInfo == null) return 1f

    val halfWidth = screenWidthPx / 2
    val itemCenter = itemInfo.offset + (itemInfo.size / 2)
    val distanceFromCenter = kotlin.math.abs(itemCenter - halfWidth)
    val scaleFactor = 1f - kotlin.math.min(1f, distanceFromCenter / halfWidth)

    return 1f + (0.2f * scaleFactor)
}
