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
    id("org.jetbrains.kotlinx.kover.aggregation") version "0.9.4"
    id("com.android.settings") version "9.0.0"
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

    reports {
        excludedClasses.addAll(
            "amazon.lastmile.inject.*",
            "*ComponentMerged*",
            "*_Impl*",
            "*ViewModelComponent*",
            "*ViewModelFactory*",
            "*ComposableSingletons*",
        )
        excludesAnnotatedBy.addAll(
            "androidx.compose.ui.tooling.preview.Preview",
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
include(":feature:certificate")
include(":feature:configuration")
include(":feature:contact-api")
include(":feature:genders")
include(":feature:journal")
include(":feature:user-api")
include(":androidApp")
