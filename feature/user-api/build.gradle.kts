plugins {
    alias(libs.plugins.monica.kmp)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(project(":feature:contact-api"))

                implementation(libs.kotlinx.coroutines.core)
            }
        }
    }
}

monica {
    optIn {
        flowPreview = false
    }
}
