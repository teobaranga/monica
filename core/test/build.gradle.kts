import com.teobaranga.monica.libs

plugins {
    alias(libs.plugins.monica.kmp)
}

kotlin {
    androidLibrary {
        namespace = "com.teobaranga.monica.test"
    }
    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.robolectric)
            }
        }
    }
}
