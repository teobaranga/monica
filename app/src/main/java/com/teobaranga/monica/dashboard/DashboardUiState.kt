package com.teobaranga.monica.dashboard

import java.nio.ByteBuffer

data class DashboardUiState(
    val userInfo: UserInfo,
) {
    data class UserInfo(
        val name: String,
        val initials: String,
        val avatarColor: String,
        val avatarData: ByteBuffer?,
    )
}
