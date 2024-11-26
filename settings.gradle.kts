pluginManagement {
    repositories {
        google()  // Include Google's repository for Android-related plugins
        mavenCentral()  // Use Maven Central for other dependencies
        gradlePluginPortal()  // Use Gradle Plugin Portal for Gradle-specific plugins
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)  // Fail if projects define repositories
    repositories {
        google()  // Google repository for Android libraries
        mavenCentral()  // Maven Central for other libraries
    }

    // Enable version catalogs
    versionCatalogs {
        create("libs") {
               // Correct path to the version catalog file
        }
    }
}

rootProject.name = "Tracha X"  // Root project name

include(":app")  // Include the 'app' module
