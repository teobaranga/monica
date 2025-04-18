package com.teobaranga.monica.core.datetime

import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn

class Year private constructor(
    val value: Int,
) {

    companion object {

        /**
         * Returns the current year.
         */
        fun now(): Year {
            val localDate = Clock.System.todayIn(TimeZone.currentSystemDefault())
            return Year(localDate.year)
        }
    }
}
