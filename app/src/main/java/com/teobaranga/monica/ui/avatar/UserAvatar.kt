package com.teobaranga.monica.ui.avatar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import coil.request.ImageRequest

data class UserAvatar(
    val contactId: Int,
    val initials: String,
    val color: String,
    val avatarUrl: String?,
)

@Composable
fun UserAvatar(userAvatar: UserAvatar, onClick: () -> Unit, modifier: Modifier = Modifier) {
    SubcomposeAsyncImage(
        modifier = modifier
            .size(32.dp)
            .clip(CircleShape)
            .background(Color(userAvatar.color.toColorInt()))
            .clickable(onClick = onClick),
        model = ImageRequest.Builder(LocalContext.current)
            .data(userAvatar)
            .memoryCacheKey(userAvatar.avatarUrl)
            .diskCacheKey(userAvatar.avatarUrl)
            // Refresh the image when the avatar changes
            .setParameter("avatarUrl", userAvatar.avatarUrl)
            .crossfade(true)
            .build(),
        contentDescription = null,
    ) {
        val state = painter.state
        if (state is AsyncImagePainter.State.Success) {
            SubcomposeAsyncImageContent()
        } else {
            Box(
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = userAvatar.initials,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.White.copy(alpha = 0.85f),
                )
            }
        }
    }
}
