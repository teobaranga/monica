package com.teobaranga.monica.ui

import android.content.Context
import coil.ImageLoader
import com.teobaranga.monica.ui.avatar.UserAvatarFetcherFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import com.r0adkll.kimchi.annotations.ContributesTo
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn
import javax.inject.Singleton

@ContributesTo(AppScope::class)
interface ImageLoaderComponent {

    @me.tatarka.inject.annotations.Provides
    @SingleIn(AppScope::class)
    fun provideImageLoader(
        @ApplicationContext context: Context,
        userAvatarFetcherFactory: UserAvatarFetcherFactory,
    ): ImageLoader {
        return ImageLoader.Builder(context)
            .components {
                add(userAvatarFetcherFactory)
            }
            .build()
    }
}
