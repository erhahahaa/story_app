plugins {
  alias(libs.plugins.android.application)
  alias(libs.plugins.kotlin.android)
  kotlin("plugin.serialization") version "2.0.20"
  id("androidx.navigation.safeargs.kotlin")
  id("kotlin-parcelize")
  id("com.google.devtools.ksp")
}

android {
  namespace = "dev.erhahahaa.storyapp"
  compileSdk = 34

  defaultConfig {
    applicationId = "dev.erhahahaa.storyapp"
    minSdk = 24
    targetSdk = 34
    versionCode = 1
    versionName = "1.0"

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    buildConfigField("String", "API_URL", "\"https://story-api.dicoding.dev/v1/\"")
  }

  buildTypes {
    release {
      isMinifyEnabled = true
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
    }
  }

  buildFeatures {
    viewBinding = true
    buildConfig = true
  }

  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
  }
  kotlinOptions { jvmTarget = "11" }
}

val mockitoAgent = configurations.create("mockitoAgent")

dependencies {
  //  Default dependencies
  implementation(libs.androidx.core.ktx)
  implementation(libs.androidx.appcompat)
  implementation(libs.material)
  implementation(libs.androidx.activity)
  implementation(libs.androidx.constraintlayout)
  implementation(libs.androidx.annotation)
  implementation(libs.androidx.lifecycle.livedata.ktx)
  implementation(libs.androidx.navigation.fragment.ktx)
  implementation(libs.androidx.navigation.ui.ktx)
  testImplementation(libs.junit)
  testImplementation(libs.junit.jupiter)
  androidTestImplementation(libs.androidx.junit)
  androidTestImplementation(libs.androidx.espresso.core)

  //  ViewModel related dependencies
  implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.6")

  //  UI
  implementation("com.github.bumptech.glide:glide:4.16.0")
  implementation(libs.androidx.swiperefreshlayout)

  //  API related dependencies
  implementation("com.squareup.retrofit2:retrofit:2.11.0")
  implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3")
  implementation("com.squareup.retrofit2:converter-kotlinx-serialization:2.11.0")
  implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")
  implementation(libs.play.services.location)

  //  Storage dependencies
  implementation("androidx.datastore:datastore-preferences:1.1.1")
  implementation("androidx.room:room-runtime:2.6.1")
  implementation("androidx.room:room-ktx:2.6.1")
  ksp("androidx.room:room-compiler:2.6.1")

  // Maps dependencies
  implementation("com.google.android.gms:play-services-maps:19.0.0")
  implementation("com.google.android.gms:play-services-location:21.3.0")

  //  Paging dependencies
  implementation("androidx.paging:paging-runtime:3.3.5")
  implementation("androidx.room:room-paging:2.6.1")

  // Testing dependencies
  testImplementation("org.mockito:mockito-core:5.14.2")
  testImplementation("org.mockito.kotlin:mockito-kotlin:5.4.0")
  mockitoAgent("org.mockito:mockito-core:5.14.2") { isTransitive = false }
}

tasks.withType<Test> { jvmArgs("-javaagent:${mockitoAgent.asPath}") }
