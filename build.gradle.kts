group = "land.tbp"
version = "1.5"
description = "Telegram Bot used for my Duplicacy Utils scripts"
java.sourceCompatibility = JavaVersion.VERSION_15
java.targetCompatibility = JavaVersion.VERSION_15

val ktor_version = "1.2.6"
val kotlin_version = "1.3.61"
val jackson_datatype_version = "2.9.9"
val logbackVersion = "1.2.3"


plugins {
    kotlin("jvm") version "1.4.21"
    id("com.google.cloud.tools.jib") version "2.7.1"
}


repositories {
    mavenCentral()
    jcenter()
}


dependencies {
    compile("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlin_version")
    compile("io.ktor:ktor-server-netty:$ktor_version")
    compile("ch.qos.logback:logback-classic:$logbackVersion")
    compile("io.ktor:ktor-client-core:$ktor_version")
    compile("io.ktor:ktor-client-core-jvm:$ktor_version")
    compile("io.ktor:ktor-client-json-jvm:$ktor_version")
    compile("io.ktor:ktor-client-jackson:$ktor_version")
    compile("io.ktor:ktor-client-logging-jvm:$ktor_version")
    compile("io.ktor:ktor-server-core:$ktor_version")
    compile("io.ktor:ktor-jackson:$ktor_version")
    compile("io.ktor:ktor-metrics:$ktor_version")
    compile("io.ktor:ktor-server-host-common:$ktor_version")
    compile("io.ktor:ktor-client-apache:$ktor_version")
    compile("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:$jackson_datatype_version")
    testCompile("io.ktor:ktor-server-tests:$ktor_version")
    testCompile("io.ktor:ktor-client-mock:$ktor_version")
    testCompile("io.ktor:ktor-client-mock-jvm:$ktor_version")

    configurations.all {
        exclude(group = "junit", module = "junit")
        exclude(module = "mockito-core")
    }
}

tasks {
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict")
            jvmTarget = JavaVersion.VERSION_15.majorVersion
        }
    }

    withType<Test> {
        useJUnitPlatform()
    }
}
