package com.teobaranga.monica.setup

import com.teobaranga.monica.data.MonicaApi
import com.teobaranga.monica.data.TestDataStore
import com.teobaranga.monica.data.user.UserDao
import com.teobaranga.monica.work.TestWorkScheduler
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.DependencyGraph
import dev.zacsweers.metro.SingleIn

@DependencyGraph(AppScope::class)
@SingleIn(AppScope::class)
interface SetupComponent {
    val setupViewModel: SetupViewModel
    fun userDao(): UserDao
    fun dataStore(): TestDataStore
    fun testWorkScheduler(): TestWorkScheduler
    fun monicaApi(): MonicaApi
}
