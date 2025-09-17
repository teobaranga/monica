package com.teobaranga.monica.useravatar

import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun UserAvatarIconButton(
    userAvatar: UserAvatar,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    IconButton(
        modifier = modifier,
        onClick = onClick,
    ) {
        UserAvatar(
            userAvatar = userAvatar,
        )
    }
}
