package com.teobaranga.monica.work

import android.content.Context
import androidx.work.WorkManager
import com.teobaranga.monica.core.inject.ApplicationContext
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn

@ContributesTo(AppScope::class)
interface WorkComponent {

    @Provides
    @SingleIn(AppScope::class)
    fun provideWorkManager(@ApplicationContext context: Context): WorkManager {
        return WorkManager.getInstance(context)
    }
}
