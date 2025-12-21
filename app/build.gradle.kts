import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.serialization)

    alias(libs.plugins.android.application)
    alias(libs.plugins.firebase.crashlytics)
    alias(libs.plugins.google.mapsplatform)
    alias(libs.plugins.google.protobuf)
    alias(libs.plugins.google.services)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ktlint)
}

secrets {
    propertiesFileName = "secrets.properties"
    defaultPropertiesFileName = "secrets.default.properties"
}

android {
    namespace = "pl.kwasow"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "pl.kwasow"
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        versionCode = libs.versions.versionCode.get().toInt()
        versionName = libs.versions.versionName.get()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )

            buildConfigField("String", "BASE_URL", "\"https://flamingo.kwasow.pl\"")
        }

        debug {
            isMinifyEnabled = false

            versionNameSuffix = "-beta"
            applicationIdSuffix = ".beta"

            buildConfigField("String", "BASE_URL", "\"https://flamingo-test.kwasow.pl\"")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlin {
        compilerOptions {
            jvmTarget = JvmTarget.JVM_1_8
        }
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.15"
    }

    // Specify build flavors
    flavorDimensions += "version"
    productFlavors {
        create("Flamingo") {
            isDefault = true
            dimension = "version"

            applicationIdSuffix = ".flamingo"
        }

        create("Karonia") {
            dimension = "version"

            applicationIdSuffix = ".sunshine"
        }
    }
}

dependencies {
    // Internal
    implementation(project(":types"))

    // BoM
    implementation(platform(libs.compose.bom))
    implementation(platform(libs.firebase.bom))
    implementation(platform(libs.koin.bom))

    // Firebase and Google
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.crashlytics)
    implementation(libs.firebase.messaging)
    implementation(libs.google.services.location)
    implementation(libs.google.services.maps)
    implementation(libs.google.libraries.googleid)
    implementation(libs.google.libraries.maps.compose)
    implementation(libs.google.libraries.protobuf.kotlin)

    // Kotlin
    implementation(libs.kotlin.core)
    implementation(libs.kotlin.serialization.json)

    // Ktor
    implementation(libs.ktor.client.android)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.serialization.kotlinx.json)

    // Koin
    implementation(libs.koin.android)
    implementation(libs.koin.androidx.compose)
    implementation(libs.koin.androidx.compose.navigation)
    implementation(libs.koin.core)

    // Compose
    implementation(libs.compose.accompanist.permissions)
    implementation(libs.compose.livedata)
    implementation(libs.compose.material.icons.core)
    implementation(libs.compose.material.icons.extended)
    implementation(libs.compose.material3)
    implementation(libs.compose.navigation)
    implementation(libs.compose.ui.base)
    implementation(libs.compose.ui.graphics)
    implementation(libs.compose.ui.tooling.preview)

    // Other
    implementation(libs.android.credentials.base)
    implementation(libs.android.credentials.services)
    implementation(libs.android.dataStore)
    implementation(libs.android.lifecycle)
    implementation(libs.android.media.common)
    implementation(libs.android.media.exoplayer)
    implementation(libs.android.media.session)
    implementation(libs.coil)
    implementation(libs.haze.base)
    implementation(libs.haze.materials)

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.android.test.espresso)
    androidTestImplementation(libs.android.test.junit)
    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation(libs.compose.ui.test.junit4)
    debugImplementation(libs.compose.ui.tooling.base)
    debugImplementation(libs.compose.ui.test.manifest)
}

protobuf {
    protoc {
        artifact = libs.google.libraries.protobuf.compiler.get().toString()
    }

    generateProtoTasks {
        all().forEach { task ->
            task.builtins {
                create("java") {
                    option("lite")
                }
                create("kotlin") {
                    option("lite")
                }
            }
        }
    }
}
