plugins {
    alias(libs.plugins.monica.android.library)
    alias(libs.plugins.monica.cmp)
}

kotlin {

    sourceSets {
        commonMain {
            dependencies {
                api(libs.paging.common)
            }
        }
    }
}

android {
    namespace = "com.teobaranga.monica.core.paging"
}
