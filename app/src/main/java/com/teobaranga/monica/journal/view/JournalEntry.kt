package com.teobaranga.monica.journal.view

import JournalNavGraph
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.spec.DestinationStyle
import com.teobaranga.monica.journal.view.ui.JournalEntryScreen

@JournalNavGraph
@Destination(
    style = JournalEntryTransitions::class,
)
@Composable
fun JournalEntry(
    entryId: Int? = null,
) {
    val viewModel = hiltViewModel<JournalEntryViewModel, JournalEntryViewModel.Factory>(
        creationCallback = { factory ->
            factory.create(entryId)
        },
    )
    val entry by viewModel.entry.collectAsStateWithLifecycle()
    JournalEntryScreen(
        entry = entry,
    )
}

object JournalEntryTransitions : DestinationStyle.Animated {

    override fun AnimatedContentTransitionScope<NavBackStackEntry>.enterTransition(): EnterTransition {
        return fadeIn(animationSpec = tween(300))
    }

    override fun AnimatedContentTransitionScope<NavBackStackEntry>.exitTransition(): ExitTransition {
        return fadeOut(animationSpec = tween(300))
    }
}
