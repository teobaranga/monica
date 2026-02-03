plugins {
    alias(libs.plugins.monica.kmp)
}

kotlin {
    androidLibrary {
        namespace = "com.teobaranga.monica.core.account"
    }
}

monica {
    optIn {
        experimentalCoroutinesApi = false
        flowPreview = false
    }
}
