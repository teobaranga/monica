package com.teobaranga.monica.contacts.detail.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.teobaranga.monica.ui.avatar.UserAvatar
import kotlinx.coroutines.launch

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

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
fun LazyListScope.infoSectionTabs(
    pagerState: PagerState,
    infoSections: List<ContactInfoSection>,
) {
    item {
        val coroutineScope = rememberCoroutineScope()
        PrimaryScrollableTabRow(
            modifier = Modifier
                .padding(top = 28.dp),
            selectedTabIndex = pagerState.currentPage,
        ) {
            infoSections.forEachIndexed { index, infoSection ->
                Tab(
                    selected = false,
                    onClick = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    },
                    text = {
                        Text(
                            text = infoSection.title,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    },
                )
            }
        }
    }
    item {
        HorizontalPager(
            state = pagerState
        ) { page ->
            infoSections[page].Content(modifier = Modifier)
        }
    }
}
