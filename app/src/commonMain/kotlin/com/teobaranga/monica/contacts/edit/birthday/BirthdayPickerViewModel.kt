package com.teobaranga.monica.contacts.edit.birthday

import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teobaranga.kotlin.inject.viewmodel.runtime.ContributesViewModel
import com.teobaranga.monica.contacts.ui.Birthday
import com.teobaranga.monica.core.datetime.MonthDay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.AssistedFactory
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.AppScope

@Inject
@ContributesViewModel(scope = AppScope::class, assistedFactory = BirthdayPickerViewModel.Factory::class)
class BirthdayPickerViewModel(
    @Assisted
    private val initialBirthday: Birthday?,
    getNowLocalDate: () -> LocalDate,
    getNowMonthDay: () -> MonthDay,
) : ViewModel() {

    val uiState = BirthdayPickerUiState(
        initialBirthday = initialBirthday,
        nowLocalDate = getNowLocalDate(),
        nowMonthDay = getNowMonthDay(),
    )

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
        operator fun invoke(initialBirthday: Birthday?): BirthdayPickerViewModel
    }
}
