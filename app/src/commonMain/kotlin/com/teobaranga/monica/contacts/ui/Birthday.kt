package com.teobaranga.monica.contacts.ui

import androidx.compose.runtime.Composable
import com.teobaranga.monica.core.datetime.LocalSystemClock
import com.teobaranga.monica.core.datetime.MonthDay
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import kotlinx.datetime.yearsUntil

sealed interface Birthday {

    data class AgeBased(val age: Int) : Birthday

    data class UnknownYear(val monthDay: MonthDay) : Birthday

    data class Full(val date: LocalDate) : Birthday {

        val age: Int
            @Composable
            get() = date.yearsUntil(LocalSystemClock.current.todayIn(TimeZone.currentSystemDefault()))
    }
}
