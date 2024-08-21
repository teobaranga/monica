package com.teobaranga.monica.data

import com.skydoves.sandwich.retrofit.adapters.ApiResponseCallAdapterFactory
import com.squareup.moshi.Moshi
import com.teobaranga.monica.contacts.data.ContactApi
import com.teobaranga.monica.data.adapter.AlwaysSerializeNullsFactory
import com.teobaranga.monica.data.adapter.LocalDateAdapter
import com.teobaranga.monica.data.adapter.OffsetDateTimeAdapter
import com.teobaranga.monica.data.adapter.UuidAdapter
import com.teobaranga.monica.data.photo.PhotoApi
import com.teobaranga.monica.data.user.UserApi
import com.teobaranga.monica.journal.data.JournalApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Provides
    @Singleton
    fun provideMoshiConverterFactory(): MoshiConverterFactory {
        val moshi = Moshi.Builder()
            .add(AlwaysSerializeNullsFactory())
            .add(OffsetDateTimeAdapter())
            .add(LocalDateAdapter())
            .add(UuidAdapter())
            .build()
        return MoshiConverterFactory.create(moshi)
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        interceptors: Set<@JvmSuppressWildcards Interceptor>,
        moshiConverterFactory: MoshiConverterFactory,
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
            .baseUrl("https://app.monicahq.com/")
            .addConverterFactory(moshiConverterFactory)
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
