package com.teobaranga.monica

import android.app.Application
import androidx.lifecycle.ViewModelProvider
import androidx.work.Configuration
import coil.ImageLoader
import coil.ImageLoaderFactory
import com.diamondedge.logging.KmLogging
import com.teobaranga.kotlin.inject.viewmodel.runtime.compose.ViewModelFactoryOwner
import com.teobaranga.monica.core.inject.ScopedViewModelFactoryProvider
import com.teobaranga.monica.di.AndroidAppComponent
import com.teobaranga.monica.di.create
import com.teobaranga.monica.sync.SyncWorker
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import kotlin.reflect.KClass

class MonicaApp :
    Application(),
    ImageLoaderFactory,
    Configuration.Provider,
    ScopedViewModelFactoryProvider {

    /**
     * DI component for the entire app. Should not be publicly exposed because this class is not available
     * outside of the app module so any different modules would not be able to access it directly.
     */
    private val appComponent = AndroidAppComponent::class.create(this)

    override fun onCreate() {
        super.onCreate()

        KmLogging.setLoggers(*appComponent.loggers.toTypedArray())

        appComponent.workScheduler.schedule(SyncWorker.WORK_NAME)
    }

    override fun newImageLoader(): ImageLoader {
        return appComponent.imageLoader
    }

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(appComponent.workerFactory)
            .build()

    override fun getViewModelFactoryOwner(scope: KClass<out Any>): ViewModelFactoryOwner {
        return when (scope) {
            AppScope::class -> object : ViewModelFactoryOwner {
                override val viewModelFactory: ViewModelProvider.Factory
                    get() = appComponent.viewModelFactory
            }

            else -> error("Unknown scope $scope")
        }
    }
}
