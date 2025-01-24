pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
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

rootProject.name = "Planner"
include(":app")

include(":core")
include(":core:utils")
include(":core:designsystem")
include(":core:database")
include(":core:ui")

include(":feature")
include(":feature:home")
include(":feature:home:impl")
include(":feature:home:api")
include(":feature:tasklist")
include(":feature:tasklist:api")
include(":feature:tasklist:impl")
