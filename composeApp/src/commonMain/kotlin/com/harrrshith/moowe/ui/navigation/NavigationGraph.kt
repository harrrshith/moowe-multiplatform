package com.harrrshith.moowe.ui.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.harrrshith.moowe.domain.model.MediaType
import com.harrrshith.moowe.ui.detail.DetailRoute
import com.harrrshith.moowe.ui.discover.DiscoverRoute
import com.harrrshith.moowe.ui.search.SearchRoute
import com.harrrshith.moowe.ui.trending.TrendingRoute
import com.harrrshith.moowe.ui.yours.YoursRoute

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
                    enterTransition = { NavigationTransitions.topLevelEnter(this) },
                    exitTransition = { NavigationTransitions.topLevelExit(this) },
                    popEnterTransition = { NavigationTransitions.topLevelEnter(this) },
                    popExitTransition = { NavigationTransitions.topLevelExit(this) }
                ) {
                    DiscoverRoute(
                        animatedContentScope = this@composable,
                        sharedTransitionScope = this@SharedTransitionLayout,
                        navigateToDetail = { id, mediaType, sharedKey, title, posterPath ->
                            navController.navigate(
                                Destination.Detail(
                                    id = id,
                                    mediaType = mediaType.apiValue,
                                    sharedKey = sharedKey,
                                    title = title,
                                    posterPath = posterPath,
                                )
                            )
                        }
                    )
                }
                composable<Destination.Home.Trending>(
                    enterTransition = { NavigationTransitions.topLevelEnter(this) },
                    exitTransition = { NavigationTransitions.topLevelExit(this) },
                    popEnterTransition = { NavigationTransitions.topLevelEnter(this) },
                    popExitTransition = { NavigationTransitions.topLevelExit(this) }
                ) {
                    TrendingRoute(
                        animatedContentScope = this@composable,
                        sharedTransitionScope = this@SharedTransitionLayout,
                        navigateToDetail = { id, mediaType, sharedKey, title, posterPath ->
                            navController.navigate(
                                Destination.Detail(
                                    id = id,
                                    mediaType = mediaType.apiValue,
                                    sharedKey = sharedKey,
                                    title = title,
                                    posterPath = posterPath,
                                )
                            )
                        }
                    )
                }
                composable<Destination.Home.Search>(
                    enterTransition = { NavigationTransitions.topLevelEnter(this) },
                    exitTransition = { NavigationTransitions.topLevelExit(this) },
                    popEnterTransition = { NavigationTransitions.topLevelEnter(this) },
                    popExitTransition = { NavigationTransitions.topLevelExit(this) }
                ) {
                    SearchRoute(
                        modifier = Modifier.fillMaxSize(),
                        navigateToDetail = { id, mediaType, sharedKey, title, posterPath ->
                            navController.navigate(
                                Destination.Detail(
                                    id = id,
                                    mediaType = mediaType.apiValue,
                                    sharedKey = sharedKey,
                                    title = title,
                                    posterPath = posterPath,
                                )
                            )
                        }
                    )
                }
                composable<Destination.Home.Yours>(
                    enterTransition = { NavigationTransitions.topLevelEnter(this) },
                    exitTransition = { NavigationTransitions.topLevelExit(this) },
                    popEnterTransition = { NavigationTransitions.topLevelEnter(this) },
                    popExitTransition = { NavigationTransitions.topLevelExit(this) }
                ) {
                    YoursRoute(
                        modifier = Modifier.fillMaxSize(),
                        navigateToDetail = { id, mediaType, sharedKey, title, posterPath ->
                            navController.navigate(
                                Destination.Detail(
                                    id = id,
                                    mediaType = mediaType.apiValue,
                                    sharedKey = sharedKey,
                                    title = title,
                                    posterPath = posterPath,
                                )
                            )
                        }
                    )
                }
            }

            composable<Destination.Detail>(
                enterTransition = { EnterTransition.None },
                exitTransition = { ExitTransition.None },
                popEnterTransition = { EnterTransition.None },
                popExitTransition = { fadeOut(animationSpec = tween(120)) }
            ) {
                val detailDestination = it.toRoute<Destination.Detail>()
                DetailRoute(
                    sharedKey = detailDestination.sharedKey,
                    initialTitle = detailDestination.title,
                    initialPosterPath = detailDestination.posterPath,
                    mediaType = MediaType.fromApiValue(detailDestination.mediaType),
                    movieId = detailDestination.id,
                    animatedContentScope = this@composable,
                    sharedTransitionScope = this@SharedTransitionLayout,
                    onBackPressed = { navController.popBackStack() }
                )
            }
        }
    }
}
