package com.harrrshith.moowe.ui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.navigation.NavBackStackEntry

/**
 * Navigation transitions tuned for smooth, polished feel on iOS and Android.
 */
object NavigationTransitions {
    private const val TOP_LEVEL_DURATION = 320

    fun topLevelEnter(scope: AnimatedContentTransitionScope<NavBackStackEntry>): EnterTransition {
        if (!scope.isTopLevelToTopLevel()) return EnterTransition.None
        val movingForward = scope.isMovingForwardBetweenTopLevelDestinations()
        return fadeIn(
            animationSpec = tween(
                durationMillis = TOP_LEVEL_DURATION,
                delayMillis = 40,
                easing = FastOutSlowInEasing,
            )
        ) + slideInHorizontally(
            animationSpec = tween(durationMillis = TOP_LEVEL_DURATION, easing = FastOutSlowInEasing),
            initialOffsetX = { fullWidth -> if (movingForward) fullWidth / 12 else -fullWidth / 12 },
        )
    }

    fun topLevelExit(scope: AnimatedContentTransitionScope<NavBackStackEntry>): ExitTransition {
        if (!scope.isTopLevelToTopLevel()) return ExitTransition.None
        val movingForward = scope.isMovingForwardBetweenTopLevelDestinations()
        return fadeOut(
            animationSpec = tween(durationMillis = 180, easing = FastOutSlowInEasing)
        ) + slideOutHorizontally(
            animationSpec = tween(durationMillis = 240, easing = FastOutSlowInEasing),
            targetOffsetX = { fullWidth -> if (movingForward) -fullWidth / 16 else fullWidth / 16 },
        )
    }

    private fun AnimatedContentTransitionScope<NavBackStackEntry>.isTopLevelToTopLevel(): Boolean {
        val from = topLevelRouteIndex(initialState.destination.route)
        val to = topLevelRouteIndex(targetState.destination.route)
        return from != -1 && to != -1
    }

    private fun AnimatedContentTransitionScope<NavBackStackEntry>.isMovingForwardBetweenTopLevelDestinations(): Boolean {
        val from = topLevelRouteIndex(initialState.destination.route)
        val to = topLevelRouteIndex(targetState.destination.route)
        if (from == -1 || to == -1) return true
        return to >= from
    }

    private fun topLevelRouteIndex(route: String?): Int {
        if (route == null) return -1
        return when {
            route.contains("Destination.Home.Discover") -> 0
            route.contains("Destination.Home.Trending") -> 1
            route.contains("Destination.Home.Search") -> 2
            route.contains("Destination.Home.Yours") -> 3
            else -> -1
        }
    }
}
