package com.teobaranga.monica.contacts.list

import androidx.compose.runtime.Stable
import androidx.paging.PagingData
import com.teobaranga.monica.contacts.list.model.Contact
import kotlinx.coroutines.flow.Flow

@Stable
class ContactsUiState(
    val items: Flow<PagingData<Contact>>,
)