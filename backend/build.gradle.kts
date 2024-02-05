plugins {
    java
    application
    id("org.springframework.boot") version "3.1.1"
    id("io.spring.dependency-management") version "1.1.0"
}

group = "com.github.bgalek"
version = "1.0.0"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(20))
    }
}

repositories {
    mavenCentral()
}

dependencies {
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.session:spring-session-jdbc")
    implementation("org.springframework.boot:spring-boot-starter-jdbc")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("io.github.resilience4j:resilience4j-retry:2.2.0")
    implementation("com.theokanning.openai-gpt3-java:service:0.18.2")
    runtimeOnly("io.micrometer:micrometer-registry-prometheus:1.11.1")
    runtimeOnly("org.postgresql:postgresql")
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.processResources {
    dependsOn(":frontend:build")
}

tasks.jar {
    enabled = false
}

tasks.distZip {
    enabled = false;
}

tasks.distTar {
    enabled = false;
}
