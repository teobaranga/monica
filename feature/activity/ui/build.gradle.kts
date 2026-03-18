plugins {
    alias(libs.plugins.monica.cmp)
    alias(libs.plugins.monica.kotlin.inject)
    alias(libs.plugins.kotlinx.serialization)
}

kotlin {
    android {
        namespace = "com.teobaranga.monica.activity.ui"

        withHostTest {
            isIncludeAndroidResources = true
        }
    }
    sourceSets {
        named("androidHostTest") {
            dependencies {
                implementation(libs.kotlinx.serialization)
                implementation(libs.sandwich)
                implementation(project(":feature:activity:test"))
            }
        }
        commonMain {
            dependencies {
                implementation(libs.compose.placeholder)

                implementation(project(":core:data"))
                implementation(project(":core:dispatcher"))
                implementation(project(":core:ui"))
                implementation(project(":feature:activity:data"))
                implementation(project(":feature:contact:data"))
            }
        }
    }
}
