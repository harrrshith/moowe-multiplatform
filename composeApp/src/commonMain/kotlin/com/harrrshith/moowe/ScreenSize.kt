package com.harrrshith.moowe

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp

@Composable
expect fun getScreenWidth(): Dp

@Composable
expect fun getScreenHeight(): Dp

val width: Dp
    @Composable
    get() = getScreenWidth()

val height: Dp
    @Composable
    get() = getScreenHeight()