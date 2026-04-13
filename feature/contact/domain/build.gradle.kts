plugins {
    alias(libs.plugins.monica.kmp)
    alias(libs.plugins.monica.kotlin.inject)
}

kotlin {
    android {
        namespace = "com.teobaranga.monica.contact.domain"
    }
    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.kotlinx.coroutines.core)
                implementation(project(":feature:contact:data"))
                implementation(project(":feature:genders"))
            }
        }
    }
}
