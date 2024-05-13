package com.teobaranga.monica.data.user

import com.skydoves.sandwich.getOrNull
import com.teobaranga.monica.contacts.data.ContactRepository
import com.teobaranga.monica.contacts.data.toExternalModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.mapLatest
import javax.inject.Inject
import javax.inject.Singleton

@OptIn(ExperimentalCoroutinesApi::class)
@Singleton
internal class UserRepository @Inject constructor(
    private val userApi: UserApi,
    private val userDao: UserDao,
    private val contactRepository: ContactRepository,
) {

    val me = userDao.getMe()
        .filterNotNull()
        .mapLatest { meFullDetails ->
            Me(
                firstName = meFullDetails.info.firstName,
                contact = meFullDetails.contact?.toExternalModel(),
            )
        }

    suspend fun sync() {
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
