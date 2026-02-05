package com.teobaranga.monica.data

import com.teobaranga.monica.contacts.data.ContactApi
import com.teobaranga.monica.core.network.AndroidNetworkComponent
import com.teobaranga.monica.genders.data.GendersApi
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.MockEngineConfig
import io.mockk.mockk
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.serialization.json.Json
import me.tatarka.inject.annotations.Provides
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesTo
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

@ContributesTo(
    scope = AppScope::class,
    replaces = [AndroidNetworkComponent::class],
)
interface TestNetworkComponent {

    @Provides
    @SingleIn(AppScope::class)
    fun provideJson(): Json {
        return Json {
            ignoreUnknownKeys = true
        }
    }

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

    @Provides
    @SingleIn(AppScope::class)
    fun provideContactApi(): ContactApi = mockk<ContactApi>()

    @Provides
    @SingleIn(AppScope::class)
    fun provideGendersApi(): GendersApi = mockk<GendersApi>()
}
