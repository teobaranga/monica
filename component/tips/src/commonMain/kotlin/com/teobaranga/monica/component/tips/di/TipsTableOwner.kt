package com.teobaranga.monica.component.tips.di

import com.teobaranga.monica.component.tips.data.local.TipsDao

interface TipsTableOwner {

    fun tipsDao(): TipsDao
}
