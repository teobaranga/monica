package com.teobaranga.monica.home

import com.teobaranga.monica.destinations.DirectionDestination
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HomeNavigationManager @Inject constructor() {

    private val _navigation = MutableSharedFlow<DirectionDestination>(extraBufferCapacity = 1)
    val navigation = _navigation.asSharedFlow()

    fun navigateTo(destination: DirectionDestination) {
        _navigation.tryEmit(destination)
    }
}
