plugins {
    alias(libs.plugins.monica.kmp)
    alias(libs.plugins.monica.kotlin.inject)
}

kotlin {
    android {
        namespace = "com.teobaranga.monica.activity.domain"
    }
    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.kotlinx.coroutines.core)
                implementation(project(":feature:activity:data"))
                implementation(project(":feature:contact:domain"))
            }
        }
    }
}
