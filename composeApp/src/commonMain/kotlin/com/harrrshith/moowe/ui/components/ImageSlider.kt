package com.harrrshith.moowe.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun ImageSlider(
    modifier: Modifier = Modifier,
    colors: List<Color>,
    listState: LazyListState,
    screenWidth: Dp, //Important to calculate the itemWidth.
    contentHorizontalPadding: Dp = 8.dp,
    itemsShownPerScreen: Int = 3, // If 3, then 3 full item and half of the next Item is shown.
    itemHorizontalPadding: Dp = 4.dp,
    itemVerticalPadding: Dp = 4.dp
){
    val actualItemWidth = screenWidth / (itemsShownPerScreen + 0.5f)
    val horizontalPadding = remember { (screenWidth - actualItemWidth) / 11 } //we'll change this later
    LazyRow(
        modifier = Modifier
            .fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = horizontalPadding),
        state = listState
    ){
        items(items = colors) { color ->
            Box(
                modifier = modifier
                    .width(actualItemWidth)
                    .aspectRatio(.75f)
                    .padding(horizontal = itemHorizontalPadding, vertical = itemVerticalPadding)
                    .clip(RoundedCornerShape(12.dp))
                    .background(color)
            )
        }
    }
}
