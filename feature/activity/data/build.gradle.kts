plugins {
    alias(libs.plugins.monica.module.data)
}

kotlin {
    android {
        namespace = "com.teobaranga.monica.activity.data"

        withHostTest {
        }
    }
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":feature:contact:data"))
            }
        }
    }
}
