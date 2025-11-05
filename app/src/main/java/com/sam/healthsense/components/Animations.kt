
package com.sam.healthsense.components

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.navigation.NavBackStackEntry
import com.sam.healthsense.Utils.Constants

/**
 *  Custom enter transition that slides in from the right.
 *  This function is intended to be used as an enterTransition.
 */


fun AnimatedContentTransitionScope<NavBackStackEntry>.slideIntoContainerAnimation(
    towards: AnimatedContentTransitionScope.SlideDirection = AnimatedContentTransitionScope.SlideDirection.Start // Default to sliding in from the Left
): EnterTransition {
    return slideInHorizontally(
        animationSpec = tween(
            durationMillis = Constants.NAVIGATION_ANIMATION_DURATION,
            easing = EaseIn
        ),

        initialOffsetX = { fullWidth ->
            when (towards) {
                AnimatedContentTransitionScope.SlideDirection.Start -> -fullWidth // Slide in from Left
                AnimatedContentTransitionScope.SlideDirection.End -> fullWidth    // Slide in from Right
                else -> fullWidth // Default: Slide in from Right (matches your original explicit version)
                // Or choose a different default like Start if that makes more sense
            }
        }
    )
}

/**
 *  Custom exit transition that slides out horizontally.
 *  The direction of the slide is configurable.
 *  This function is intended to be used as an exitTransition.
 */
fun AnimatedContentTransitionScope<NavBackStackEntry>.slideOutOfContainerAnimation(
    towards: AnimatedContentTransitionScope.SlideDirection = AnimatedContentTransitionScope.SlideDirection.End // Default to sliding out towards the Right
): ExitTransition {
    return slideOutHorizontally(
        animationSpec = tween(
            durationMillis = Constants.NAVIGATION_ANIMATION_DURATION,
            easing = EaseOut
        ),
        targetOffsetX = { fullWidth ->
            when (towards) {
                AnimatedContentTransitionScope.SlideDirection.Start -> -fullWidth // Slide out to Left
                AnimatedContentTransitionScope.SlideDirection.End -> fullWidth    // Slide out to Right
                else -> -fullWidth

            }
        }
    )
}



/**
 *  Custom enter transition with configurable slide direction.
 */
fun AnimatedContentTransitionScope<NavBackStackEntry>.slideInWithDirection(
    slideDirection: AnimatedContentTransitionScope.SlideDirection = AnimatedContentTransitionScope.SlideDirection.End
): EnterTransition {
    return slideInHorizontally(
        animationSpec = tween(
            durationMillis = Constants.NAVIGATION_ANIMATION_DURATION,
            easing = EaseIn
        ),
        initialOffsetX = { fullWidth ->
            when (slideDirection) {
                AnimatedContentTransitionScope.SlideDirection.Start -> -fullWidth // From Left
                AnimatedContentTransitionScope.SlideDirection.End -> fullWidth    // From Right
                else -> fullWidth // Default to from Right
            }
        }
    )
}

/**
 *  Custom exit transition with configurable slide direction.
 */
fun AnimatedContentTransitionScope<NavBackStackEntry>.slideOutWithDirection(
    slideDirection: AnimatedContentTransitionScope.SlideDirection = AnimatedContentTransitionScope.SlideDirection.Start
): ExitTransition {
    return slideOutHorizontally(
        animationSpec = tween(
            durationMillis = Constants.NAVIGATION_ANIMATION_DURATION,
            easing = EaseOut
        ),
        targetOffsetX = { fullWidth ->
            when (slideDirection) {
                AnimatedContentTransitionScope.SlideDirection.Start -> -fullWidth // To Left
                AnimatedContentTransitionScope.SlideDirection.End -> fullWidth    // To Right
                else -> -fullWidth // Default to towards Left
            }
        }
    )
}

