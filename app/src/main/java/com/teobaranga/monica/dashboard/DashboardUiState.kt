package com.teobaranga.monica.dashboard

data class DashboardUiState(
    val userInfo: UserInfo,
) {
    data class UserInfo(
        val name: String,
        val avatarColor: String,
        val initials: String,
    )
}
