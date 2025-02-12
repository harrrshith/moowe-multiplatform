package com.harrrshith.moowe

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.abs


@Composable
fun ImageCarousel(
    screenWidth: Dp = 200.dp,
    modifier: Modifier = Modifier
) {
    val colors = listOf(
        Color(0xFFFF0000),
        Color(0xFF00FF00),
        Color(0xFF0000FF),
        Color(0xFFFFFF00),
        Color(0xFF00FFFF),
        Color(0xFFFF00FF),
        Color(0xFFFFA500),
        Color(0xFFFFC0CB),
        Color(0xFFBFFF00),
        Color(0xFF40E0D0)
    )

    val itemWidth = remember { screenWidth * 0.7f }
    val horizontalPadding = remember { (screenWidth - itemWidth) / 2 }
    val lazyListState = rememberLazyListState()
    val snapBehavior = rememberSnapFlingBehavior(lazyListState)

    LazyRow(
        modifier = modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(vertical = 16.dp),
        state = lazyListState,
        contentPadding = PaddingValues(horizontal = horizontalPadding),
        horizontalArrangement = Arrangement.spacedBy(32.dp),
        flingBehavior = snapBehavior,
        userScrollEnabled = true
    ) {
        itemsIndexed(colors) { index, color ->
            CarouselItem(
                color = color,
                itemWidth = itemWidth,
                index = index,
                totalItems = colors.size,
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
    totalItems: Int,
    lazyListState: LazyListState
) {
    val scaleFactor = 1.2f
    val density = LocalDensity.current

    val itemInfo by remember {
        derivedStateOf {
            lazyListState.layoutInfo.visibleItemsInfo.firstOrNull { it.index == index }
        }
    }

    val distanceFromCenter by remember {
        derivedStateOf {
            val offset = itemInfo?.offset ?: 0
            calculateDistanceFromCenter(
                listState = lazyListState,
                offset = offset,
                itemIndex = index,
                totalItems = totalItems
            )
        }
    }

    val targetScale by remember {
        derivedStateOf {
            val itemSizeInPx = with(density) { itemWidth.toPx() }
            calculateScaleForDistance(
                distanceFromCenter = distanceFromCenter,
                itemSizeInPx = itemSizeInPx,
                scaleFactor = scaleFactor
            )
        }
    }

    val animatedScale by animateFloatAsState(
        targetValue = targetScale,
//        animationSpec = spring(
//            dampingRatio = 0.7f,
//            stiffness = 400f
//        ),
        animationSpec = tween(durationMillis = 100, easing = LinearEasing),
        label = "scale"
    )

    Box(
        modifier = Modifier
            .width(itemWidth)
            .aspectRatio(1.5f)
            .scale(animatedScale)
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

private fun calculateScaleForDistance(
    distanceFromCenter: Float,
    itemSizeInPx: Float,
    scaleFactor: Float
): Float {
    val distance = abs(distanceFromCenter / itemSizeInPx)
    return if (distance < 0.5f) {
        1f + (scaleFactor - 1f) * (1f - (distance / 0.5f))
    } else {
        1f
    }
}

private fun calculateDistanceFromCenter(
    listState: LazyListState,
    offset: Int,
    itemIndex: Int,
    totalItems: Int
): Float {
    val viewportWidth = listState.layoutInfo.viewportEndOffset - listState.layoutInfo.viewportStartOffset
    val center = viewportWidth / 2f
    val itemSize = listState.layoutInfo.visibleItemsInfo.firstOrNull()?.size ?: 0

    return when (itemIndex) {
        0 -> offset + (itemSize / 2f) - center
        totalItems - 1 -> offset + (itemSize / 2f) - center
        else -> offset + (itemSize / 2f) - center
    }
}