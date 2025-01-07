plugins {
    application
    id("org.springframework.boot") version "3.3.2"
}

application {
    mainClass = "com.github.bgalek.MerlinApplication"
}

dependencies {
    implementation(project(":backend"))
    implementation(project(":frontend"))
}

allprojects {
    apply(plugin = "java")
    java {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(23))
        }
    }
}
