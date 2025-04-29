package pl.kwasow.extensions

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDeepLink
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

inline fun <reified T : Any> NavGraphBuilder.slideComposable(
    deepLinks: List<NavDeepLink> = emptyList(),
    noinline content: @Composable AnimatedContentScope.(NavBackStackEntry) -> Unit,
) {
    val animationTime = 400

    composable<T>(
        content = content,
        enterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(animationTime),
            )
        },
        exitTransition = {
            scaleOut(
                targetScale = 0.75f,
                animationSpec = tween(animationTime),
            ) +
                fadeOut(
                    targetAlpha = 0.4f,
                    animationSpec = tween(animationTime),
                )
        },
        popEnterTransition = {
            scaleIn(
                initialScale = 0.75f,
                animationSpec = tween(animationTime),
            ) +
                fadeIn(
                    initialAlpha = 0.4f,
                    animationSpec = tween(animationTime),
                )
        },
        popExitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(animationTime),
            )
        },
        deepLinks = deepLinks,
    )
}
