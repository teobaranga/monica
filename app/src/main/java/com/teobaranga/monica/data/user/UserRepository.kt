package com.teobaranga.monica.data.user

import com.skydoves.sandwich.getOrNull
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
            val meResponse = userApi.getMe().getOrNull()
            if (meResponse != null) {
                val me = Me(
                    id = meResponse.data.contact.id,
                    firstName = meResponse.data.firstName,
                    initials = meResponse.data.contact.initials,
                    avatarUrl = meResponse.data.contact.info.avatar.url,
                    avatarColor = meResponse.data.contact.info.avatar.color,
                )
                _me.emit(me)
            }
        }
    }
}
