plugins {
    alias(libs.plugins.monica.cmp)
    alias(libs.plugins.monica.android.library)
}

kotlin {
    androidTarget()

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
