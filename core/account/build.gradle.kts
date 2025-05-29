plugins {
    alias(libs.plugins.monica.kmp)
}

group = "com.teobaranga.monica.core.account"

monica {
    optIn {
        experimentalCoroutinesApi = false
        flowPreview = false
    }
}
