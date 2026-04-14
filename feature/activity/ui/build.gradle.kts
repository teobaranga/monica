plugins {
    alias(libs.plugins.monica.cmp)
    alias(libs.plugins.monica.kotlin.inject)
    alias(libs.plugins.kotlinx.serialization)
}

kotlin {
    android {
        namespace = "com.teobaranga.monica.activity.ui"
    }
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":core:data"))
                implementation(project(":core:dispatcher"))
                implementation(project(":core:ui"))
                implementation(project(":component:user_avatar"))
                implementation(project(":feature:activity:data"))
                implementation(project(":feature:activity:nav"))
                implementation(project(":feature:contact:data"))
                implementation(project(":feature:contact:domain"))
                implementation(project(":feature:contact:nav"))
                implementation(project(":feature:contact-api"))
            }
        }
    }
}
