

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


rootProject.name = "HealthSense"
include(":app")

//pluginManagement {
//    repositories {
//        google {
//            content {
//                includeGroupByRegex("com\\.android.*")
//                includeGroupByRegex("com\\.google.*")
//                includeGroupByRegex("androidx.*")
//            }
//        }
//        mavenCentral()
//        gradlePluginPortal()
//    }
//}
//
//dependencyResolutionManagement {
//    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
//    repositories {
//        google()
//        mavenCentral()
//    }
//    // Ensure the versionCatalogs block is INSIDE dependencyResolutionManagement
//    // and uncommented if you are using libs.versions.toml
//    versionCatalogs {
//        create("libs") {
//            from(files("gradle/libs.versions.toml"))
//        }
//    }
//}
//
//// Enable this if you want type-safe project accessors for multi-module projects.
//// It's good practice but not strictly required for a single-module project to build.
//// enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
//
//rootProject.name = "HealthSense"
//include(":app")