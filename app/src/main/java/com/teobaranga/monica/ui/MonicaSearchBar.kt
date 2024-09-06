package com.teobaranga.monica.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.teobaranga.monica.ui.avatar.UserAvatar
import com.teobaranga.monica.ui.theme.MonicaTheme
import com.teobaranga.monica.util.compose.keyboardAsState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MonicaSearchBar(
    userAvatar: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    state: SearchBarState = rememberSearchBarState(),
) {
    DockedSearchBar(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        inputField = {
            SearchBarDefaults.InputField(
                modifier = Modifier
                    .fillMaxWidth(),
                query = state.query,
                onQueryChange = {
                    state.query = it
                },
                onSearch = {
                    state.shouldBeActive = false
                },
                expanded = state.active,
                onExpandedChange = {
                    state.shouldBeActive = it
                },
                placeholder = {
                    Text(text = "Search something")
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
            )
        },
        expanded = state.active,
        onExpandedChange = {
            state.shouldBeActive = it
        },
        content = {
            // TODO: display results
        },
    )
}

@Stable
class SearchBarState {

    var query by mutableStateOf("")

    var shouldBeActive by mutableStateOf(false)

    val active: Boolean
        @Composable
        get() {
            val isImeVisible by keyboardAsState()
            return shouldBeActive && (query.isNotBlank() || isImeVisible)
        }

}

@Composable
fun rememberSearchBarState(): SearchBarState {
    // TODO make this saveable
    return remember { SearchBarState() }
}

@Preview
@Composable
private fun PreviewDashboardSearchBar() {
    MonicaTheme {
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
