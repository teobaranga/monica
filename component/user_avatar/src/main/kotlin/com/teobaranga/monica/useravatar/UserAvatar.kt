package com.teobaranga.monica.useravatar

import android.content.res.Configuration
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import coil.request.ImageRequest
import com.materialkolor.ktx.darken
import com.teobaranga.monica.core.ui.fromHex
import com.teobaranga.monica.core.ui.isLight
import com.teobaranga.monica.core.ui.theme.MonicaTheme

data class UserAvatar(
    val contactId: Int,
    val initials: String,
    val color: String,
    val avatarUrl: String?,
) {

    companion object {

        fun default(name: String) = UserAvatar(
            contactId = -1,
            initials = name.take(2).uppercase(),
            color = "#709512",
            avatarUrl = null,
        )
    }
}

@Composable
fun UserAvatar(userAvatar: UserAvatar, modifier: Modifier = Modifier, onClick: (() -> Unit)? = null) {
    SubcomposeAsyncImage(
        modifier = modifier
            .size(32.dp)
            .clip(CircleShape)
            .background(
                Color.fromHex(userAvatar.color).run {
                    // Some colours don't have enough contrast in dark mode, darken them
                    val isLight = MaterialTheme.colorScheme.isLight()
                    if (isLight) this else darken(2.5f)
                },
            )
            .then(
                if (onClick != null) {
                    Modifier.clickable(onClick = onClick)
                } else {
                    Modifier
                },
            ),
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

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PreviewUserAvatar() {
    MonicaTheme {
        UserAvatar(
            userAvatar = UserAvatar(
                contactId = 1,
                initials = "TB",
                color = "#709512",
                avatarUrl = null,
            ),
        )
    }
}
