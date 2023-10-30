package com.teobaranga.monica.data.sync

interface Synchronizer {
    suspend fun sync()

    enum class State {
        IDLE,
        REFRESHING,
    }
}
