package com.harrrshith.moowe.ui.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.harrrshith.moowe.ImageCarousel
import com.harrrshith.moowe.ui.onBoarding.OnBoardingRoute
import com.harrrshith.moowe.width

@Composable
fun NavigationGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    val startDestination = Destination.OnBoard
    val navigate = remember { NavigationHelper(navController) }
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination
    ){
        composable<Destination.OnBoard> {
            OnBoardingRoute(
                navigateToHome = { navigate.navigateToHome() }
            )
        }

        composable<Destination.Home.Discover> {
            ImageCarousel(
                screenWidth = width,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}
