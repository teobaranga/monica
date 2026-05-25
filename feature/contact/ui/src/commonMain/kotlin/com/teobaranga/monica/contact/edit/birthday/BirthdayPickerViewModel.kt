package com.teobaranga.monica.contact.edit.birthday

import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teobaranga.monica.contact.Birthday
import com.teobaranga.monica.core.datetime.MonthDay
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Assisted
import dev.zacsweers.metro.AssistedFactory
import dev.zacsweers.metro.AssistedInject
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metrox.viewmodel.ManualViewModelAssistedFactory
import dev.zacsweers.metrox.viewmodel.ManualViewModelAssistedFactoryKey
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate

@AssistedInject
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
    @ManualViewModelAssistedFactoryKey
    @ContributesIntoMap(AppScope::class)
    interface Factory: ManualViewModelAssistedFactory {
        fun create(initialBirthday: Birthday?): BirthdayPickerViewModel
    }
}
