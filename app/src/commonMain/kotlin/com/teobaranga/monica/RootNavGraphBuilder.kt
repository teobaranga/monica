package com.teobaranga.monica

import androidx.navigation.NavGraphBuilder
import com.teobaranga.monica.certificate.detail.certificateDetailsScreen
import com.teobaranga.monica.certificate.list.certificateListScreen
import com.teobaranga.monica.home.home
import com.teobaranga.monica.setup.setup

val RootNavGraphBuilder: NavGraphBuilder.() -> Unit = {
    home()
    setup()
    certificateListScreen()
    certificateDetailsScreen()
}
