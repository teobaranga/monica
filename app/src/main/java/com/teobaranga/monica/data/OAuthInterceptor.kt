package com.teobaranga.monica.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.r0adkll.kimchi.annotations.ContributesMultibinding
import com.teobaranga.monica.settings.getTokenStorage
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

@SingleIn(AppScope::class)
@me.tatarka.inject.annotations.Inject
@ContributesMultibinding(AppScope::class)
class OAuthInterceptor(
    private val dataStore: DataStore<Preferences>,
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        var request = chain.request()

        val accessToken = runBlocking {
            dataStore.data.first()
                .getTokenStorage()
                .accessToken
        }

        if (accessToken != null) {
            request = request.newBuilder()
                .header("Authorization", "Bearer $accessToken")
                .build()
        }

        return chain.proceed(request)
    }
}
