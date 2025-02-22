package com.harrrshith.moowe.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.harrrshith.moowe.width
import kotlin.math.abs
import kotlin.math.min

@Composable
fun ImageCarousel(
    modifier: Modifier = Modifier,
    colors: List<Color>, //Will change this to actual content
    horizontalArrangement: Arrangement.Horizontal = Arrangement.spacedBy(8.dp),
    screenWidth: Dp = 200.dp,
    itemWidth: Float = 0.7f,
    lazyListState: LazyListState,
    flingBehavior: FlingBehavior?,
){
    val itemWidth = remember { screenWidth * itemWidth }
    val horizontalPadding = remember { (screenWidth - itemWidth) / 2  }

    LazyRow(
        modifier = modifier,
        state = lazyListState,
        contentPadding = PaddingValues(horizontal = horizontalPadding),
        horizontalArrangement = horizontalArrangement,
        flingBehavior = flingBehavior ?: ScrollableDefaults.flingBehavior(),
        userScrollEnabled = true
    ) {
        itemsIndexed(colors) { index, color ->
            CarouselItem(
                color = color,
                itemWidth = itemWidth,
                index = index,
                lazyListState = lazyListState
            )
        }
    }
}

@Composable
private fun CarouselItem(
    color: Color,
    itemWidth: Dp,
    index: Int,
    lazyListState: LazyListState
) {
    val density = LocalDensity.current
    val maxWidthPx = with(density) { width.toPx() }

    val scale = calculateItemScale(
        listState = lazyListState,
        index = index,
        maxWidthPx = maxWidthPx,
        maxScale = 1.2f,
        minScale = 1.0f
    )

    Box(
        modifier = Modifier
            .width(itemWidth)
            .aspectRatio(1.5f)
            .scale(scale.value)
            .clip(RoundedCornerShape(12.dp))
    ) {
        Box(
            modifier = Modifier
                .width(itemWidth)
                .aspectRatio(1.5f)
                .background(color)
        )
    }
}

@Composable
fun calculateItemScale(
    listState: LazyListState,
    index: Int,
    maxWidthPx: Float,
    maxScale: Float = 1.2f,
    minScale: Float = 1.0f
): State<Float> {
    return remember {
        derivedStateOf {
            val currentItem = listState.layoutInfo.visibleItemsInfo
                .firstOrNull { it.index == index }
                ?: return@derivedStateOf 1f

            val halfWidth = maxWidthPx / 2
            val itemCenter = currentItem.offset + (currentItem.size / 2)
            val distanceFromCenter = abs(itemCenter - halfWidth)

            val scaleRange = maxScale - minScale
            val scaleFactor = 1f - min(1f, distanceFromCenter / halfWidth)
            minScale + (scaleRange * scaleFactor)
        }
    }
}