@file:OptIn(ExperimentalMaterial3Api::class)
package com.harrrshith.moowe

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
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
            topBar = {
                TopAppBar(title = { Text("Moowe") })
            },
            bottomBar = {
                MooweBottomBar(navController = navHostController)
            }
        ){ padding ->
            NavigationGraph(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                navController = navHostController
            )
        }
    }
}