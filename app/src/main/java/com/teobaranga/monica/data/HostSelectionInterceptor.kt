package com.teobaranga.monica.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.teobaranga.monica.settings.getOAuthSettings
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.launch
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.Interceptor
import okhttp3.Response
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@OptIn(ExperimentalCoroutinesApi::class)
@Singleton
class HostSelectionInterceptor @Inject constructor(
    dataStore: DataStore<Preferences>,
) : Interceptor {

    @Volatile
    private lateinit var host: HttpUrl

    init {
        GlobalScope.launch {
            dataStore.data
                .mapLatest { preferences ->
                    preferences.getOAuthSettings().serverAddress
                }
                .filterNotNull()
                .collectLatest { serverAddress ->
                    try {
                        host = serverAddress.toHttpUrl()
                    } catch (e: IllegalArgumentException) {
                        Timber.e(e, "Unable to parse server address: %s", serverAddress)
                    }
                }
        }
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        val newUrl = originalRequest.url.newBuilder()
            .scheme(host.scheme)
            .host(host.host)
            .port(host.port)
            .encodedPath(host.encodedPath)
            .apply {
                originalRequest.url.encodedPathSegments
                    .forEach { encodedPathSegment ->
                        addEncodedPathSegment(encodedPathSegment)
                    }
            }
            .build()

        val newRequest = originalRequest.newBuilder()
            .url(newUrl)
            .build()

        return chain.proceed(newRequest)
    }
}
