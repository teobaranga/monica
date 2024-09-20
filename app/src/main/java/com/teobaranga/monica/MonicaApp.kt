package com.teobaranga.monica

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import androidx.work.WorkManager
import coil.ImageLoader
import coil.ImageLoaderFactory
import com.teobaranga.monica.sync.SyncWorker
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Provider

@HiltAndroidApp
class MonicaApp : Application(), ImageLoaderFactory, Configuration.Provider {

    @Inject
    lateinit var timberTrees: Set<@JvmSuppressWildcards Timber.Tree>

    @Inject
    lateinit var imageLoader: Provider<ImageLoader>

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    @Inject
    lateinit var workManager: WorkManager

    override fun onCreate() {
        super.onCreate()

        Timber.plant(*timberTrees.toTypedArray())

        SyncWorker.enqueue(workManager)
    }

    override fun newImageLoader(): ImageLoader {
        return imageLoader.get()
    }

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
}
