plugins {
    java
    alias(libs.plugins.error.prone)
}

dependencies {
    implementation(platform(libs.spring.boot.dependencies))
    errorprone(libs.error.prone)
    implementation(libs.spring.boot.starter.webmvc)
    implementation(libs.spring.boot.starter.session.jdbc)
    implementation(libs.spring.boot.starter.jdbc)
    implementation(libs.spring.boot.starter.actuator)
    implementation(libs.sentry.spring.boot.starter)
    implementation(libs.azure.ai.openai)
    implementation(libs.caffeine)
    runtimeOnly(libs.micrometer.registry.prometheus)
    runtimeOnly(libs.postgresql)
    runtimeOnly(libs.hsqldb)
}

tasks.withType<Test> {
    useJUnitPlatform()
}