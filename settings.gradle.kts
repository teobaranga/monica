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

rootProject.name = "Monica"
include(":app")
include(":core:data")
include(":core:dispatcher")
include(":core:inject")
include(":core:ui")
include(":feature:configuration")
include(":feature:genders")
