package com.teobaranga.monica.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.teobaranga.monica.core.dispatcher.Dispatcher
import com.teobaranga.monica.inject.runtime.ApplicationContext
import com.teobaranga.monica.settings.getOAuthSettings
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Inject
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.Interceptor
import okhttp3.Response
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn
import timber.log.Timber

@Inject
@SingleIn(AppScope::class)
@ContributesBinding(AppScope::class, multibinding = true)
class HostSelectionInterceptor(
    @ApplicationContext
    appCoroutineScope: CoroutineScope,
    dispatcher: Dispatcher,
    dataStore: DataStore<Preferences>,
) : Interceptor {

    @Volatile
    private lateinit var host: HttpUrl

    init {
        appCoroutineScope.launch(dispatcher.io) {
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
