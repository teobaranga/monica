package com.teobaranga.monica.setup

import androidx.lifecycle.SavedStateHandle
import com.teobaranga.monica.core.dispatcher.DispatcherImpl
import com.teobaranga.monica.data.ApiComponent
import com.teobaranga.monica.data.DaosComponent
import com.teobaranga.monica.data.DataStoreComponent
import com.teobaranga.monica.data.TestDataStore
import com.teobaranga.monica.data.user.UserDao
import com.teobaranga.monica.work.WorkManagerWorkScheduler
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.MergeComponent
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

@MergeComponent(
    AppScope::class,
    exclude = [
        WorkManagerWorkScheduler::class,
        DispatcherImpl::class,
        DaosComponent::class,
        ApiComponent::class,
        DataStoreComponent::class,
    ],
)
@SingleIn(AppScope::class)
interface SetupComponent {
    fun setupViewModel(): (SavedStateHandle) -> SetupViewModel
    fun userDao(): UserDao
    fun dataStore(): TestDataStore
}
