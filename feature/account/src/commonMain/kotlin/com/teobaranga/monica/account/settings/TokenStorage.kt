package com.teobaranga.monica.account.settings

import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey

private val AUTHORIZATION_CODE = stringPreferencesKey("authorizationCode")
private val ACCESS_TOKEN = stringPreferencesKey("accessToken")
private val REFRESH_TOKEN = stringPreferencesKey("refreshToken")

data class TokenStorage(
    val authorizationCode: String?,
    val accessToken: String?,
    val refreshToken: String?,
)

fun Preferences.getTokenStorage(): TokenStorage {
    return TokenStorage(
        authorizationCode = this[AUTHORIZATION_CODE],
        accessToken = this[ACCESS_TOKEN],
        refreshToken = this[REFRESH_TOKEN],
    )
}

class MutableTokenStorageScope(private val preferences: MutablePreferences) {

    fun setAuthorizationCode(authorizationCode: String) {
        preferences[AUTHORIZATION_CODE] = authorizationCode
    }

    fun setAccessToken(accessToken: String) {
        preferences[ACCESS_TOKEN] = accessToken
    }

    fun setRefreshToken(refreshToken: String) {
        preferences[REFRESH_TOKEN] = refreshToken
    }

    fun clear() {
        preferences.remove(AUTHORIZATION_CODE)
        preferences.remove(ACCESS_TOKEN)
        preferences.remove(REFRESH_TOKEN)
    }
}

inline fun MutablePreferences.tokenStorage(block: MutableTokenStorageScope.() -> Unit) {
    val scope = MutableTokenStorageScope(this)
    block(scope)
}
