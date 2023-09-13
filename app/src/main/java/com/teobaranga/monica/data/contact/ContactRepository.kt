package com.teobaranga.monica.data.contact

import com.teobaranga.monica.util.coroutines.Dispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@OptIn(ExperimentalCoroutinesApi::class)
@Singleton
class ContactRepository @Inject constructor(
    private val dispatcher: Dispatcher,
    private val contactApi: ContactApi,
) {
    private val scope = CoroutineScope(SupervisorJob() + dispatcher.io)

    private val _contacts = MutableSharedFlow<Map<Int, Contact>>(replay = 1)

    init {
        scope.launch(dispatcher.io) {
            val contactsResponse = contactApi.getContacts()
            if (!contactsResponse.isSuccessful) {
                Timber.w("Error while loading contacts: %s", contactsResponse.errorBody())
                return@launch
            }
            val contacts = requireNotNull(contactsResponse.body())
            val contactsMap = contacts.data
                .map {
                    Contact(
                        id = it.id,
                        firstName = it.firstName,
                        lastName = it.lastName,
                        initials = it.initials,
                        avatar = Contact.Avatar(
                            url = it.info.avatar.url,
                            color = it.info.avatar.color,
                        ),
                    )
                }
                .associateBy { it.id }
            _contacts.emit(contactsMap)
        }
    }

    fun getContacts(): Flow<List<Contact>> {
        return _contacts.mapLatest {
            it.values.toList()
        }
    }

    fun getContact(id: Int): Flow<Contact> {
        return _contacts
            .mapLatest {
                it[id]
            }
            .filterNotNull()
    }
}
