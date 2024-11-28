package com.teobaranga.monica.data

import com.skydoves.sandwich.retrofit.adapters.ApiResponseCallAdapterFactory
import com.teobaranga.monica.MONICA_URL
import com.teobaranga.monica.contacts.data.ContactApi
import com.teobaranga.monica.data.photo.PhotoApi
import com.teobaranga.monica.data.user.UserApi
import com.teobaranga.monica.journal.data.JournalApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    private val Json = Json {
        ignoreUnknownKeys = true
    }

    @Provides
    @Singleton
    fun provideConverterFactory(): Converter.Factory {
        return Json
            .asConverterFactory("application/json; charset=UTF8".toMediaType())
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        interceptors: Set<@JvmSuppressWildcards Interceptor>,
        converterFactory: Converter.Factory,
    ): Retrofit {
        val client = OkHttpClient.Builder()
            .apply {
                for (interceptor in interceptors) {
                    addInterceptor(interceptor)
                }
            }
            .build()
        val retrofit = Retrofit.Builder()
            .client(client)
            .baseUrl(MONICA_URL)
            .addConverterFactory(converterFactory)
            .addCallAdapterFactory(ApiResponseCallAdapterFactory.create())
            .build()

        return retrofit
    }

    @Provides
    @Singleton
    fun provideApi(retrofit: Retrofit): MonicaApi {
        return retrofit.create(MonicaApi::class.java)
    }

    @Provides
    @Singleton
    fun provideUserApi(retrofit: Retrofit): UserApi {
        return requireNotNull(retrofit.create(UserApi::class.java))
    }

    @Provides
    @Singleton
    fun provideContactApi(retrofit: Retrofit): ContactApi {
        return retrofit.create(ContactApi::class.java)
    }

    @Provides
    @Singleton
    fun providePhotoApi(retrofit: Retrofit): PhotoApi {
        return retrofit.create(PhotoApi::class.java)
    }

    @Provides
    @Singleton
    fun provideJournalApi(retrofit: Retrofit): JournalApi {
        return retrofit.create(JournalApi::class.java)
    }
}
