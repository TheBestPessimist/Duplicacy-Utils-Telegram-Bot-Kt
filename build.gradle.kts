import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion

val javaVersion = JavaVersion.VERSION_23
val kotlinLanguageVersion = "2.1"
val jacksonVersion = "2.18.3"
val logbackVersion = "1.5.18"

val applicationJvmArgs = listOf(
    "-server",
    "-XX:+UseSerialGC",
    "-XX:+UseStringDeduplication",
    "-Xms40m",
    "-Xmx40m"
)


group = "land.tbp"
version = "1.7"
description = "Telegram Bot used for my Duplicacy Utils scripts"
java.sourceCompatibility = javaVersion
java.targetCompatibility = javaVersion



plugins {
    application
    kotlin("jvm") version "2.1.20"
    id("com.google.cloud.tools.jib") version "3.4.5"
    id("io.ktor.plugin") version "3.1.1"
}


repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("ch.qos.logback:logback-classic:$logbackVersion")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:$jacksonVersion")

    // ktor server
    implementation("io.ktor:ktor-server-netty-jvm")
    implementation("io.ktor:ktor-server-host-common")
    implementation("io.ktor:ktor-server-core-jvm")
    implementation("io.ktor:ktor-server-metrics-jvm")
    implementation("io.ktor:ktor-server-call-logging-jvm")
    implementation("io.ktor:ktor-server-content-negotiation")

    // ktor client
    implementation("io.ktor:ktor-client-core")
    implementation("io.ktor:ktor-client-apache")
    implementation("io.ktor:ktor-client-logging")
    implementation("io.ktor:ktor-client-content-negotiation")

    // ktor common
    implementation("io.ktor:ktor-serialization-jackson")

    configurations.all {
        exclude(group = "junit", module = "junit")
        exclude(module = "mockito-core")
        exclude(module = "mockito-all")
        exclude(module = "slf4j-log4j12")
    }
}


tasks {
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        compilerOptions {
            freeCompilerArgs.addAll(listOf("-Xjsr305=strict"))
            jvmTarget.set(JvmTarget.fromTarget(javaVersion.majorVersion))
            languageVersion.set(KotlinVersion.fromVersion(kotlinLanguageVersion))
            apiVersion.set(KotlinVersion.fromVersion(kotlinLanguageVersion))
        }
    }

    withType<Test> {
        useJUnitPlatform()
    }

    wrapper {
        gradleVersion = "8.13"
    }
}

jib {
    from {
        image = "openjdk:23-jdk-slim"
    }
    to {
        image = "docker.io/thebestpessimist/duplicacy-utils-telegram-bot"
    }
    container {
        ports = listOf("13337")
        jvmFlags = applicationJvmArgs
    }
}

application {
    applicationDefaultJvmArgs = applicationJvmArgs
}
