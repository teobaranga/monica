package com.teobaranga.monica.user.data

import com.skydoves.sandwich.getOrNull
import com.teobaranga.monica.contact.data.ContactRepository
import com.teobaranga.monica.user.data.local.MeEntity
import com.teobaranga.monica.user.data.local.UserDao
import com.teobaranga.monica.user.data.remote.UserApi
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn
import kotlinx.coroutines.flow.filterNotNull

@Inject
@SingleIn(AppScope::class)
class UserRepository(
    private val userApi: UserApi,
    private val userDao: UserDao,
    private val contactRepository: ContactRepository,
) {

    val me = userDao.getMe()
        .filterNotNull()

    suspend fun sync() {
        val meResponse = userApi.getMe().getOrNull()
        if (meResponse != null) {
            val contactId = meResponse.data.contact?.id
            if (contactId != null) {
                contactRepository.syncContact(contactId)
            }

            val me = MeEntity(
                id = meResponse.data.id,
                name = meResponse.data.name,
                firstName = meResponse.data.firstName,
                lastName = meResponse.data.lastName,
                email = meResponse.data.email,
                contactId = contactId,
            )
            userDao.upsertMe(me)
        }
    }
}
