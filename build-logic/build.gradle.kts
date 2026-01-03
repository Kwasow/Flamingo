import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    `kotlin-dsl`
}

group = "pl.kwasow.build"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_17
    }
}

dependencies {
    implementation(libs.delvelin.gradlePlugin)
    implementation(libs.detekt.gradlePlugin)
    implementation(libs.kotlin.gradlePlugin)
    implementation(libs.kotlinx.serialization.gradlePlugin)
    implementation(libs.ktlint.gradlePlugin)

    // Allow using version catalog in convention plugins
    implementation(files(libs.javaClass.superclass.protectionDomain.codeSource.location))
}

gradlePlugin {
    plugins {
        register("kotlinCodeQL") {
            id = libs.plugins.template.kotlin.codeql.get().pluginId
            implementationClass = "CodeQLConventionPlugin"
        }
        register("kotlinLint") {
            id = libs.plugins.template.kotlin.lint.get().pluginId
            implementationClass = "KtlintConventionPlugin"
        }
        register("kotlinxSerialization") {
            id = libs.plugins.template.kotlinx.serialization.get().pluginId
            implementationClass = "KotlinxSerializationConventionPlugin"
        }
    }
}
