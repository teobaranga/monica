import com.teobaranga.monica.implementation
import com.teobaranga.monica.libs

plugins {
    alias(libs.plugins.monica.jvm.library)
}

group = "com.teobaranga.monica.core.inject"

dependencies {
    implementation(libs.kotlin.inject.runtime)
    implementation(libs.kotlin.inject.viewmodel.runtime.compose)
}
