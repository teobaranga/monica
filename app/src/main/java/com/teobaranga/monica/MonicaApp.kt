package com.teobaranga.monica

import android.app.Application
import androidx.lifecycle.ViewModelProvider
import androidx.work.Configuration
import coil.ImageLoader
import coil.ImageLoaderFactory
import com.teobaranga.monica.di.AndroidAppComponent
import com.teobaranga.monica.di.create
import com.teobaranga.monica.inject.runtime.ViewModelFactoryOwner
import com.teobaranga.monica.sync.SyncWorker
import timber.log.Timber

class MonicaApp :
    Application(),
    ImageLoaderFactory,
    Configuration.Provider,
    ViewModelFactoryOwner {

    /**
     * DI component for the entire app. Should not be publicly exposed because this class is not available
     * outside of the app module so any different modules would not be able to access it directly.
     */
    private val appComponent = AndroidAppComponent::class.create(this)

    override fun onCreate() {
        super.onCreate()

        Timber.plant(*appComponent.timberTrees.toTypedArray())

        appComponent.workScheduler.schedule(SyncWorker.WORK_NAME)
    }

    override fun newImageLoader(): ImageLoader {
        return appComponent.imageLoader
    }

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(appComponent.workerFactory)
            .build()

    override val viewModelFactory: ViewModelProvider.Factory
        get() = appComponent.viewModelFactory
}
