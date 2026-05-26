package com.teobaranga.monica.network

import com.teobaranga.monica.certificate.data.CertificateRepository
import com.teobaranga.monica.core.network.HttpRequestMaker
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.DependencyGraph
import dev.zacsweers.metro.SingleIn
import io.ktor.client.engine.mock.MockEngine

@DependencyGraph(AppScope::class)
@SingleIn(AppScope::class)
interface HttpRequestMakerComponent {

    fun httpEngine(): MockEngine.Queue

    fun httpRequestMaker(): HttpRequestMaker

    fun certificateRepository(): CertificateRepository

    fun testSslSettings(): TestSslSettings
}
