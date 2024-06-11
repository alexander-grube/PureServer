plugins {
    id("java")
    application
}

application {
    mainClass = "org.example.Main"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.undertow:undertow-core:2.3.13.Final")
    implementation("io.undertow:undertow-servlet:2.3.13.Final")
    implementation("io.undertow:undertow-websockets-jsr:2.3.13.Final")
    implementation("com.alibaba.fastjson2:fastjson2:2.0.51")
    implementation("org.postgresql:postgresql:42.7.3")
    implementation("org.furyio:fury-core:0.4.1")
    implementation("com.zaxxer:HikariCP:5.1.0")
    implementation("io.github.cdimascio:java-dotenv:5.2.2")
    testImplementation(platform("org.junit:junit-bom:5.11.0-M1"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.11.0-M1")
    testImplementation("org.testcontainers:testcontainers:1.19.8")
}

tasks.test {
    useJUnitPlatform()
}