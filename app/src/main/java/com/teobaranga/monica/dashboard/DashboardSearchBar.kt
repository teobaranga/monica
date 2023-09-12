package com.teobaranga.monica.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import com.teobaranga.monica.MonicaBackground
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
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(Color(userInfo.avatarColor.toColorInt())),
                contentAlignment = Alignment.Center,
            ) {
                Text(text = userInfo.initials)
            }
        },
        placeholder = {
            Text(text = "Search")
        }
    ) {
        // TODO
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
                    )
                )
            }
        }
    }
}
