plugins {
    id("java")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("com.formdev:flatlaf:3.4.1")
    implementation("com.formdev:flatlaf-intellij-themes:3.4.1")
    implementation("com.formdev:flatlaf-fonts-inter:4.0")
    implementation("org.kordamp.ikonli:ikonli-swing:12.3.1")
    implementation("org.kordamp.ikonli:ikonli-carbonicons-pack:12.3.1")
}

tasks.test {
    useJUnitPlatform()
}