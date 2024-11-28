package com.teobaranga.monica.setup

import com.teobaranga.monica.auth.AuthorizationModule
import com.teobaranga.monica.data.ApiModule
import com.teobaranga.monica.data.DataStoreModule
import com.teobaranga.monica.data.TestDataStore
import com.teobaranga.monica.data.user.UserDao
import com.teobaranga.monica.database.TestDaosModule
import com.teobaranga.monica.work.TestWorkScheduleModule
import dagger.Component
import javax.inject.Singleton

@Component(
    modules = [
        ViewModelModule::class,
        DispatcherModule::class,
        DataStoreModule::class,
        AuthorizationModule::class,
        TestDaosModule::class,
        ApiModule::class,
        TestWorkScheduleModule::class,
    ],
)
@Singleton
interface SetupComponent {
    fun setupViewModel(): SetupViewModel
    fun userDao(): UserDao
    fun dataStore(): TestDataStore
}
