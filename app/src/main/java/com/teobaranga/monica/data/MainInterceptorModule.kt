package com.teobaranga.monica.data

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import okhttp3.Interceptor

@Module
@InstallIn(SingletonComponent::class)
object MainInterceptorModule {

    @Provides
    @IntoSet
    fun provideOAuthInterceptor(oAuthInterceptor: OAuthInterceptor): Interceptor {
        return oAuthInterceptor
    }
}
