package com.teobaranga.monica.contacts.edit.ui

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.teobaranga.monica.contacts.ui.Birthday
import com.teobaranga.monica.genders.domain.Gender

sealed interface ContactEditUiState {

    data object Loading : ContactEditUiState

    @Stable
    class Loaded(
        val id: Int,
        firstName: String,
        lastName: String?,
        nickname: String?,
        initialGender: Gender?,
        val genders: List<Gender>,
        initialBirthday: Birthday?,
    ) : ContactEditUiState {

        val firstName = TextFieldState(firstName)

        val lastName = TextFieldState(lastName.orEmpty())

        val nickname = TextFieldState(nickname.orEmpty())

        var gender by mutableStateOf(initialGender)

        var birthday by mutableStateOf(initialBirthday)

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other == null || this::class != other::class) return false

            other as Loaded

            if (id != other.id) return false
            if (genders != other.genders) return false
            if (firstName.text != other.firstName.text) return false
            if (lastName.text != other.lastName.text) return false
            if (nickname.text != other.nickname.text) return false
            if (gender != other.gender) return false
            if (birthday != other.birthday) return false

            return true
        }

        override fun hashCode(): Int {
            var result = id
            result = 31 * result + firstName.hashCode()
            result = 31 * result + lastName.hashCode()
            result = 31 * result + nickname.hashCode()
            result = 31 * result + (birthday?.hashCode() ?: 0)
            result = 31 * result + (gender?.hashCode() ?: 0)
            return result
        }

        override fun toString(): String {
            return "Loaded(" +
                "id=$id, " +
                "firstName=$firstName, " +
                "lastName=$lastName, " +
                "nickname=$nickname, " +
                "gender=$gender, " +
                "birthday=$birthday, " +
                "genders=$genders)"
        }
    }
}
