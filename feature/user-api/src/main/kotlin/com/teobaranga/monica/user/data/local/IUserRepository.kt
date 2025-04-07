package com.teobaranga.monica.user.data.local

import kotlinx.coroutines.flow.Flow

interface IUserRepository {

    val me: Flow<Me>

    suspend fun sync()
}
