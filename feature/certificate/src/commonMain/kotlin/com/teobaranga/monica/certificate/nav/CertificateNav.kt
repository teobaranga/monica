package com.teobaranga.monica.certificate.nav

import androidx.navigation.NavGraphBuilder
import com.teobaranga.monica.certificate.detail.certificateDetailsScreen
import com.teobaranga.monica.certificate.list.certificateListScreen

fun NavGraphBuilder.certificateListAndDetails() {
    certificateListScreen()
    certificateDetailsScreen()
}
