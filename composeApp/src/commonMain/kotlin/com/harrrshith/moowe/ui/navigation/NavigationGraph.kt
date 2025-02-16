package com.harrrshith.moowe.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
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
        navigation<Destination.Home>(
            startDestination = Destination.Home.Discover
        ){
            composable<Destination.Home.Discover> {
                ImageCarousel(
                    screenWidth = width,
                    modifier = Modifier.fillMaxSize()
                )
            }
            composable<Destination.Home.Trending> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ){
                    Text("Trending")
                }
            }
            composable<Destination.Home.Search> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ){
                    Text("Search")
                }
            }
            composable<Destination.Home.Yours> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ){
                    Text("Yours")
                }
            }
        }
    }
}
