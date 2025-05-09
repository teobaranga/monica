package com.teobaranga.monica.setup

import androidx.lifecycle.SavedStateHandle
import com.teobaranga.monica.data.TestDataStore
import com.teobaranga.monica.data.user.UserDao
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.MergeComponent
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

@MergeComponent(AppScope::class)
@SingleIn(AppScope::class)
interface SetupComponent {
    fun setupViewModel(): (SavedStateHandle) -> SetupViewModel
    fun userDao(): UserDao
    fun dataStore(): TestDataStore
}
