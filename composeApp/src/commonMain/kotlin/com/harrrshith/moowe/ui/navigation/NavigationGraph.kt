package com.harrrshith.moowe.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.harrrshith.moowe.ui.onBoarding.OnBoardingRoute

@Composable
fun NavigationGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    val startDestination = Destination.OnBoard
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination
    ){
        composable<Destination.OnBoard> {
            OnBoardingRoute()
        }
    }
}
