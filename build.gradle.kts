plugins {
    application
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
