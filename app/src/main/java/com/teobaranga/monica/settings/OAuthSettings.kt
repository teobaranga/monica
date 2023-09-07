package com.teobaranga.monica.settings

import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey

private val SERVER_ADDRESS = stringPreferencesKey("serverAddress")
private val CLIENT_ID = stringPreferencesKey("clientId")
private val CLIENT_SECRET = stringPreferencesKey("clientSecret")
private val AUTHORIZATION_CODE = stringPreferencesKey("authorizationCode")

data class OAuthSettings(
    val serverAddress: String?,
    val clientId: String?,
    val clientSecret: String?,
    val authorizationCode: String?,
)

fun Preferences.getOAuthSettings(): OAuthSettings {
    return OAuthSettings(
        serverAddress = this[SERVER_ADDRESS],
        clientId = this[CLIENT_ID],
        clientSecret = this[CLIENT_SECRET],
        authorizationCode = this[AUTHORIZATION_CODE],
    )
}

class MutableOAuthSettingsScope(private val preferences: MutablePreferences) {

    fun setServerAddress(serverAddress: String) {
        preferences[SERVER_ADDRESS] = serverAddress
    }

    fun setClientId(clientId: String) {
        preferences[CLIENT_ID] = clientId
    }

    fun setClientSecret(clientSecret: String) {
        preferences[CLIENT_SECRET] = clientSecret
    }

    fun setAuthorizationCode(authorizationCode: String) {
        preferences[AUTHORIZATION_CODE] = authorizationCode
    }
}


inline fun MutablePreferences.oAuthSettings(block: MutableOAuthSettingsScope.() -> Unit) {
    val scope = MutableOAuthSettingsScope(this)
    block(scope)
}
