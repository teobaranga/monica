plugins {
    alias(libs.plugins.monica.module.data)
}

kotlin {
    android {
        namespace = "com.teobaranga.monica.contact.data"
    }
}

monica {
    optIn {
        experimentalCoroutinesApi = false
        flowPreview = false
    }
}
