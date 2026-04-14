plugins {
    alias(libs.plugins.monica.kmp)
}

kotlin {
    android {
        namespace = "com.teobaranga.monica.feature.contact"
    }
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":core:datetime"))
                implementation(project(":component:user_avatar"))
                implementation(project(":feature:contact:data"))
            }
        }
    }
}
