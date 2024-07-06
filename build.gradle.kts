plugins {
    java
    `java-library`
    application
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "org.example"
version = "1.0-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("com.formdev:flatlaf:3.4.1")
    implementation("com.formdev:flatlaf-intellij-themes:3.4.1")
    implementation("com.formdev:flatlaf-fonts-jetbrains-mono:2.304")
    implementation("org.kordamp.ikonli:ikonli-swing:12.3.1")
    implementation("org.kordamp.ikonli:ikonli-carbonicons-pack:12.3.1")
}

tasks.test {
    useJUnitPlatform()
}

application {
    mainClass = "program.Main"
}

tasks {
    val shadowJar by getting(com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar::class) {
        archiveClassifier.set("")
        manifest {
            attributes["Main-Class"] = application.mainClass
        }
    }

    jar {
        enabled = false
    }

    // Ensure tasks that depend on shadowJar are declared
    named("distZip") {
        dependsOn(shadowJar)
    }

    named("distTar") {
        dependsOn(shadowJar)
    }

    named("startScripts") {
        dependsOn(shadowJar)
    }
}
