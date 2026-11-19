plugins {
    alias(libs.plugins.monica.cmp)
    alias(libs.plugins.monica.metro)
    alias(libs.plugins.kotlinx.serialization)
}

kotlin {
    android {
        namespace = "com.teobaranga.monica.certificate"
    }
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":core:inject"))
                implementation(project(":core:network"))
                implementation(project(":core:navigation"))
                implementation(project(":core:ui"))
                implementation(libs.navigation3.runtime)
                implementation(libs.kotlinx.serialization)
                implementation(libs.okio)
                implementation(libs.signum.indispensable)
            }
        }
    }
}
