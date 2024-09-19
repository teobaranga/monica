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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MonicaSearchBar(
    userAvatar: @Composable () -> Unit,
    onSearch: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var query by remember { mutableStateOf("") }
    DockedSearchBar(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        inputField = {
            SearchBarDefaults.InputField(
                modifier = Modifier
                    .fillMaxWidth(),
                query = query,
                onQueryChange = {
                    query = it
                },
                onSearch = onSearch,
                expanded = false,
                onExpandedChange = {
                    // Never expand
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
        expanded = false,
        onExpandedChange = {
            // Never expand
        },
        content = {
            // Display content on a different screen
        },
    )
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
            onSearch = { },
        )
    }
}
