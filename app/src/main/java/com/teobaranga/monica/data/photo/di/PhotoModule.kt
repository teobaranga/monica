package com.teobaranga.monica.data.photo.di

import com.teobaranga.monica.account.AccountListener
import com.teobaranga.monica.data.photo.PhotoSynchronizer
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(SingletonComponent::class)
abstract class PhotoModule {

    @Binds
    @IntoSet
    abstract fun bindPhotoSynchronizer(synchronizer: PhotoSynchronizer): AccountListener
}
