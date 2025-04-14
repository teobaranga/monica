plugins {
    alias(libs.plugins.monica.android.feature)
}

android {
    namespace = "com.teobaranga.monica.contact"
}

monica {
    optIn {
        experimentalCoroutinesApi = false
        flowPreview = false
    }
}

dependencies {
    implementation(project(":core:datetime"))
    implementation(project(":component:user_avatar"))
}
