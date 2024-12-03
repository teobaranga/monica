package com.teobaranga.monica.ui

import android.content.Context
import coil.ImageLoader
import com.teobaranga.monica.inject.runtime.ApplicationContext
import com.teobaranga.monica.ui.avatar.UserAvatarFetcherFactory
import me.tatarka.inject.annotations.Provides
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesTo
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

@ContributesTo(AppScope::class)
interface ImageLoaderComponent {

    @Provides
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
