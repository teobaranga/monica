package com.teobaranga.monica.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.scene.Scene
import androidx.navigation3.ui.NavDisplay

private const val TRANSITION_DURATION_MILLIS = 220

@Composable
internal actual fun MonicaNavDisplay(
    entries: List<NavEntry<NavKey>>,
    topLevelContentKeys: Set<Any>,
    onBack: () -> Unit,
    modifier: Modifier,
) {
    NavDisplay(
        entries = entries,
        onBack = onBack,
        modifier = modifier,
        transitionSpec = {
            if (isTabSwitch(topLevelContentKeys)) {
                tabSwitchTransform()
            } else {
                pushTransform()
            }
        },
        popTransitionSpec = {
            if (isTabSwitch(topLevelContentKeys)) {
                tabSwitchTransform()
            } else {
                popTransform()
            }
        },
        predictivePopTransitionSpec = {
            if (isTabSwitch(topLevelContentKeys)) {
                tabSwitchTransform()
            } else {
                popTransform()
            }
        },
    )
}

private fun AnimatedContentTransitionScope<Scene<NavKey>>.isTabSwitch(
    topLevelContentKeys: Set<Any>,
): Boolean {
    return initialState.key in topLevelContentKeys && targetState.key in topLevelContentKeys
}

private fun tabSwitchTransform(): ContentTransform {
    return fadeIn(
        animationSpec = tween(TRANSITION_DURATION_MILLIS, easing = FastOutSlowInEasing),
    ) togetherWith fadeOut(
        animationSpec = tween(TRANSITION_DURATION_MILLIS, easing = FastOutSlowInEasing),
    )
}

private fun pushTransform(): ContentTransform {
    return slideInHorizontally(
        animationSpec = tween(TRANSITION_DURATION_MILLIS, easing = FastOutSlowInEasing),
        initialOffsetX = { width -> width },
    ) togetherWith ExitTransition.None
}

private fun popTransform(): ContentTransform {
    return EnterTransition.None togetherWith slideOutHorizontally(
        animationSpec = tween(TRANSITION_DURATION_MILLIS, easing = FastOutSlowInEasing),
        targetOffsetX = { width -> width },
    )
}
