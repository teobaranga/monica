package com.teobaranga.monica.journal.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.teobaranga.monica.core.ui.navigation.LocalNavigator
import kotlinx.coroutines.flow.SharedFlow

sealed interface JournalEntryEffect {
    data object Back : JournalEntryEffect
}

@Composable
fun JournalEntryEffectHandler(
    effects: SharedFlow<JournalEntryEffect>,
) {
    val navigator = LocalNavigator.current

    LaunchedEffect(Unit) {
        effects.collect { effect ->
            when (effect) {
                is JournalEntryEffect.Back -> navigator.popBackStack()
            }
        }
    }
}
