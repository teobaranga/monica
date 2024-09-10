package com.teobaranga.monica.auth

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.teobaranga.monica.data.user.UserDao
import com.teobaranga.monica.settings.getTokenStorage
import com.teobaranga.monica.core.dispatcher.Dispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

interface AuthorizationRepository {

    val isLoggedIn: StateFlow<Boolean?>
}

@Singleton
class MonicaAuthorizationRepository @Inject constructor(
    private val dispatcher: Dispatcher,
    private val dataStore: DataStore<Preferences>,
    private val userDao: UserDao,
) : AuthorizationRepository {
    private val scope = CoroutineScope(SupervisorJob() + dispatcher.io)

    private val _isLoggedIn = MutableStateFlow<Boolean?>(null)
    override val isLoggedIn: StateFlow<Boolean?> = _isLoggedIn.asStateFlow()

    init {
        scope.launch {
            combine(
                dataStore.data,
                userDao.getMe(),
            ) { preferences, me ->
                val tokenStorage = preferences.getTokenStorage()
                withContext(dispatcher.main) {
                    val isLoggedIn = when {
                        tokenStorage.authorizationCode == null -> false
                        me == null -> false
                        else -> true
                    }
                    _isLoggedIn.emit(isLoggedIn)
                }
            }.collect()
        }
    }
}
