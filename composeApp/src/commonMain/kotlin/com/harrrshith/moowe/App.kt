package com.harrrshith.moowe

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.harrrshith.moowe.theme.MooweTheme
import com.harrrshith.moowe.ui.navigation.NavigationGraph
import com.harrrshith.moowe.ui.onBoarding.OnBoardingRoute
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    MooweTheme{
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
//            OnBoardingRoute()
            val navHostController = rememberNavController()
            NavigationGraph(
                modifier = Modifier
                    .fillMaxSize(),
                navController = navHostController
            )
        }
    }
}