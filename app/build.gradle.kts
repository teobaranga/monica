import com.teobaranga.monica.InjectHandler
import com.teobaranga.monica.libs

plugins {
    alias(libs.plugins.monica.cmp)
    alias(libs.plugins.monica.kotlin.inject)
    alias(libs.plugins.monica.network)
    alias(libs.plugins.dependency.analysis)
    alias(libs.plugins.detekt)
    alias(libs.plugins.room)
    alias(libs.plugins.sentry.kmp)
    alias(libs.plugins.kotest)
}

kotlin {
    applyDefaultHierarchyTemplate()

    androidLibrary {
        namespace = "com.teobaranga.monica.app"

        withHostTest {
            isIncludeAndroidResources = true
        }
    }

    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "MonicaApp"
            isStatic = true
            // Required when using NativeSQLiteDriver
            linkerOpts.add("-lsqlite3")
        }
    }

    sourceSets {
        androidMain {
            dependencies {
                implementation(libs.work)

                implementation(libs.browser)
            }
        }
        getByName("androidHostTest") {
            // TODO run tests in common source set
            dependencies {
                implementation(libs.kotest.runner.junit5)
                implementation(libs.kotest.extensions.htmlreporter)
                implementation(libs.kotest.extensions.junitxml)
                // Robolectric only works with JUnit 4 but the regular unit tests run with JUnit 5
                implementation(libs.junit.vintage)

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

                implementation(libs.paging.compose)

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
                implementation(libs.kotest.assertions.core)
                implementation(libs.kotest.framework.engine)

                implementation(libs.junit)
                implementation(libs.kotlinx.coroutines.test)

                implementation(libs.turbine)
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

room {
    schemaDirectory("$projectDir/schemas")
}

detekt {
    autoCorrect = true
}

monica {
    inject {
        injectIn = InjectHandler.Target.SEPARATE
    }
}
