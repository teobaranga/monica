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
include(":core:account")
include(":core:data")
include(":core:dispatcher")
include(":core:inject")
include(":core:ui")
include(":component:user_avatar")
include(":feature:account")
include(":feature:configuration")
include(":feature:contact-api")
include(":feature:genders")
include(":feature:journal")
include(":feature:user-api")
