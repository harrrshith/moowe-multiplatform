package com.harrrshith.moowe

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.harrrshith.moowe.theme.MooweTheme
import com.harrrshith.moowe.ui.components.MooweBottomBar
import com.harrrshith.moowe.ui.navigation.NavigationGraph
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    MooweTheme{
        val navHostController = rememberNavController()
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            contentWindowInsets = WindowInsets.systemBars,
            bottomBar = {
                MooweBottomBar(navController = navHostController)
            }
        ){
            NavigationGraph(
                modifier = Modifier
                    .fillMaxSize(),
                navController = navHostController
            )
        }
    }
}