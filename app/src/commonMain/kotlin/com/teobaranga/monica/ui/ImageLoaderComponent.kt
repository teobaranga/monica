package com.teobaranga.monica.ui

import coil3.ImageLoader
import coil3.PlatformContext
import com.teobaranga.monica.ui.avatar.UserAvatarFetcherFactory
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn

@ContributesTo(AppScope::class)
interface ImageLoaderComponent {

    @Provides
    @SingleIn(AppScope::class)
    fun provideImageLoader(
        context: PlatformContext,
        userAvatarFetcherFactory: UserAvatarFetcherFactory,
    ): ImageLoader {
        return ImageLoader.Builder(context)
            .components {
                add(userAvatarFetcherFactory)
            }
            .build()
    }
}
