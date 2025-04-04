package com.teobaranga.monica.data.user

import com.skydoves.sandwich.getOrNull
import com.teobaranga.monica.contacts.data.ContactRepository
import com.teobaranga.monica.contacts.data.toExternalModel
import com.teobaranga.monica.user.data.local.IUserRepository
import com.teobaranga.monica.user.data.local.Me
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.mapLatest
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

@Inject
@SingleIn(AppScope::class)
@ContributesBinding(AppScope::class)
class UserRepository(
    private val userApi: UserApi,
    private val userDao: UserDao,
    private val contactRepository: ContactRepository,
): IUserRepository {

    override val me = userDao.getMe()
        .filterNotNull()
        .mapLatest { meFullDetails ->
            Me(
                firstName = meFullDetails.info.firstName,
                contact = meFullDetails.contact?.toExternalModel(),
            )
        }

    override suspend fun sync() {
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
