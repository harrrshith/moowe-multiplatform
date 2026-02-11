package com.harrrshith.moowe.ui.navigation

import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.harrrshith.moowe.ui.detail.DetailRoute
import com.harrrshith.moowe.ui.discover.DiscoverRoute

@Composable
fun NavigationGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    val startDestination = Destination.Home
    
    SharedTransitionLayout {
        NavHost(
            modifier = modifier,
            navController = navController,
            startDestination = startDestination
        ){
            navigation<Destination.Home>(
                startDestination = Destination.Home.Discover
            ){
                composable<Destination.Home.Discover>(
                    enterTransition = { NavigationTransitions.enterTransition },
                    exitTransition = { NavigationTransitions.exitTransition },
                    popEnterTransition = { NavigationTransitions.popEnterTransition },
                    popExitTransition = { NavigationTransitions.popExitTransition }
                ) {
                    DiscoverRoute(
                        animatedContentScope = this@composable,
                        sharedTransitionScope = this@SharedTransitionLayout,
                        navigateToDetail = { id ->
                            navController.navigate(Destination.Detail(id))
                        }
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

            composable<Destination.Detail>(
                enterTransition = { NavigationTransitions.enterTransition },
                exitTransition = { NavigationTransitions.exitTransition },
                popEnterTransition = { NavigationTransitions.popEnterTransition },
                popExitTransition = { NavigationTransitions.popExitTransition }
            ) {
                DetailRoute(
                    animatedContentScope = this@composable,
                    sharedTransitionScope = this@SharedTransitionLayout,
                    onBackPressed = { navController.popBackStack() }
                )
            }
        }
    }
}