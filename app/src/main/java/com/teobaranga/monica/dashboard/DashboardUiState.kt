package com.teobaranga.monica.dashboard

import com.teobaranga.monica.ui.UserAvatar

data class DashboardUiState(
    val userInfo: UserInfo,
    val avatar: UserAvatar,
) {
    data class UserInfo(
        val name: String,
    )
}
