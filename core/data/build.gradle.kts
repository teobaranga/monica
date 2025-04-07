import com.teobaranga.monica.implementation
import com.teobaranga.monica.libs

plugins {
    alias(libs.plugins.monica.jvm.library)
    alias(libs.plugins.kotlinx.serialization)
}

group = "com.teobaranga.monica.core.data"

monica {
    optIn {
        flowPreview = false
    }
}

dependencies {
    implementation(project(":core:dispatcher"))

    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.serialization)

    implementation(libs.paging.common)
}
