plugins {
    alias(libs.plugins.monica.android.library)
    alias(libs.plugins.monica.cmp)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":core:ui"))

                implementation(libs.coil)

                implementation(libs.material.kolor)
            }
        }
    }
}

android {
    namespace = "com.teobaranga.monica.component.user_avatar"
}
