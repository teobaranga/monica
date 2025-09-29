import com.teobaranga.monica.libs

plugins {
    alias(libs.plugins.monica.android.library)
    alias(libs.plugins.monica.kmp)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.robolectric)
            }
        }
    }
}

android {
    namespace = "com.teobaranga.monica.test"
}
