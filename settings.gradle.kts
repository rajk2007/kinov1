dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            from(files("cloudstream/gradle/libs.versions.toml"))
        }
        create("csLibs") {
            from(files("cloudstream/gradle/libs.versions.toml"))
        }
    }
}

pluginManagement {
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
        maven { url = uri("https://jitpack.io") }
    }
}

rootProject.name = "Kino"
include(":app")
include(":cloudstream:app")
