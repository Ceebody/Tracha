plugins {
    alias(libs.plugins.android.application) // Android application plugin
    alias(libs.plugins.google.gms.google.services)
    alias(libs.plugins.google.android.libraries.mapsplatform.secrets.gradle.plugin) // Google services plugin d

}

android {
    namespace = "com.example.trachax"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.trachax"
        minSdk = 27
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }



    buildFeatures {
        viewBinding = true // Enable View Binding
    }
}

dependencies {
    // AndroidX and Material Components
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.fragment)
    implementation(libs.core)
    implementation(libs.support.annotations)
    implementation(libs.legacy.support.v4)

    // Firebase SDKs
    implementation(libs.firebase.auth)
    implementation(libs.firebase.database)
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.storage)
    implementation(libs.firebase.appcheck.playintegrity)
    implementation(platform("com.google.firebase:firebase-bom:33.6.0"))

    // Google Play Services
    implementation(libs.play.services.maps)
    implementation(libs.play.services.auth)
    implementation(libs.play.services.auth.api.phone)

    // Third-party Libraries
    implementation(libs.ccp)
    implementation(libs.androidx.gridlayout)

    // Testing Libraries
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation(libs.google.maps)
    implementation(libs.androidx.fragment)
}
