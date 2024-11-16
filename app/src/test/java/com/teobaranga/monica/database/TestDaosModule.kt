package com.teobaranga.monica.database

import com.teobaranga.monica.data.user.UserDao
import dagger.Binds
import dagger.Module

@Module
abstract class TestDaosModule {

    @Binds
    abstract fun bindsUserDao(dao: TestUserDao): UserDao
}
