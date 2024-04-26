package com.teobaranga.monica.ui

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.teobaranga.monica.MonicaBackground
import com.teobaranga.monica.ui.avatar.UserAvatar
import com.teobaranga.monica.ui.theme.MonicaTheme
import com.teobaranga.monica.util.compose.keyboardAsState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MonicaSearchBar(userAvatar: @Composable () -> Unit, modifier: Modifier = Modifier) {
    var query by rememberSaveable { mutableStateOf("") }
    var shouldBeActive by rememberSaveable { mutableStateOf(false) }
    val isImeVisible by keyboardAsState()
    Box(
        modifier = Modifier
            .zIndex(1f)
            .fillMaxSize()
            //noinspection UnnecessaryComposedModifier because state is read and must update the modifier
            .composed {
                if (shouldBeActive) {
                    pointerInput(Unit) {
                        detectTapGestures {
                            shouldBeActive = false
                        }
                    }
                } else {
                    Modifier
                }
            },
    ) {
        DockedSearchBar(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            query = query,
            onQueryChange = {
                query = it
            },
            onSearch = {
                shouldBeActive = false
            },
            active = shouldBeActive && (query.isNotBlank() || isImeVisible),
            onActiveChange = {
                shouldBeActive = it
            },
            leadingIcon = {
                Icon(
                    painter = rememberVectorPainter(image = Icons.Default.Search),
                    contentDescription = null,
                )
            },
            trailingIcon = {
                userAvatar()
            },
            placeholder = {
                Text(text = "Search something")
            },
        ) {
            // TODO: display results
        }
    }
}

@Preview
@Composable
private fun PreviewDashboardSearchBar() {
    MonicaTheme {
        MonicaBackground {
            Box {
                MonicaSearchBar(
                    userAvatar = {
                        UserAvatar(
                            userAvatar = UserAvatar(
                                contactId = 0,
                                initials = "TB",
                                color = "#709512",
                                avatarUrl = null,
                            ),
                            onClick = { },
                        )
                    },
                )
            }
        }
    }
}
