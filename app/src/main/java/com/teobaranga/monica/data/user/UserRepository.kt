package com.teobaranga.monica.data.user

import com.skydoves.sandwich.getOrNull
import com.teobaranga.monica.data.contact.ContactRepository
import com.teobaranga.monica.util.coroutines.Dispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.launch
import okio.ByteString.Companion.decodeBase64
import javax.inject.Inject
import javax.inject.Singleton

@OptIn(ExperimentalCoroutinesApi::class)
@Singleton
class UserRepository @Inject constructor(
    private val dispatcher: Dispatcher,
    private val userApi: UserApi,
    private val userDao: UserDao,
    private val contactRepository: ContactRepository,
) {
    private val scope = CoroutineScope(SupervisorJob() + dispatcher.io)

    val me = userDao.getMe()
        .filterNotNull()
        .mapLatest { meFullDetails ->
            val contact = meFullDetails.contact?.let { contact ->
                val avatar = meFullDetails.photos
                    .firstOrNull { photo ->
                        photo.fileName in contact.avatarUrl.orEmpty()
                    }
                Me.Contact(
                    initials = contact.initials,
                    avatarColor = contact.avatarColor,
                    avatarData = avatar?.data?.decodeBase64()?.asByteBuffer(),
                )
            }
            Me(
                firstName = meFullDetails.info.firstName,
                contact = contact,
            )
        }

    init {
        scope.launch(dispatcher.io) {
            val meResponse = userApi.getMe().getOrNull()
            if (meResponse != null) {
                val contactId = meResponse.data.contact?.id
                if (contactId != null) {
                    contactRepository.syncContact(contactId)
                }

                val me = MeEntity(
                    id = meResponse.data.id,
                    firstName = meResponse.data.firstName,
                    contactId = contactId,
                )
                userDao.upsertMe(me)
            }
        }
    }
}
