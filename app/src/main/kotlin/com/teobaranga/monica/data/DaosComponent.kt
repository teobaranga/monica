/*
 * Copyright 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.teobaranga.monica.data

import com.teobaranga.monica.activities.data.ContactActivitiesDao
import com.teobaranga.monica.contacts.data.ContactDao
import com.teobaranga.monica.data.photo.PhotoDao
import com.teobaranga.monica.data.user.UserDao
import com.teobaranga.monica.genders.data.GendersDao
import me.tatarka.inject.annotations.Provides
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesTo

@ContributesTo(AppScope::class)
interface DaosComponent {

    @Provides
    fun providesUserDao(database: MonicaDatabase): UserDao = database.userDao()

    @Provides
    fun providesContactDao(database: MonicaDatabase): ContactDao = database.contactDao()

    @Provides
    fun providesContactActivitiesDao(database: MonicaDatabase): ContactActivitiesDao = database.contactActivitiesDao()

    @Provides
    fun providesPhotoDao(database: MonicaDatabase): PhotoDao = database.photoDao()

    @Provides
    fun providesGendersDao(database: MonicaDatabase): GendersDao = database.gendersDao()
}
