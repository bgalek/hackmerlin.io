import com.github.gradle.node.npm.task.NpmTask

plugins {
    `java-library`
    id("com.github.node-gradle.node") version "7.1.0"
}

node {
    download = true
    version = "23.5.0"
}

tasks.build {
    dependsOn(tasks.named("npmBuild"))
}

tasks.test {
    dependsOn(tasks.named("npmTest"))
}

tasks.register<NpmTask>("npmBuild") {
    dependsOn(tasks.npmInstall)
    args.set(listOf("run", "build"))
    inputs.dir(project.fileTree("src").exclude("**/*.test.ts"))
    inputs.dir(project.fileTree("public"))
    inputs.files("*.html", "*.json", "*.ts", "*.js")
    outputs.dir(project.layout.buildDirectory.dir("dist"))
    dependsOn(tasks.named("npmTest"))
}

tasks.register<NpmTask>("npmTest") {
    args.set(listOf("run", "test"))
    inputs.dir(project.fileTree("src"))
    inputs.dir(project.fileTree("public"))
    inputs.files("*.html", "*.json", "*.ts", "*.js")
    outputs.upToDateWhen { true }
}

tasks.jar {
    dependsOn(tasks.named("npmBuild"))
    from(project.layout.buildDirectory.dir("dist"))
}