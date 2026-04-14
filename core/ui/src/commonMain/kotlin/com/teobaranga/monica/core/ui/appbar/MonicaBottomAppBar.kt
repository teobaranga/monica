package com.teobaranga.monica.core.ui.appbar

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun MonicaBottomAppBar(
    actions: @Composable RowScope.() -> Unit,
    floatingActionButton: @Composable (() -> Unit)? = null,
    colors: BottomAppBarColors = BottomAppBarDefaults.bottomAppBarColors(),
    contentScrollState: ScrollState = rememberScrollState(),
    modifier: Modifier = Modifier,
) {
    val targetColor by remember(colors, contentScrollState) {
        derivedStateOf {
            val colorTransitionFraction = if (contentScrollState.canScrollForward) 1f else 0f
            colors.containerColor(colorTransitionFraction)
        }
    }
    val bottomBarContainerColor by animateColorAsState(
        targetValue = targetColor,
        animationSpec = MaterialTheme.motionScheme.defaultEffectsSpec(),
    )
    BottomAppBar(
        modifier = modifier,
        containerColor = bottomBarContainerColor,
        actions = actions,
        floatingActionButton = floatingActionButton,
    )
}

@Stable
class BottomAppBarColors(
    val containerColor: Color,
    val scrolledContainerColor: Color,
) {

    @Stable
    fun containerColor(colorTransitionFraction: Float): Color {
        return lerp(
            scrolledContainerColor,
            containerColor,
            FastOutLinearInEasing.transform(colorTransitionFraction),
        )
    }
}

@Composable
fun BottomAppBarDefaults.bottomAppBarColors(
    containerColor: Color = BottomAppBarDefaults.containerColor,
    scrolledContainerColor: Color = MaterialTheme.colorScheme.background,
): BottomAppBarColors {

    return BottomAppBarColors(
        containerColor = containerColor,
        scrolledContainerColor = scrolledContainerColor,
    )
}
