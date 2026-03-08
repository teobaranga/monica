plugins {
    alias(libs.plugins.monica.kmp)
}

kotlin {
    android {
        namespace = "com.teobaranga.monica.core.account"
    }
}

monica {
    optIn {
        experimentalCoroutinesApi = false
        flowPreview = false
    }
}
