package com.teobaranga.monica.configuration.domain

import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding

@Inject
@ContributesBinding(AppScope::class)
class RestartAppUseCaseImpl: RestartAppUseCase {

    override fun invoke() {
        // TODO
    }
}
