package com.teobaranga.monica.core.datetime

import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class Year private constructor(
    val value: Int,
) {

    companion object {

        /**
         * Returns the current year.
         */
        fun now(): Year {
            val localDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
            return Year(localDateTime.year)
        }
    }
}
