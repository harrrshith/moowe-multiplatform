package com.harrrshith.imagecarousel.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

val screenWidth: Dp
    @Composable
    get() = LocalWindowInfo.current.containerSize.width.dp

val screenHeight: Dp
    @Composable
    get() = LocalWindowInfo.current.containerSize.height.dp
