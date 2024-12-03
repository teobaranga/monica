package com.teobaranga.monica.data

import com.skydoves.sandwich.retrofit.adapters.ApiResponseCallAdapterFactory
import com.teobaranga.monica.MONICA_URL
import com.teobaranga.monica.contacts.data.ContactApi
import com.teobaranga.monica.data.photo.PhotoApi
import com.teobaranga.monica.data.user.UserApi
import com.teobaranga.monica.journal.data.JournalApi
import kotlinx.serialization.json.Json
import me.tatarka.inject.annotations.Provides
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesTo
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

private val AppJson = Json {
    ignoreUnknownKeys = true
}

@ContributesTo(AppScope::class)
interface ApiComponent {

    @Provides
    @SingleIn(AppScope::class)
    fun provideConverterFactory(): Converter.Factory {
        return AppJson
            .asConverterFactory("application/json; charset=UTF8".toMediaType())
    }

    @Provides
    @SingleIn(AppScope::class)
    fun provideRetrofit(
        interceptors: Set<Interceptor>,
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
    @SingleIn(AppScope::class)
    fun provideApi(retrofit: Retrofit): MonicaApi {
        return retrofit.create(MonicaApi::class.java)
    }

    @Provides
    @SingleIn(AppScope::class)
    fun provideUserApi(retrofit: Retrofit): UserApi {
        return requireNotNull(retrofit.create(UserApi::class.java))
    }

    @Provides
    @SingleIn(AppScope::class)
    fun provideContactApi(retrofit: Retrofit): ContactApi {
        return retrofit.create(ContactApi::class.java)
    }

    @Provides
    @SingleIn(AppScope::class)
    fun providePhotoApi(retrofit: Retrofit): PhotoApi {
        return retrofit.create(PhotoApi::class.java)
    }

    @Provides
    @SingleIn(AppScope::class)
    fun provideJournalApi(retrofit: Retrofit): JournalApi {
        return retrofit.create(JournalApi::class.java)
    }
}
