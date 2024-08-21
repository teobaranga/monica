package com.teobaranga.monica.contacts.edit.ui

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.teobaranga.monica.contacts.ui.Birthday

sealed interface ContactEditUiState {
    data object Loading : ContactEditUiState

    @Stable
    class Loaded(
        val id: Int,
        firstName: String,
        lastName: String?,
        nickname: String?,
        initialBirthday: Birthday?,
    ) : ContactEditUiState {

        val firstName = TextFieldState(firstName)

        val lastName = TextFieldState(lastName.orEmpty())

        val nickname = TextFieldState(nickname.orEmpty())

        var birthday by mutableStateOf(initialBirthday)

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false
            other as Loaded
            return id == other.id &&
                firstName.text == other.firstName.text &&
                lastName.text == other.lastName.text &&
                nickname.text == other.nickname.text &&
                birthday == other.birthday
        }

        override fun hashCode(): Int {
            var result = id
            result = 31 * result + firstName.hashCode()
            result = 31 * result + lastName.hashCode()
            result = 31 * result + nickname.hashCode()
            result = 31 * result + (birthday?.hashCode() ?: 0)
            return result
        }
    }
}
