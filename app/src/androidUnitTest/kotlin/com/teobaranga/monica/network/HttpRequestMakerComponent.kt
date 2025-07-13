package com.teobaranga.monica.network

import com.teobaranga.monica.certificate.data.CertificateRepository
import com.teobaranga.monica.core.network.HttpRequestMaker
import io.ktor.client.engine.mock.MockEngine
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.MergeComponent
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

@MergeComponent(AppScope::class)
@SingleIn(AppScope::class)
interface HttpRequestMakerComponent {

    fun httpEngine(): MockEngine.Queue

    fun httpRequestMaker(): HttpRequestMaker

    fun certificateRepository(): CertificateRepository
}
