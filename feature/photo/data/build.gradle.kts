plugins {
    alias(libs.plugins.monica.module.data)
}

kotlin {
    android {
        namespace = "com.teobaranga.monica.photo.data"
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":core:account"))
                implementation(project(":feature:contact:data"))
            }
        }
    }
}
