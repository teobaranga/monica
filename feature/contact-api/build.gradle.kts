plugins {
    alias(libs.plugins.monica.kmp)
}

kotlin {
    androidLibrary {
        namespace = "com.teobaranga.monica.feature.contact"
    }
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":core:datetime"))
                implementation(project(":component:user_avatar"))
            }
        }
    }
}

monica {
    optIn {
        experimentalCoroutinesApi = false
        flowPreview = false
    }
}
