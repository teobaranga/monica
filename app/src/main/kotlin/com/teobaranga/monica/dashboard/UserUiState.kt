package com.teobaranga.monica.dashboard

data class UserUiState(
    val userInfo: UserInfo,
) {
    data class UserInfo(
        val name: String,
    )
}
