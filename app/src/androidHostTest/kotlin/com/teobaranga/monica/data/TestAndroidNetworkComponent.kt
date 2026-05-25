package com.teobaranga.monica.data

import com.teobaranga.monica.core.network.AndroidNetworkComponent
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.MockEngineConfig
import io.mockk.mockk
import kotlinx.coroutines.test.UnconfinedTestDispatcher

@ContributesTo(
    scope = AppScope::class,
    replaces = [AndroidNetworkComponent::class],
)
interface TestAndroidNetworkComponent {

    @Provides
    @SingleIn(AppScope::class)
    fun provideHttpEngine(): MockEngine.Queue {
        return MockEngine.Queue(
            config = MockEngineConfig().apply {
                dispatcher = UnconfinedTestDispatcher()
            }
        )
    }

    @Provides
    @SingleIn(AppScope::class)
    fun provideHttpClient(engine: MockEngine.Queue): HttpClient {
        return HttpClient(engine)
    }

    @Provides
    @SingleIn(AppScope::class)
    fun provideMonicaApi(): MonicaApi = mockk<MonicaApi>()
}
