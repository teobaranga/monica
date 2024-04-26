package com.teobaranga.monica.home

import com.ramcosta.composedestinations.spec.DirectionDestinationSpec
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HomeNavigationManager @Inject constructor() {

    private val _navigation = MutableSharedFlow<DirectionDestinationSpec>(extraBufferCapacity = 1)
    val navigation = _navigation.asSharedFlow()

    fun navigateTo(destination: DirectionDestinationSpec) {
        _navigation.tryEmit(destination)
    }
}
