plugins {
    alias(libs.plugins.monica.android.library)
    alias(libs.plugins.monica.kmp)
}

android {
    namespace = "com.teobaranga.monica.core.account"
}

monica {
    optIn {
        experimentalCoroutinesApi = false
        flowPreview = false
    }
}
