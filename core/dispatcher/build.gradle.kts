plugins {
    alias(libs.plugins.monica.kmp)
    
    alias(libs.plugins.monica.metro)
}

kotlin {
    android {
        namespace = "com.teobaranga.monica.core.dispatcher"
    }
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":core:inject"))
                implementation(libs.kotlinx.coroutines.core)
            }
        }
    }
}
