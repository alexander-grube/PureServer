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
    implementation("io.undertow:undertow-core:2.3.18.Final")
    implementation("io.undertow:undertow-servlet:2.3.18.Final")
    implementation("io.undertow:undertow-websockets-jsr:2.3.18.Final")
    implementation("com.alibaba.fastjson2:fastjson2:2.0.54")
    implementation("org.postgresql:postgresql:42.7.5")
    implementation("org.furyio:fury-core:0.4.1")
    implementation("com.zaxxer:HikariCP:6.2.1")
    implementation("io.github.cdimascio:dotenv-java:3.1.0")
}

tasks.test {
    useJUnitPlatform()
}