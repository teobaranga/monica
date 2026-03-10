plugins {
    alias(libs.plugins.monica.module.data)
}

kotlin {
    android {
        namespace = "com.teobaranga.monica.activity.data"
    }
    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.kmlogging)

                implementation(project(":feature:contact:data"))
            }
        }
    }
}
