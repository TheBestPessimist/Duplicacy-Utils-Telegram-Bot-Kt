val javaVersion = JavaVersion.VERSION_24
val kotlinLanguageVersion = "2.1" // Updated Kotlin language version
val ktorVersion = "3.1.2" // Updated Ktor version
val jacksonVersion = "2.18.3" // Updated Jackson version
val logbackVersion = "1.5.18" // Updated Logback version

val applicationJvmArgs = listOf(
    "-server",
    "-XX:+UseSerialGC",
    "-XX:+UseStringDeduplication",
    "-Xms40m",
    "-Xmx40m"
    // Finalization is disabled by default since Java 18
)


group = "land.tbp"
version = "1.6"
description = "Telegram Bot used for my Duplicacy Utils scripts"
java.sourceCompatibility = javaVersion
java.targetCompatibility = javaVersion



plugins {
    application
    kotlin("jvm") version "1.6.20"
    id("com.google.cloud.tools.jib") version "3.2.0"
}


repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("ch.qos.logback:logback-classic:$logbackVersion")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:$jacksonVersion")

    // ktor server
    implementation("io.ktor:ktor-server-netty-jvm:$ktorVersion")
    implementation("io.ktor:ktor-server-host-common:$ktorVersion")
    implementation("io.ktor:ktor-server-core-jvm:$ktorVersion")
    implementation("io.ktor:ktor-server-metrics-jvm:$ktorVersion")
    implementation("io.ktor:ktor-server-call-logging-jvm:$ktorVersion")
    implementation("io.ktor:ktor-server-content-negotiation:$ktorVersion")

    // ktor client
    implementation("io.ktor:ktor-client-core:$ktorVersion")
    implementation("io.ktor:ktor-client-apache:$ktorVersion")
    implementation("io.ktor:ktor-client-logging:$ktorVersion")
    implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")

    // ktor common
    implementation("io.ktor:ktor-serialization-jackson:$ktorVersion")



    testImplementation("io.ktor:ktor-server-tests-jvm:$ktorVersion")

    configurations.all {
        exclude(group = "junit", module = "junit")
        exclude(module = "mockito-core")
        exclude(module = "mockito-all")
        exclude(module = "slf4j-log4j12")
    }
}


tasks {
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions {
            @Suppress("SuspiciousCollectionReassignment")
            freeCompilerArgs += listOf("-Xjsr305=strict")
            jvmTarget = javaVersion.majorVersion
            languageVersion = kotlinLanguageVersion
            apiVersion = kotlinLanguageVersion
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
        image = "openjdk:24-jdk-slim" // Use a Java 24 base image
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
