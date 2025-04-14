import com.teobaranga.monica.libs

plugins {
    alias(libs.plugins.monica.jvm.library)
}

group = "com.teobaranga.monica.core.datetime"

dependencies {
    api(libs.kotlinx.datetime)
}
