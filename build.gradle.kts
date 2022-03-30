val javaVersion = JavaVersion.VERSION_16
val kotlinLanguageVersion = "1.6"
val ktorVersion = "1.6.8"
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
    kotlin("jvm") version "1.6.10"
    id("com.google.cloud.tools.jib") version "3.2.0"
}


repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("ch.qos.logback:logback-classic:$logbackVersion")
    implementation("io.ktor:ktor-server-netty:$ktorVersion")
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
        exclude(module = "mockito-all")
        exclude(module = "slf4j-log4j12")
    }
}


tasks {
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions {
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
        gradleVersion = "7.4.1"
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
