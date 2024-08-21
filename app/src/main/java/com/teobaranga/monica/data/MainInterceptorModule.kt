package com.teobaranga.monica.data

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import okhttp3.Interceptor

@Module
@InstallIn(SingletonComponent::class)
interface MainInterceptorModule {

    @Binds
    @IntoSet
    fun bindOAuthInterceptor(oAuthInterceptor: OAuthInterceptor): Interceptor

    @Binds
    @IntoSet
    fun bindHostSelectionInterceptor(hostSelectionInterceptor: HostSelectionInterceptor): Interceptor
}
