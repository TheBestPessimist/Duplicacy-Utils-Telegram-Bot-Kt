group = "land.tbp"
version = "1.5"
description = "Telegram Bot used for my Duplicacy Utils scripts"
java.sourceCompatibility = JavaVersion.VERSION_15
java.targetCompatibility = JavaVersion.VERSION_15

val ktorVersion = "1.6.4"
val jacksonVersion = "2.13.0"
val logbackVersion = "1.2.6"


plugins {
    application
    kotlin("jvm") version "1.5.31"
    id("com.google.cloud.tools.jib") version "3.1.4"
}


repositories {
    mavenCentral()
    jcenter()
}


dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("io.ktor:ktor-server-netty:$ktorVersion")
    implementation("ch.qos.logback:logback-classic:$logbackVersion")
    implementation("io.ktor:ktor-client-core:$ktorVersion")
    implementation("io.ktor:ktor-client-core-jvm:$ktorVersion")
    implementation("io.ktor:ktor-client-json-jvm:$ktorVersion")
    implementation("io.ktor:ktor-client-jackson:$ktorVersion")
    implementation("io.ktor:ktor-client-logging-jvm:$ktorVersion")
    implementation("io.ktor:ktor-server-core:$ktorVersion")
    implementation("io.ktor:ktor-jackson:$ktorVersion")
    implementation("io.ktor:ktor-metrics:$ktorVersion")
    implementation("io.ktor:ktor-server-host-common:$ktorVersion")
    implementation("io.ktor:ktor-client-apache:$ktorVersion")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:$jacksonVersion")
    testImplementation("io.ktor:ktor-server-tests:$ktorVersion")
    testImplementation("io.ktor:ktor-client-mock:$ktorVersion")
    testImplementation("io.ktor:ktor-client-mock-jvm:$ktorVersion")

    configurations.all {
        exclude(group = "junit", module = "junit")
        exclude(module = "mockito-core")
    }
}

tasks {
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs += listOf("-Xjsr305=strict")
            jvmTarget = JavaVersion.VERSION_16.majorVersion
        }
    }

    withType<Test> {
        useJUnitPlatform()
    }
}


jib {
    from {
        image = "openjdk:16-alpine"
    }
    to {
        image = "docker.io/thebestpessimist/duplicacy-utils-telegram-bot"
    }
    container {
        this.ports = listOf("13337")
        jvmFlags = listOf(
            "-server",
            "-XX:+UseSerialGC",
            "-XX:+UseStringDeduplication",
            "-Xms40m",
            "-Xmx40m"
        )
    }
}

application {
    applicationDefaultJvmArgs = listOf(
        "-server",
        "-XX:+UseSerialGC",
        "-XX:+UseStringDeduplication",
        "-Xms40m",
        "-Xmx40m"
    )
}
