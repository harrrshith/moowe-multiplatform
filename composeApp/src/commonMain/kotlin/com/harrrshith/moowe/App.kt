package com.harrrshith.moowe

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.harrrshith.imagecarousel.ImageCarousel
import com.harrrshith.imagecarousel.items
import com.harrrshith.moowe.ui.components.AppBottomBar
import com.harrrshith.moowe.ui.navigation.NavigationGraph
import com.harrrshith.moowe.ui.theme.AppTheme
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.rememberHazeState
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    val navController = rememberNavController()
    val hazeState = rememberHazeState(blurEnabled = true)
    AppTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            contentWindowInsets = WindowInsets.systemBars,
            bottomBar = {
                AppBottomBar(
                    navController = navController,
                    hazeState = hazeState
                )
            }
        ) {
            CompositionLocalProvider(LocalHazeState provides hazeState ) {
                NavigationGraph(
                    modifier = Modifier
                        .fillMaxSize(),
                    navController = navController
                )
            }
        }
    }
}
val LocalHazeState = staticCompositionLocalOf<HazeState> {
    error("No Haze State provided")
}