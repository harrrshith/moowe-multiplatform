package com.harrrshith.imagecarousel

import androidx.compose.runtime.Composable

interface ImageCarouselScope {
    fun item(
        key: Any? = null,
        contentType: Any? = null,
        content: @Composable ImageCarouselItemScope.() -> Unit
    )
    fun items(
        count: Int,
        key: ((index: Int) -> Any)? = null,
        contentType: (index: Int) -> Any? = { null },
        itemContent: @Composable ImageCarouselItemScope.(index: Int) -> Unit
    )
}
