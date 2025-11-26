import org.jlleitschuh.gradle.ktlint.KtlintExtension

plugins {
    war

    alias(libs.plugins.kotlin.jpa)
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kotlin.spring)

    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.manager)

    alias(libs.plugins.ktlint)
}

group = "pl.kwasow.flamingo"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

dependencies {
    // Internal
    implementation(project(":types"))

    // Spring framework
    implementation(libs.jackson.module.kotlin)
    implementation(libs.kotlin.reflect)
    implementation(libs.spring.boot.starter.data.jpa)
    implementation(libs.spring.boot.starter.security)
    implementation(libs.spring.boot.starter.web)

    runtimeOnly(libs.mariadb.java.client)
    providedRuntime(libs.spring.boot.starter.tomcat)

    // Google and Firebase
    implementation(libs.firebase.admin)

    // Testing
    testImplementation(libs.kotlin.serialization.json)
    testImplementation(libs.mockito)
    testImplementation(libs.mockito.junit)
    testImplementation(libs.testcontainers)
    testImplementation(libs.testcontainers.junit)
    testImplementation(libs.testcontainers.mariadb)

    testImplementation(libs.spring.boot.starter.test)
    testImplementation(libs.spring.boot.testcontainers)
    testImplementation(libs.spring.security.test)
    testImplementation(libs.kotlin.test.junit5)
    testRuntimeOnly(libs.junit.platform.launcher)
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

allOpen {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.MappedSuperclass")
    annotation("jakarta.persistence.Embeddable")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

configure<KtlintExtension> {
    version.set("1.5.0")
}
