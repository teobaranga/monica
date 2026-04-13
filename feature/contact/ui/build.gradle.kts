plugins {
    alias(libs.plugins.monica.cmp)
    alias(libs.plugins.monica.kotlin.inject)
}

kotlin {
    android {
        namespace = "com.teobaranga.monica.contact.ui"

        withHostTest {
            isIncludeAndroidResources = true
        }
    }

    sourceSets {
        named("androidHostTest") {
            dependencies {
                implementation(libs.kotlinx.serialization)
                implementation(libs.room.runtime)
                implementation(libs.sandwich)
                implementation(project(":core:data"))
                implementation(project(":feature:activity:test"))
                implementation(project(":feature:contact:test"))
            }
        }
        commonMain {
            dependencies {
                implementation(project(":core:ui"))
                implementation(project(":core:dispatcher"))
                implementation(project(":feature:contact:data"))
                implementation(project(":feature:contact:domain"))
                implementation(project(":feature:contact:nav"))
                implementation(project(":feature:genders"))
                implementation(project(":feature:activity:data"))
                implementation(project(":feature:activity:nav"))
                implementation(project(":component:user_avatar"))

                implementation(libs.compose.placeholder)
            }
        }
    }
}
