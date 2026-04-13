plugins {
    alias(libs.plugins.monica.module.data)
}

kotlin {
    android {
        namespace = "com.teobaranga.monica.contact.data"
    }
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":core:account"))
                implementation(project(":feature:genders"))
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
