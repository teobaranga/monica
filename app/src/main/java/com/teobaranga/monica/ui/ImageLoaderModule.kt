package com.teobaranga.monica.ui

import android.content.Context
import coil.ImageLoader
import com.teobaranga.monica.ui.avatar.UserAvatarFetcherFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ImageLoaderModule {

    @Provides
    @Singleton
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
