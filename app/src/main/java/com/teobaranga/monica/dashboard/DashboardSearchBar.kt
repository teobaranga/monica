package com.teobaranga.monica.dashboard

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
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.core.graphics.toColorInt
import com.teobaranga.monica.MonicaBackground
import com.teobaranga.monica.ui.UserAvatar
import com.teobaranga.monica.ui.theme.MonicaTheme
import com.teobaranga.monica.util.compose.keyboardAsState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardSearchBar(
    modifier: Modifier = Modifier,
    userInfo: DashboardUiState.UserInfo,
) {
    var query by rememberSaveable { mutableStateOf("") }
    var shouldBeActive by rememberSaveable { mutableStateOf(false) }
    val isImeVisible by keyboardAsState()
    Box(
        modifier = Modifier
            .zIndex(1f)
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures {
                    shouldBeActive = false
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
                val userAvatar = remember(userInfo) {
                    if (userInfo.avatarData != null) {
                        UserAvatar.Photo(
                            data = userInfo.avatarData,
                        )
                    } else {
                        UserAvatar.Initials(
                            initials = userInfo.initials,
                            color = Color(userInfo.avatarColor.toColorInt()),
                        )
                    }
                }
                UserAvatar(
                    userAvatar = userAvatar,
                )
            },
            placeholder = {
                Text(text = "Search")
            }
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
                DashboardSearchBar(
                    userInfo = DashboardUiState.UserInfo(
                        name = "Teo",
                        initials = "TB",
                        avatarColor = "#709512",
                        avatarData = null,
                    )
                )
            }
        }
    }
}
