import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import com.google.firebase.crashlytics.buildtools.gradle.CrashlyticsExtension
import com.google.gms.googleservices.GoogleServicesPlugin
import com.teobaranga.monica.InjectHandler
import com.teobaranga.monica.libs

plugins {
    alias(libs.plugins.monica.android.application)
    alias(libs.plugins.monica.cmp)
    alias(libs.plugins.monica.kotlin.inject)
    alias(libs.plugins.monica.network)
    alias(libs.plugins.google.services)
    alias(libs.plugins.crashlytics)
    alias(libs.plugins.dependency.analysis)
    alias(libs.plugins.detekt)
    alias(libs.plugins.room)
}

kotlin {
    applyDefaultHierarchyTemplate()

    androidTarget()

    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "MonicaApp"
            // Required when using NativeSQLiteDriver
            linkerOpts.add("-lsqlite3")
        }
    }

    sourceSets {
        androidMain {
            dependencies {
                implementation(libs.work)

                implementation(libs.browser)

                implementation(project.dependencies.platform(libs.firebase.bom))
                implementation(libs.firebase.crashlytics)
            }
        }
        androidUnitTest {
            // TODO run tests in common source set
            dependencies {
                implementation(libs.kotest.runner.junit5)
                implementation(libs.kotest.extensions.htmlreporter)
                implementation(libs.kotest.extensions.junitxml)
                implementation(libs.junit)
                // Robolectric only works with JUnit 4 but the regular unit tests run with JUnit 5
                implementation(libs.junit.vintage)
                implementation(libs.kotlinx.coroutines.test)

                implementation(libs.turbine)

                implementation(libs.mockk)
            }
        }
        commonMain {
            dependencies {
                implementation(project(":core:account"))
                implementation(project(":core:data"))
                implementation(project(":core:datetime"))
                implementation(project(":core:dispatcher"))
                implementation(project(":core:inject"))
                implementation(project(":core:paging"))
                implementation(project(":core:network"))
                implementation(project(":core:ui"))
                implementation(project(":component:user_avatar"))
                implementation(project(":component:tips"))
                implementation(project(":feature:account"))
                implementation(project(":feature:certificate"))
                implementation(project(":feature:configuration"))
                implementation(project(":feature:contact-api"))
                implementation(project(":feature:genders"))
                implementation(project(":feature:journal"))
                implementation(project(":feature:user-api"))

                implementation(libs.compose.placeholder)

                implementation(libs.coil)

                implementation(libs.jetbrains.navigation)

                implementation(libs.datastore.preferences)

                implementation(libs.room.runtime)
                implementation(libs.sqlite.bundled)

                implementation(libs.kmlogging)

                implementation(libs.signum.indispensable)
            }
        }
        commonTest {
            dependencies {
                implementation(project(":core:test"))
            }
        }
    }
}

dependencies {
    add("kspAndroid", libs.room.compiler)
    add("kspIosArm64", libs.room.compiler)
    add("kspIosSimulatorArm64", libs.room.compiler)

    detektPlugins(libs.compose.rules)
    detektPlugins(libs.detekt.formatting)
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
            configure<CrashlyticsExtension> {
                val isMappingFileUploadEnabled =
                    project.properties["isMappingFileUploadEnabled"]?.toString()?.toBoolean() ?: false
                mappingFileUploadEnabled = isMappingFileUploadEnabled
            }
        }
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            excludes += "META-INF/versions/9/OSGI-INF/MANIFEST.MF"
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

room {
    schemaDirectory("$projectDir/schemas")
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

monica {
    inject {
        injectIn.set(InjectHandler.Target.SEPARATE)
    }
}
