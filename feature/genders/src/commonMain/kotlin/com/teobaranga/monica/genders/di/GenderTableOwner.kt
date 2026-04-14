package com.teobaranga.monica.genders.di

import com.teobaranga.monica.genders.data.GendersDao

interface GenderTableOwner {

    fun genderDao(): GendersDao
}
