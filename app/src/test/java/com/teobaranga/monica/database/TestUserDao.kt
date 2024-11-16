package com.teobaranga.monica.database

import com.teobaranga.monica.data.user.MeEntity
import com.teobaranga.monica.data.user.MeFullDetails
import com.teobaranga.monica.data.user.UserDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TestUserDao @Inject constructor() : UserDao {

    private val me = MutableStateFlow<MeEntity?>(null)

    override fun getMe(): Flow<MeFullDetails?> {
        return me.map { meEntity ->
            meEntity?.let {
                MeFullDetails(
                    info = it,
                    contact = null,
                )
            }
        }
    }

    override suspend fun upsertMe(entity: MeEntity) {
        me.value = entity
    }
}
