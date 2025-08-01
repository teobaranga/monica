package com.teobaranga.monica.certificate.data

import com.diamondedge.logging.logging
import com.teobaranga.monica.core.inject.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Inject
import okio.ByteString
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn
import kotlin.time.Duration.Companion.seconds

@Inject
@SingleIn(AppScope::class)
class CertificateRepository(
    @param:ApplicationContext
    private val appScope: CoroutineScope,
    private val userTrustedCertificateDataStore: UserTrustedCertificateDataStore,
) {

    private val _untrustedCertificates = MutableStateFlow(emptyList<CommonCertificate>())
    val untrustedCertificates = _untrustedCertificates.asStateFlow()

    private val _untrustedCertificateResults = MutableSharedFlow<UntrustedCertificateResult>()
    val untrustedCertificateResult = _untrustedCertificateResults.asSharedFlow()

    private val userTrustedCertificatesRefreshEvents = MutableSharedFlow<Unit>()
    val userTrustedCertificates = channelFlow {
        send(userTrustedCertificateDataStore.getUserTrustedCertificates())
        launch {
            userTrustedCertificatesRefreshEvents
                .collectLatest {
                    send(userTrustedCertificateDataStore.getUserTrustedCertificates())
                }
        }
    }.stateIn(
        scope = appScope,
        started = SharingStarted.WhileSubscribed(5.seconds.inWholeMilliseconds),
        initialValue = emptyList(),
    )

    val allCertificates
        get() = combine(untrustedCertificates, userTrustedCertificates) { untrusted, userTrusted ->
            untrusted + userTrusted
        }

    fun setUntrustedCertificates(certificates: List<CommonCertificate>) {
        _untrustedCertificates.value = certificates
        log.w { "Untrusted certificates: $certificates" }
    }

    fun refuseUntrustedCertificates() {
        _untrustedCertificates.value = emptyList()
        appScope.launch {
            _untrustedCertificateResults.emit(UntrustedCertificateResult.Refused)
        }
    }

    fun acceptUntrustedCertificates() {
        appScope.launch {
            val certificates = _untrustedCertificates.value
            userTrustedCertificateDataStore.trustCertificates(certificates)
            _untrustedCertificates.value = emptyList()
            _untrustedCertificateResults.emit(UntrustedCertificateResult.Accepted)
        }
    }

    suspend fun deleteUserTrustedCertificate(sha256Hash: ByteString) {
        userTrustedCertificateDataStore.deleteCertificate(sha256Hash)
        userTrustedCertificatesRefreshEvents.emit(Unit)
    }

    companion object {
        private val log = logging()
    }
}
