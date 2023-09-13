package com.teobaranga.monica.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import java.nio.ByteBuffer

sealed interface UserAvatar {
    data class Initials(
        val initials: String,
        val color: Color,
    ) : UserAvatar

    data class Photo(
        val data: ByteBuffer,
    ) : UserAvatar
}

@Composable
fun UserAvatar(
    modifier: Modifier = Modifier,
    userAvatar: UserAvatar,
) {
    when (userAvatar) {
        is UserAvatar.Initials -> {
            Box(
                modifier = modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(userAvatar.color),
                contentAlignment = Alignment.Center,
            ) {
                Text(text = userAvatar.initials)
            }
        }

        is UserAvatar.Photo -> {
            AsyncImage(
                modifier = modifier
                    .size(32.dp)
                    .clip(CircleShape),
                model = userAvatar.data,
                contentDescription = null,
            )
        }
    }
}
