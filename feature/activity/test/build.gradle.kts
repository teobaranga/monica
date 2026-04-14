plugins {
    alias(libs.plugins.monica.module.test.fixture)
}

kotlin {
    android {
        namespace = "com.teobaranga.monica.activity.test"
    }
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":core:data"))
                implementation(project(":feature:contact:data"))
                implementation(project(":feature:activity:data"))
            }
        }
    }
}
