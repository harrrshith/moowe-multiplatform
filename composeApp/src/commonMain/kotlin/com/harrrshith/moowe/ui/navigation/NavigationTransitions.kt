package com.harrrshith.moowe.ui.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut

/**
 * Smooth navigation transitions that complement shared element animations.
 * Uses subtle fade and scale effects that don't conflict with the shared element morphing.
 */
object NavigationTransitions {
    private const val DURATION = 300
    
    // Detail screen enters with fade + slight scale
    val enterTransition: EnterTransition = fadeIn(
        animationSpec = tween(DURATION)
    ) + scaleIn(
        initialScale = 0.97f,
        animationSpec = tween(DURATION)
    )
    
    // Discover screen exits with fade (stays in place)
    val exitTransition: ExitTransition = fadeOut(
        animationSpec = tween(DURATION)
    )
    
    // Discover screen re-enters with fade
    val popEnterTransition: EnterTransition = fadeIn(
        animationSpec = tween(DURATION)
    )
    
    // Detail screen exits with fade + slight scale
    val popExitTransition: ExitTransition = fadeOut(
        animationSpec = tween(DURATION)
    ) + scaleOut(
        targetScale = 0.97f,
        animationSpec = tween(DURATION)
    )
}
