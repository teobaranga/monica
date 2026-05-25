package com.teobaranga.monica.setup.domain

import com.teobaranga.monica.core.network.NetworkRegex
import dev.zacsweers.metro.Inject
import io.ktor.http.URLProtocol

@Inject
class ValidateServerAddressUseCase {

    operator fun invoke(address: String): Result {
        if (!address.startsWith(URLProtocol.HTTP.name)
            && !address.startsWith(URLProtocol.HTTPS.name)
        ) {
            return Result.InvalidProtocol
        }

        return if (NetworkRegex.WEB_URL.matches(address)) {
            Result.Valid
        } else {
            Result.Invalid
        }
    }

    sealed interface Result {
        object Valid : Result
        object InvalidProtocol : Result
        object Invalid : Result
    }
}
