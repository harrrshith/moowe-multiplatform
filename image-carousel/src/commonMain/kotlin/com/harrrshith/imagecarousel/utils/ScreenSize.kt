package com.harrrshith.imagecarousel.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.Dp

val screenWidth: Dp
    @Composable
    get() = with(LocalDensity.current) { LocalWindowInfo.current.containerSize.width.toDp() }

val screenHeight: Dp
    @Composable
    get() = with(LocalDensity.current) { LocalWindowInfo.current.containerSize.height.toDp() }
