package com.teobaranga.monica.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
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
import com.teobaranga.monica.settings.getOAuthSettings
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    /**
     * The idea here is that we want a single Retrofit instance in memory but we want to allow
     * changing the base url whenever needed. Since this is immutable, the only solution seems to be
     * checking the cached base url every time Retrofit is injected and recreate the instance with the
     * updated url if needed.
     */

    private var currentRetrofit: Instance? = null

    private data class Instance(
        val retrofit: Retrofit,
        val monicaApi: MonicaApi? = null,
        val userApi: UserApi? = null,
        val contactApi: ContactApi? = null,
        val photoApi: PhotoApi? = null,
        val journalApi: JournalApi? = null,
    )

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
    fun provideRetrofit(
        interceptors: Set<@JvmSuppressWildcards Interceptor>,
        moshiConverterFactory: MoshiConverterFactory,
        dataStore: DataStore<Preferences>,
    ): Retrofit {
        val baseUrl = runBlocking {
            dataStore.data.first()
                .getOAuthSettings()
                .serverAddress ?: throw IllegalStateException("Retrofit requested before a server address was provided")
        }
        currentRetrofit?.retrofit?.let { retrofit ->
            if (retrofit.baseUrl() == baseUrl.toHttpUrlOrNull()) {
                return retrofit
            }
        }
        val client = OkHttpClient.Builder()
            .apply {
                for (interceptor in interceptors) {
                    addInterceptor(interceptor)
                }
            }
            .build()
        val retrofit = Retrofit.Builder()
            .client(client)
            .baseUrl(baseUrl)
            .addConverterFactory(moshiConverterFactory)
            .addCallAdapterFactory(ApiResponseCallAdapterFactory.create())
            .build()

        currentRetrofit = Instance(retrofit)

        return retrofit
    }

    @Provides
    fun provideApi(retrofit: Retrofit): MonicaApi {
        var monicaApi = currentRetrofit?.monicaApi
        if (monicaApi == null) {
            monicaApi = requireNotNull(retrofit.create(MonicaApi::class.java))
            currentRetrofit = currentRetrofit?.copy(monicaApi = monicaApi)
        }
        return monicaApi
    }

    @Provides
    fun provideUserApi(retrofit: Retrofit): UserApi {
        var userApi = currentRetrofit?.userApi
        if (userApi == null) {
            userApi = requireNotNull(retrofit.create(UserApi::class.java))
            currentRetrofit = currentRetrofit?.copy(userApi = userApi)
        }
        return userApi
    }

    @Provides
    fun provideContactApi(retrofit: Retrofit): ContactApi {
        var contactApi = currentRetrofit?.contactApi
        if (contactApi == null) {
            contactApi = requireNotNull(retrofit.create(ContactApi::class.java))
            currentRetrofit = currentRetrofit?.copy(contactApi = contactApi)
        }
        return contactApi
    }

    @Provides
    fun providePhotoApi(retrofit: Retrofit): PhotoApi {
        var photoApi = currentRetrofit?.photoApi
        if (photoApi == null) {
            photoApi = requireNotNull(retrofit.create(PhotoApi::class.java))
            currentRetrofit = currentRetrofit?.copy(photoApi = photoApi)
        }
        return photoApi
    }

    @Provides
    fun provideJournalApi(retrofit: Retrofit): JournalApi {
        var journalApi = currentRetrofit?.journalApi
        if (journalApi == null) {
            journalApi = requireNotNull(retrofit.create(JournalApi::class.java))
            currentRetrofit = currentRetrofit?.copy(journalApi = journalApi)
        }
        return journalApi
    }
}
