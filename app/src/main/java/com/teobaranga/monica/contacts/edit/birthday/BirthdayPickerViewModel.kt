package com.teobaranga.monica.contacts.edit.birthday

import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teobaranga.monica.contacts.ui.Birthday
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = BirthdayPickerViewModel.Factory::class)
class BirthdayPickerViewModel @AssistedInject constructor(
    @Assisted
    val initialBirthday: Birthday?,
) : ViewModel() {

    val uiState = BirthdayPickerUiState(initialBirthday)

    init {
        viewModelScope.launch {
            snapshotFlow { uiState.age.birthday }
                .drop(1)
                .collectLatest {
                    uiState.setAgeBased()
                }
        }
        viewModelScope.launch {
            snapshotFlow { uiState.unknownYear.birthday }
                .drop(1)
                .collectLatest {
                    uiState.setUnknownYear()
                }
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(initialBirthday: Birthday?): BirthdayPickerViewModel
    }
}
