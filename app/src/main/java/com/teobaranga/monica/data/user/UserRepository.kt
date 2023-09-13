package com.teobaranga.monica.data.user

import com.teobaranga.monica.util.coroutines.Dispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val dispatcher: Dispatcher,
    private val userApi: UserApi,
) {
    private val scope = CoroutineScope(SupervisorJob() + dispatcher.io)

    private val _me = MutableStateFlow<Me?>(null)
    val me = _me.asStateFlow()

    init {
        scope.launch(dispatcher.io) {
            val meResponse = userApi.getMe()
            if (meResponse.isSuccessful) {
                val body = requireNotNull(meResponse.body())
                val me = Me(
                    id = body.data.contact.id,
                    firstName = body.data.firstName,
                    initials = body.data.contact.initials,
                    avatarUrl = body.data.contact.info.avatar.url,
                    avatarColor = body.data.contact.info.avatar.color,
                )
                _me.emit(me)
            }
        }
    }
}
