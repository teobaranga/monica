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
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.spec.DestinationStyle
import com.teobaranga.monica.journal.view.ui.JournalEntryScreen
import com.teobaranga.monica.journal.view.ui.JournalEntryTopAppBar

@Destination<JournalNavGraph>(
    style = JournalEntryTransitions::class,
)
@Composable
internal fun JournalEntry(
    navigator: DestinationsNavigator,
    entryId: Int? = null,
    viewModel: JournalEntryViewModel = hiltViewModel<JournalEntryViewModel, JournalEntryViewModel.Factory>(
        creationCallback = { factory ->
            factory.create(entryId)
        },
    ),
) {
    val entry by viewModel.entry.collectAsStateWithLifecycle()
    JournalEntryScreen(
        entry = entry,
        topBar = {
            JournalEntryTopAppBar(
                onBack = navigator::popBackStack,
                onSave = {
                    viewModel.onSave()
                    navigator.popBackStack()
                },
                onDelete = {
                    // TODO
                },
            )
        },
    )
}

object JournalEntryTransitions : DestinationStyle.Animated() {

    override val enterTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition?) = {
        fadeIn(animationSpec = tween(300))
    }

    override val exitTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition?) = {
        fadeOut(animationSpec = tween(300))
    }
}
