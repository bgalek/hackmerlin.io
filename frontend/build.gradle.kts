plugins {
    id("org.siouan.frontend-jdk11") version "6.0.0"
}

frontend {
    nodeVersion.set("20.5.1")
    assembleScript.set("run build")
    cleanScript.set("run clean")
    checkScript.set("run check")
    nodeDistributionProvided.set(true)
}

tasks.build {
    dependsOn("copyDistToBackend")
}

tasks.create("copyDistToBackend", Copy::class) {
    from("./build/dist")
    into("../backend/build/resources/main/static")
}
