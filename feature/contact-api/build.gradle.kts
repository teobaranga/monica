plugins {
    alias(libs.plugins.monica.android.library)
    alias(libs.plugins.monica.kmp)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":core:datetime"))
                implementation(project(":component:user_avatar"))
            }
        }
    }
}

android {
    namespace = "com.teobaranga.monica.feature.contact"
}

monica {
    optIn {
        experimentalCoroutinesApi = false
        flowPreview = false
    }
}
