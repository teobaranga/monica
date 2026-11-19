package com.teobaranga.monica.certificate.nav

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.teobaranga.monica.certificate.detail.certificateDetailsScreen
import com.teobaranga.monica.certificate.list.certificateListScreen

fun EntryProviderScope<NavKey>.certificateListAndDetails() {
    certificateListScreen()
    certificateDetailsScreen()
}
