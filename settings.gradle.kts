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
    id("org.jetbrains.kotlinx.kover.aggregation") version "0.9.1"
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
include(":core:paging")
include(":core:ui")
include(":component:user_avatar")
include(":feature:account")
include(":feature:configuration")
include(":feature:contact-api")
include(":feature:genders")
include(":feature:journal")
include(":feature:user-api")
