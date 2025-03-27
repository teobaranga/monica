plugins {
    alias(libs.plugins.monica.jvm.library)
}

group = "com.teobaranga.monica.core.account"

monica {
    optIn {
        experimentalCoroutinesApi = false
        flowPreview = false
    }
}
