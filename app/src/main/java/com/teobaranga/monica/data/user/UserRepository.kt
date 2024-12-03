package com.teobaranga.monica.data.user

import com.skydoves.sandwich.getOrNull
import com.teobaranga.monica.contacts.data.ContactRepository
import com.teobaranga.monica.contacts.data.toExternalModel
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.mapLatest
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

@SingleIn(AppScope::class)
@Inject
internal class UserRepository(
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
