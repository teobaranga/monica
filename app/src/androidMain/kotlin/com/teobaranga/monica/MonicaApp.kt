package com.teobaranga.monica

import android.app.Application
import androidx.lifecycle.ViewModelProvider
import androidx.work.Configuration
import coil3.ImageLoader
import coil3.PlatformContext
import coil3.SingletonImageLoader
import com.diamondedge.logging.KmLogging
import com.teobaranga.kotlin.inject.viewmodel.runtime.compose.ViewModelFactoryOwner
import com.teobaranga.monica.core.inject.ScopedViewModelFactoryProvider
import com.teobaranga.monica.di.createAppComponent
import com.teobaranga.monica.setup.domain.SYNC_WORKER_WORK_NAME
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import kotlin.reflect.KClass

class MonicaApp :
    Application(),
    SingletonImageLoader.Factory,
    Configuration.Provider,
    ScopedViewModelFactoryProvider {

    init {
        instance = this
    }

    /**
     * DI component for the entire app. Should not be publicly exposed because this class is not available
     * outside of the app module so any different modules would not be able to access it directly.
     */
    private val appComponent = createAppComponent()

    override fun onCreate() {
        super.onCreate()

        KmLogging.setLoggers(*appComponent.loggers.toTypedArray())

        appComponent.workScheduler.schedule(SYNC_WORKER_WORK_NAME)
    }

    override fun newImageLoader(context: PlatformContext): ImageLoader {
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

    companion object {
        lateinit var instance: MonicaApp
            private set
    }
}
