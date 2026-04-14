pluginManagement {
    includeBuild("build-logic")
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

plugins {
    id("org.jetbrains.kotlinx.kover.aggregation") version "0.9.8"
    id("com.android.settings") version "9.1.1"
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

android {
    execution {
        profiles {
            register("ci") {
                r8 {
                    runInSeparateProcess = true
                    jvmOptions += listOf(
                        "-Xms3g",
                        "-Xmx6g",
                        "-XX:MaxMetaspaceSize=2g",
                        "-XX:+HeapDumpOnOutOfMemoryError",
                        "-XX:+UseParallelGC",
                        "-XX:SoftRefLRUPolicyMSPerMB=1",
                    )
                }
            }
        }
        defaultProfile = null
    }
}


kover {
    enableCoverage()

    // Skip intermediary group projects
    skipProjects(
        ":",
        ":component",
        ":core",
        ":feature",
        ":feature:activity",
        ":feature:contact",
    )

    reports {
        excludedClasses.addAll(
            "amazon.lastmile.inject.*",
            "*ComponentMerged*",
            "*_Impl*",
            "*ViewModelComponent*",
            "*ViewModelFactory*",
            "*ComposableSingletons*",
            // Compose generated resources
            "*.generated.resources.*",
            // Icons (ImageVector)
            // TODO: drop once screenshot tests are added
            "*.icon.*",
            // Test fixtures
            "com.teobaranga.monica.core.test.*",
        )
        excludesAnnotatedBy.addAll(
            "androidx.compose.ui.tooling.preview.Preview",
            // DAOs are faked in tests, avoid including them in coverage
            // TODO: remove this once Robolectric Dao tests are included
            "androidx.room.Dao",
            "androidx.room.Database",
            // DI wiring
            "software.amazon.lastmile.kotlin.inject.anvil.ContributesTo",
            "software.amazon.lastmile.kotlin.inject.anvil.MergeComponent",
            "me.tatarka.inject.annotations.Provides",
        )
    }
}

rootProject.name = "Monica"
include(":app")
include(":core:account")
include(":core:data")
include(":core:datetime")
include(":core:dispatcher")
include(":core:inject")
include(":core:network")
include(":core:test")
include(":core:ui")
include(":component:tips")
include(":component:user_avatar")
include(":feature:account")
include(":feature:activity:data")
include(":feature:activity:domain")
include(":feature:activity:ui")
include(":feature:activity:test")
include(":feature:activity:nav")
include(":feature:certificate")
include(":feature:configuration")
include(":feature:contact:data")
include(":feature:contact:domain")
include(":feature:contact:ui")
include(":feature:contact:nav")
include(":feature:contact:test")
include(":feature:contact-api")
include(":feature:genders")
include(":feature:journal")
include(":feature:photo:data")
include(":feature:user-api")
include(":androidApp")
