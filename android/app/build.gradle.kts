import java.util.Properties

plugins {
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.serialization)

    alias(libs.plugins.android.application)
    alias(libs.plugins.google.mapsplatform)
    alias(libs.plugins.google.services)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ktlint)
}

secrets {
    propertiesFileName = "secrets.properties"
    defaultPropertiesFileName = "secrets.default.properties"
}

android {
    namespace = "pl.kwasow.sunshine"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "pl.kwasow.sunshine"
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

            // App config
            val properties = Properties()
            properties.load(project.rootProject.file("secrets.properties").inputStream())

            buildConfigField(
                "String",
                "BASE_URL",
                properties.getProperty("BASE_URL"),
            )
            buildConfigField(
                "String",
                "GOOGLE_WEB_CLIENT_ID",
                properties.getProperty("GOOGLE_WEB_CLIENT_ID"),
            )
            buildConfigField(
                "String",
                "RELATIONSHIP_START",
                properties.getProperty("RELATIONSHIP_START"),
            )
        }

        debug {
            isMinifyEnabled = false

            versionNameSuffix = "-beta"
            applicationIdSuffix = ".beta"

            // App config
            val properties = Properties()
            properties.load(project.rootProject.file("secrets.properties").inputStream())

            buildConfigField(
                "String",
                "BASE_URL",
                properties.getProperty("DEVELOPMENT_BASE_URL"),
            )
            buildConfigField(
                "String",
                "GOOGLE_WEB_CLIENT_ID",
                properties.getProperty("GOOGLE_WEB_CLIENT_ID"),
            )
            buildConfigField(
                "String",
                "RELATIONSHIP_START",
                properties.getProperty("RELATIONSHIP_START"),
            )
        }
    }

    signingConfigs {
        getByName("debug") {
            storeFile = file("debug.keystore")
            storePassword = "android"
            keyAlias = "androiddebugkey"
            keyPassword = "android"
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.15"
    }

    androidResources {
        generateLocaleConfig = true
    }

    // Specify build flavors
    flavorDimensions += "version"
    productFlavors {
        create("Sunshine") {
            isDefault = true
            dimension = "version"
        }

        create("Karonia") {
            dimension = "version"
        }
    }
}

dependencies {
    // BoM
    implementation(platform(libs.compose.bom))
    implementation(platform(libs.firebase.bom))

    // Firebase and Google
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.messaging)
    implementation(libs.google.services.location)
    implementation(libs.google.services.maps)
    implementation(libs.google.libraries.googleid)
    implementation(libs.google.libraries.maps.compose)

    // Kotlin
    implementation(libs.kotlin.core)
    implementation(libs.kotlin.serialization.json)

    // Ktor
    implementation(libs.ktor.client.android)
    implementation(libs.ktor.client.core)

    // Koin
    implementation(libs.koin.compose.base)
    implementation(libs.koin.compose.navigation)

    // Compose
    implementation(libs.compose.accompanist.permissions)
    implementation(libs.compose.livedata)
    implementation(libs.compose.material)
    implementation(libs.compose.material3)
    implementation(libs.compose.navigation)
    implementation(libs.compose.ui.base)
    implementation(libs.compose.ui.graphics)
    implementation(libs.compose.ui.tooling.preview)

    // Other
    implementation(libs.android.credentials.base)
    implementation(libs.android.credentials.services)
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
