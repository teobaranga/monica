plugins {
    alias(libs.plugins.monica.module.data)
}

kotlin {
    android {
        namespace = "com.teobaranga.monica.user.data"
    }
    sourceSets {
        commonMain {
            dependencies {
                api(project(":feature:contact:data"))
            }
        }
    }
}
