package com.teobaranga.monica.certificate

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.teobaranga.monica.certificate.nav.certificateListAndDetails
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.IntoSet
import dev.zacsweers.metro.Provides

/**
 * Contributes the certificate destinations to the app's navigation entries.
 */
@ContributesTo(AppScope::class)
@BindingContainer
object CertificateEntryBuilders {

    @Provides
    @IntoSet
    fun provideCertificateEntries(): EntryProviderScope<NavKey>.() -> Unit = {
        certificateListAndDetails()
    }
}
