package com.teobaranga.monica.certificate

import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

@SingleIn(AppScope::class)
@Inject
class CertificateRepository {

    var certificates: List<CertificateData> = emptyList()
}
