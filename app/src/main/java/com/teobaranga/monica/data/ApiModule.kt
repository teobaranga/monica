package com.teobaranga.monica.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.Rfc3339DateJsonAdapter
import com.teobaranga.monica.data.user.UserApi
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
import java.util.Date

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
    )

    @Provides
    fun provideRetrofit(
        interceptors: Set<@JvmSuppressWildcards Interceptor>,
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
        val moshi = Moshi.Builder()
            .add(Date::class.java, Rfc3339DateJsonAdapter())
            .build()
        val retrofit = Retrofit.Builder()
            .client(client)
            .baseUrl(baseUrl)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
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
}
