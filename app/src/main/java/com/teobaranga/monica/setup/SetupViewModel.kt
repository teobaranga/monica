package com.teobaranga.monica.setup

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.SavedStateHandleSaveableApi
import androidx.lifecycle.viewmodel.compose.saveable
import com.teobaranga.monica.data.PARAM_CLIENT_ID
import com.teobaranga.monica.data.PARAM_REDIRECT_URI
import com.teobaranga.monica.data.PARAM_RESPONSE_TYPE
import com.teobaranga.monica.data.REDIRECT_URI
import com.teobaranga.monica.di.IoDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import javax.inject.Inject

@OptIn(SavedStateHandleSaveableApi::class)
@HiltViewModel
class SetupViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    @IoDispatcher
    private val ioDispatcher: CoroutineDispatcher,
) : ViewModel() {

    val uiState by savedStateHandle.saveable(saver = UiState.Saver) {
        UiState()
    }

    private val _setupUri = MutableSharedFlow<String>()
    val setupUri: SharedFlow<String> = _setupUri

    fun onSignIn() {
        viewModelScope.launch(ioDispatcher) {
            val baseUrl = "${uiState.serverAddress.text}/oauth/authorize".toHttpUrlOrNull() ?: return@launch
            val url = baseUrl
                .newBuilder()
                .addQueryParameter(PARAM_CLIENT_ID, uiState.clientId.text)
                .addQueryParameter(PARAM_RESPONSE_TYPE, "code")
                .addQueryParameter(PARAM_REDIRECT_URI, REDIRECT_URI)
                .build()
                .toString()
            _setupUri.emit(url)
        }
    }
}
