package com.teobaranga.monica.contacts.detail

import ContactsNavGraph
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.teobaranga.monica.contacts.detail.ui.ContactInfoContactSection
import com.teobaranga.monica.contacts.detail.ui.ContactInfoPersonalSection
import com.teobaranga.monica.contacts.detail.ui.ContactInfoRelationshipsSection
import com.teobaranga.monica.contacts.detail.ui.ContactInfoWorkSection
import com.teobaranga.monica.contacts.detail.ui.fullNameItem
import com.teobaranga.monica.contacts.detail.ui.infoSectionTabs
import com.teobaranga.monica.contacts.detail.ui.userAvatarItem
import com.teobaranga.monica.ui.PreviewPixel4
import com.teobaranga.monica.ui.avatar.UserAvatar
import com.teobaranga.monica.ui.theme.MonicaTheme

@ContactsNavGraph
@Destination
@Composable
fun ContactDetail(
    navigator: DestinationsNavigator,
    contactId: Int,
) {
    val viewModel = hiltViewModel<ContactDetailViewModel, ContactDetailViewModel.Factory>(
        creationCallback = { factory: ContactDetailViewModel.Factory ->
            factory.create(contactId)
        },
    )
    val contactDetail by viewModel.contact.collectAsStateWithLifecycle()
    Crossfade(
        targetState = contactDetail,
        label = "ContactDetailScreen",
    ) { contactDetail ->
        when (contactDetail) {
            null -> {
                // TODO Loading
            }

            else -> {
                ContactDetailScreen(
                    contactDetail = contactDetail,
                    onBack = navigator::popBackStack,
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
private fun ContactDetailScreen(
    contactDetail: ContactDetail,
    onBack: () -> Unit,
) {
    Scaffold(
        modifier = Modifier,
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(
                        onClick = onBack,
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "",
                        )
                    }
                },
            )
        },
    ) { contentPadding ->
        val pagerState = rememberPagerState(
            pageCount = { contactDetail.infoSections.size },
        )
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(contentPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            userAvatarItem(contactDetail.userAvatar)
            fullNameItem(contactDetail.fullName)
            infoSectionTabs(
                pagerState = pagerState,
                infoSections = contactDetail.infoSections,
            )
        }
    }
}

@Composable
@PreviewPixel4
private fun PreviewContactDetailScreen() {
    MonicaTheme {
        ContactDetailScreen(
            contactDetail = ContactDetail(
                fullName = "John Doe (Johnny)",
                userAvatar = UserAvatar(
                    contactId = -1,
                    initials = "JD",
                    color = "#709512",
                    avatarUrl = null,
                ),
                infoSections = listOf(
                    ContactInfoPersonalSection(birthday = null),
                    ContactInfoContactSection,
                    ContactInfoWorkSection,
                    ContactInfoRelationshipsSection,
                ),
            ),
            onBack = { },
        )
    }
}
