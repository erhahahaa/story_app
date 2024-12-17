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
  implementation(libs.androidx.espresso.contrib)
  implementation(libs.androidx.espresso.intents)
  implementation(libs.androidx.uiautomator)
  testImplementation(libs.junit)
  testImplementation(libs.junit.jupiter)
  androidTestImplementation(libs.androidx.junit)
  androidTestImplementation(libs.androidx.espresso.core)

  //  ViewModel related dependencies
  implementation(libs.androidx.lifecycle.viewmodel.ktx)

  //  UI
  implementation(libs.glide)
  implementation(libs.androidx.swiperefreshlayout)

  //  API related dependencies
  implementation(libs.retrofit)
  implementation(libs.kotlinx.serialization.json)
  implementation(libs.converter.kotlinx.serialization)
  implementation(libs.logging.interceptor)
  implementation(libs.play.services.location)

  //  Storage dependencies
  implementation(libs.androidx.datastore.preferences)
  implementation(libs.androidx.room.runtime)
  implementation(libs.androidx.room.ktx)
  ksp(libs.androidx.room.compiler)

  // Maps dependencies
  implementation(libs.play.services.maps)
  implementation(libs.play.services.location)

  //  Paging dependencies
  implementation(libs.androidx.paging.runtime)
  implementation(libs.androidx.paging.runtime.ktx)
  implementation(libs.androidx.room.paging)

  // Testing dependencies
  implementation(libs.kotlinx.coroutines.test)
  testImplementation(libs.mockito.core)
  testImplementation(libs.mockito.kotlin)
  mockitoAgent("org.mockito:mockito-core:5.14.2") { isTransitive = false }
  testImplementation(libs.androidx.core.testing)
}

tasks.withType<Test> { jvmArgs("-javaagent:${mockitoAgent.asPath}") }
