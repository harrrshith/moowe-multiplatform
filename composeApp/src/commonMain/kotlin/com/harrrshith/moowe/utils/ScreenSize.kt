package com.harrrshith.moowe.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp

@Composable
expect fun getScreenWidth(): Dp

@Composable
expect fun getScreenHeight(): Dp

val screenWidth: Dp
    @Composable
    get() = getScreenWidth()

val screenHeight: Dp
    @Composable
    get() = getScreenHeight()