plugins {
    alias(libs.plugins.monica.cmp)
}

kotlin {
    android {
        namespace = "com.teobaranga.monica.core.ui"

        androidResources {
            enable = true
        }
    }
}
