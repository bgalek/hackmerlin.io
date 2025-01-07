plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.7.0"
}

rootProject.name = "merlin"

dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
}

include("frontend")
include("backend")
