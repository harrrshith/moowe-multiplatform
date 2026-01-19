package com.harrrshith.moowe

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.harrrshith.moowe.ui.components.AppBottomBar
import com.harrrshith.moowe.ui.navigation.NavigationGraph
import com.harrrshith.moowe.ui.theme.AppTheme
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.rememberHazeState

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