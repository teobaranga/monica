package com.teobaranga.monica

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Provider

@HiltAndroidApp
class MonicaApp : Application(), ImageLoaderFactory {

    @Inject
    lateinit var timberTrees: Set<@JvmSuppressWildcards Timber.Tree>

    @Inject
    lateinit var imageLoader: Provider<ImageLoader>

    override fun onCreate() {
        super.onCreate()

        Timber.plant(*timberTrees.toTypedArray())
    }

    override fun newImageLoader(): ImageLoader {
        return imageLoader.get()
    }
}
