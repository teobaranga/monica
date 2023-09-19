package com.teobaranga.monica.dashboard

import com.teobaranga.monica.ui.avatar.UserAvatar

data class UserUiState(
    val userInfo: UserInfo,
    val avatar: UserAvatar,
) {
    data class UserInfo(
        val name: String,
    )
}
