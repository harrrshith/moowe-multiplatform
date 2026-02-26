package com.harrrshith.moowe

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import com.harrrshith.moowe.ui.components.AppBottomBar
import com.harrrshith.moowe.ui.navigation.Destination
import com.harrrshith.moowe.ui.navigation.NavigationGraph
import com.harrrshith.moowe.ui.theme.AppTheme
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.rememberHazeState

@Composable
fun App() {
    val backStack = remember { mutableStateListOf<Destination>(Destination.Home.Discover) }
    val hazeState = rememberHazeState(blurEnabled = true)
    AppTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            contentWindowInsets = WindowInsets.systemBars,
            bottomBar = {
                AppBottomBar(
                    backStack = backStack,
                    onDestinationSelected = { destination ->
                        if (backStack.lastOrNull() != destination) {
                            backStack.clear()
                            backStack.add(destination)
                        }
                    },
                    hazeState = hazeState
                )
            }
        ) {
            CompositionLocalProvider(LocalHazeState provides hazeState ) {
                NavigationGraph(
                    modifier = Modifier
                        .fillMaxSize(),
                    backStack = backStack
                )
            }
        }
    }
}
val LocalHazeState = staticCompositionLocalOf<HazeState> {
    error("No Haze State provided")
}
