package com.teobaranga.monica.contacts.edit.birthday

import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teobaranga.monica.contacts.ui.Birthday
import com.teobaranga.monica.inject.runtime.ContributesViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.AppScope

@Inject
@ContributesViewModel(AppScope::class)
class BirthdayPickerViewModel internal constructor(
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
}
