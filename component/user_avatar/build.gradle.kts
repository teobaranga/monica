plugins {
    alias(libs.plugins.monica.cmp)
    alias(libs.plugins.monica.android.library)
}

kotlin {
    androidTarget()

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
