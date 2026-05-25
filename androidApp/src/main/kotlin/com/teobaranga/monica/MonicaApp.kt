package com.teobaranga.monica

import android.app.Application
import androidx.work.Configuration
import coil3.ImageLoader
import coil3.PlatformContext
import coil3.SingletonImageLoader
import com.diamondedge.logging.KmLogging
import com.teobaranga.monica.core.inject.ScopedViewModelFactoryProvider
import com.teobaranga.monica.di.AppComponent
import com.teobaranga.monica.setup.domain.SYNC_WORKER_WORK_NAME
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.createGraphFactory
import dev.zacsweers.metrox.viewmodel.MetroViewModelFactory
import kotlin.reflect.KClass

class MonicaApp :
    Application(),
    SingletonImageLoader.Factory,
    Configuration.Provider,
    ScopedViewModelFactoryProvider {


    /**
     * DI component for the entire app. Should not be publicly exposed because this class is not available
     * outside of the app module so any different modules would not be able to access it directly.
     */
    private val appComponent = createGraphFactory<AppComponent.Factory>().create(this)

    override fun onCreate() {
        super.onCreate()

        if (!BuildConfig.DEBUG) {
            initializeSentry()
        }

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

    override fun getViewModelFactory(scope: KClass<out Any>): MetroViewModelFactory {
        return when (scope) {
            AppScope::class -> appComponent.metroViewModelFactory

            else -> error("Unknown scope $scope")
        }
    }
}
