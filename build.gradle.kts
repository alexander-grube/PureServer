plugins {
    id("java")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.undertow:undertow-core:2.3.7.Final")
    implementation("io.undertow:undertow-servlet:2.3.7.Final")
    implementation("io.undertow:undertow-websockets-jsr:2.3.7.Final")
    implementation("com.alibaba.fastjson2:fastjson2:2.0.37")
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}