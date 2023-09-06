package com.teobaranga.monica.setup

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.SavedStateHandleSaveableApi
import androidx.lifecycle.viewmodel.compose.saveable
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@OptIn(SavedStateHandleSaveableApi::class)
@HiltViewModel
class SetupViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    val uiState by savedStateHandle.saveable(saver = UiState.Saver) {
        UiState()
    }

    fun onSignIn() {

    }
}
