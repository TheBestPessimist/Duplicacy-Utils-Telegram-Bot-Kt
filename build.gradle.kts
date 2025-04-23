val javaVersion = JavaVersion.VERSION_16
val kotlinLanguageVersion = "1.6"
val ktorVersion = "2.0.0"
val jacksonVersion = "2.13.2"
val logbackVersion = "1.2.11"

val applicationJvmArgs = listOf(
    "-server",
    "-XX:+UseSerialGC",
    "-XX:+UseStringDeduplication",
    "-Xms40m",
    "-Xmx40m",
    "--finalization=disabled" // TODO will be removed when JDK sets finalization to disabled by default.
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
        gradleVersion = "8.7"
    }
}

jib {
    from {
        image = "openjdk:18"
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
