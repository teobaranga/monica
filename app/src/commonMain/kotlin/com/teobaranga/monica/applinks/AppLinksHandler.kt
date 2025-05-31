package com.teobaranga.monica.applinks

import io.ktor.http.Url
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

object AppLinksHandler {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    private val _navigationEvents = MutableSharedFlow<Url>()
    val url: Flow<Url> = _navigationEvents.asSharedFlow()

    fun handle(url: String) {
        scope.launch {
            _navigationEvents.emit(Url(url))
        }
    }
}
