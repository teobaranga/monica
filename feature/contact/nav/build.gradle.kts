plugins {
    alias(libs.plugins.monica.kmp)
    alias(libs.plugins.metro)
    alias(libs.plugins.kotlinx.serialization)
}

kotlin {
    android {
        namespace = "com.teobaranga.monica.contact.nav"
    }
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":core:navigation"))
                implementation(libs.navigation3.runtime)
                implementation(libs.kotlinx.serialization)
            }
        }
    }
}
