    plugins {
        id("com.android.application") version libs.versions.agp.get()
        id("com.google.gms.google-services") version libs.versions.googleGmsGoogleServices.get()
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
    implementation(libs.fragmentKtx) // Using fragment-ktx for modern fragments
    implementation(libs.core)
    implementation(libs.androidx.gridlayout)

    // Firebase SDKs
    implementation(platform(libs.firebase.bom)) // BoM ensures consistent Firebase versions
    implementation(libs.firebase.auth)
    implementation(libs.firebase.database)
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.storage)
    implementation(libs.firebase.appcheck.playintegrity)

    // Google Play Services
    implementation(libs.google.maps) // Deduplicate if overlaps with libs.play.services.maps
    implementation(libs.play.services.auth)
    implementation(libs.play.services.auth.api.phone)
    implementation(libs.playServicesLocation)

    // Third-party Libraries
    implementation(libs.ccp) // Country Code Picker library

    // Testing Libraries
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}
