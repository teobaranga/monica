package com.teobaranga.monica.contacts.detail.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.teobaranga.monica.ui.avatar.UserAvatar

fun LazyListScope.userAvatarItem(userAvatar: UserAvatar) {
    item(
        key = "profile_icon",
        contentType = "profile_icon",
    ) {
        UserAvatar(
            modifier = Modifier
                .padding(top = 28.dp)
                .size(128.dp),
            userAvatar = userAvatar,
            onClick = {
                // TODO
            },
        )
    }
}

fun LazyListScope.fullNameItem(fullName: String) {
    item(
        key = "name",
        contentType = "name",
    ) {
        Text(
            modifier = Modifier
                .padding(top = 28.dp),
            text = fullName,
            style = MaterialTheme.typography.headlineMedium,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
fun LazyListScope.infoSectionTabsItem(
    tabs: List<String>,
) {
    item {
        var selectedTabIndex by remember { mutableIntStateOf(0) }
        PrimaryScrollableTabRow(
            modifier = Modifier
                .padding(top = 28.dp),
            selectedTabIndex = selectedTabIndex,
        ) {
            tabs.forEachIndexed { index, tab ->
                Tab(
                    selected = false,
                    onClick = {
                        selectedTabIndex = index
                    },
                    text = {
                        Text(
                            text = tab,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    },
                )
            }
        }
    }
}
