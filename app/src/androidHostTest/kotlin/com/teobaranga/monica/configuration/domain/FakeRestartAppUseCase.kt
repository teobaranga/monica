package com.teobaranga.monica.configuration.domain

import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject

@Inject
@ContributesBinding(AppScope::class, replaces = [RestartAppUseCaseImpl::class])
class FakeRestartAppUseCase: RestartAppUseCase {

    override fun invoke() {
        // No-op
    }
}
