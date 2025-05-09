import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import com.google.gms.googleservices.GoogleServicesPlugin

plugins {
    alias(libs.plugins.monica.android.application)
    alias(libs.plugins.monica.android.compose)
    alias(libs.plugins.monica.kotlin.inject)
    alias(libs.plugins.monica.network)
    alias(libs.plugins.ksp)
    alias(libs.plugins.google.services)
    alias(libs.plugins.crashlytics)
    alias(libs.plugins.dependency.analysis)
    alias(libs.plugins.detekt)
}

android {
    namespace = "com.teobaranga.monica"

    defaultConfig {
        applicationId = "com.teobaranga.monica"
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    signingConfigs {
        create("release") {
            val localProperties = gradleLocalProperties(rootDir, providers)
            storeFile = file("release.jks")
            storePassword = localProperties["RELEASE_STORE_PASSWORD"] as String?
            keyAlias = localProperties["RELEASE_KEY_ALIAS"] as String?
            keyPassword = localProperties["RELEASE_KEY_PASSWORD"] as String?
        }
    }

    buildFeatures {
        compose = true
    }

    buildTypes {
        debug {
            applicationIdSuffix = ".debug"
            manifestPlaceholders["crashlyticsCollectionEnabled"] = false
        }
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            signingConfig = signingConfigs.getByName("release")
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            manifestPlaceholders["crashlyticsCollectionEnabled"] = true
        }
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    testOptions.unitTests {
        isIncludeAndroidResources = true
        all { test ->
            test.systemProperties["robolectric.logging.enabled"] = "true"
        }
    }
}

googleServices {
    missingGoogleServicesStrategy = GoogleServicesPlugin.MissingGoogleServicesStrategy.WARN
}

ksp {
    arg("room.schemaLocation", "$projectDir/schemas")
}

dependencies {
    implementation(project(":core:account"))
    implementation(project(":core:data"))
    implementation(project(":core:datetime"))
    implementation(project(":core:dispatcher"))
    implementation(project(":core:inject"))
    implementation(project(":core:ui"))
    implementation(project(":component:user_avatar"))
    implementation(project(":feature:account"))
    implementation(project(":feature:configuration"))
    implementation(project(":feature:contact-api"))
    implementation(project(":feature:genders"))
    implementation(project(":feature:journal"))
    implementation(project(":feature:user-api"))

    implementation(libs.core.ktx)
    implementation(libs.lifecycle.runtime.compose)
    implementation(libs.lifecycle.viewmodel.compose)
    implementation(libs.activity.compose)
    implementation(libs.androidx.compose.material.icons)

    implementation(libs.compose.placeholder)

    implementation(libs.browser)

    implementation(libs.coil)

    implementation(libs.jetbrains.navigation)

    implementation(libs.datastore.preferences)

    implementation(libs.paging.compose)

    implementation(libs.room.runtime)
    ksp(libs.room.compiler)

    implementation(libs.kmlogging)

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.crashlytics)

    implementation(libs.work)

    testImplementation(libs.kotest.runner.junit5)
    testImplementation(libs.kotest.extensions.htmlreporter)
    testImplementation(libs.kotest.extensions.junitxml)
    testImplementation(libs.junit)
    // Robolectric only works with JUnit 4 but the regular unit tests run with JUnit 5
    testImplementation(libs.junit.vintage)
    testImplementation(libs.kotlinx.coroutines.test)

    testImplementation(libs.turbine)

    testImplementation(libs.mockk)

    androidTestImplementation(libs.androidx.test.ext.junit)

    detektPlugins(libs.compose.rules)
    detektPlugins(libs.detekt.formatting)
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
    reports {
        html.required.set(false)
        junitXml.required.set(false)
    }
    systemProperty("gradle.build.dir", project.layout.buildDirectory.asFile.get())
}

detekt {
    autoCorrect = true
}
