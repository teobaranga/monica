package com.teobaranga.monica.auth

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.teobaranga.monica.core.dispatcher.Dispatcher
import com.teobaranga.monica.data.user.UserDao
import com.teobaranga.monica.settings.getTokenStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

interface AuthorizationRepository {

    val isLoggedIn: StateFlow<Boolean?>
}

@Singleton
class MonicaAuthorizationRepository @Inject constructor(
    dispatcher: Dispatcher,
    dataStore: DataStore<Preferences>,
    userDao: UserDao,
) : AuthorizationRepository {
    private val scope = CoroutineScope(SupervisorJob() + dispatcher.io)

    override val isLoggedIn: StateFlow<Boolean?> = combine(
        dataStore.data,
        userDao.getMe(),
    ) { preferences, me ->
        val tokenStorage = preferences.getTokenStorage()
        when {
            tokenStorage.authorizationCode == null -> false
            me == null -> false
            else -> true
        }
    }
        .onEach {
            Timber.d("User logged in: $it")
        }
        .stateIn(
            scope = scope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null,
        )
}
