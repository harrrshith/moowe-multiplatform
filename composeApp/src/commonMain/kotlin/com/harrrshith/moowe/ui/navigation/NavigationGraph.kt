package com.harrrshith.moowe.ui.navigation

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.LocalNavAnimatedContentScope
import androidx.navigation3.ui.NavDisplay
import com.harrrshith.moowe.domain.model.MediaType
import com.harrrshith.moowe.ui.detail.DetailRoute
import com.harrrshith.moowe.ui.discover.DiscoverRoute
import com.harrrshith.moowe.ui.search.SearchRoute
import com.harrrshith.moowe.ui.trending.TrendingRoute
import com.harrrshith.moowe.ui.yours.YoursRoute

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun NavigationGraph(
    modifier: Modifier = Modifier,
    backStack: SnapshotStateList<Destination>,
) {
    if (backStack.isEmpty()) {
        backStack.add(Destination.Home.Discover)
    }

    SharedTransitionLayout {
        NavDisplay(
            backStack = backStack,
            modifier = modifier,
            onBack = {
                if (backStack.size > 1) {
                    backStack.removeLast()
                }
            },
            entryProvider = entryProvider<Destination> {
                entry<Destination.Home.Discover> {
                    DiscoverRoute(
                        animatedContentScope = LocalNavAnimatedContentScope.current,
                        sharedTransitionScope = this@SharedTransitionLayout,
                        navigateToDetail = { id, mediaType, sharedKey, title, posterPath ->
                            backStack.add(
                                Destination.Detail(
                                    id = id,
                                    mediaType = mediaType.apiValue,
                                    sharedKey = sharedKey,
                                    title = title,
                                    posterPath = posterPath,
                                )
                            )
                        },
                    )
                }

                entry<Destination.Home.Trending> {
                    TrendingRoute(
                        animatedContentScope = LocalNavAnimatedContentScope.current,
                        sharedTransitionScope = this@SharedTransitionLayout,
                        navigateToDetail = { id, mediaType, sharedKey, title, posterPath ->
                            backStack.add(
                                Destination.Detail(
                                    id = id,
                                    mediaType = mediaType.apiValue,
                                    sharedKey = sharedKey,
                                    title = title,
                                    posterPath = posterPath,
                                )
                            )
                        },
                    )
                }

                entry<Destination.Home.Search> {
                    SearchRoute(
                        navigateToDetail = { id, mediaType, sharedKey, title, posterPath ->
                            backStack.add(
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

                entry<Destination.Home.Yours> {
                    YoursRoute(
                        navigateToDetail = { id, mediaType, sharedKey, title, posterPath ->
                            backStack.add(
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

                entry<Destination.Detail>(
                    clazzContentKey = { detail ->
                        "detail-${detail.id}-${detail.mediaType}-${detail.sharedKey}"
                    }
                ) { detailDestination ->
                    DetailRoute(
                        sharedKey = detailDestination.sharedKey,
                        initialTitle = detailDestination.title,
                        initialPosterPath = detailDestination.posterPath,
                        mediaType = MediaType.fromApiValue(detailDestination.mediaType),
                        movieId = detailDestination.id,
                        animatedContentScope = LocalNavAnimatedContentScope.current,
                        sharedTransitionScope = this@SharedTransitionLayout,
                        onBackPressed = {
                            if (backStack.size > 1) {
                                backStack.removeLast()
                            }
                        },
                    )
                }
            },
        )
    }
}
