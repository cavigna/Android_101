<h1>Librerias pa el Gradle</h1>


Retrofit

https://github.com/square/retrofit


Gson

https://github.com/google/gson

Navigation

https://developer.android.com/guide/navigation/navigation-getting-started


Lyfecicle / ViewModel /LiveData

https://developer.android.com/jetpack/androidx/releases/lifecycle


Room

https://developer.android.com/jetpack/androidx/releases/room

Coil
https://github.com/coil-kt/coil


```gradle

plugins {
    id 'com.android.application'
    id 'kotlin-android'
    id 'androidx.navigation.safeargs'
}

android {

    
    compileSdk 31

    apply plugin: 'kotlin-kapt'

    buildFeatures{
        viewBinding true
    }

    defaultConfig {
        applicationId "com.example.dogspics"
        minSdk 23
        targetSdk 31
        versionCode 1
        versionName "1.0"

        testInstrumentationRunner "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            minifyEnabled false
            proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
        }
    }
    compileOptions {
        sourceCompatibility JavaVersion.VERSION_1_8
        targetCompatibility JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = '1.8'
    }

    packagingOptions {
        exclude 'META-INF/atomicfu.kotlin_module'
        exclude "META-INF/licenses/**" //testing
        exclude "META-INF/AL2.0" //testing
        exclude "META-INF/LGPL2.1" //testing
    }
}

dependencies {

    implementation 'androidx.core:core-ktx:1.7.0'
    implementation 'androidx.appcompat:appcompat:1.3.1'
    implementation 'com.google.android.material:material:1.4.0'
    implementation 'androidx.constraintlayout:constraintlayout:2.1.1'
    implementation 'androidx.legacy:legacy-support-v4:1.0.0'
    testImplementation 'junit:junit:4.+'
    androidTestImplementation 'androidx.test.ext:junit:1.1.3'
    androidTestImplementation 'androidx.test.espresso:espresso-core:3.4.0'


    //RetroFit
    implementation 'com.squareup.retrofit2:retrofit:2.9.0'

    //Gson
    implementation 'com.google.code.gson:gson:2.8.9'
    implementation 'com.squareup.retrofit2:converter-gson:2.9.0'

    //Nav
    def nav_version = "2.3.5"
    implementation "androidx.navigation:navigation-fragment-ktx:$nav_version"
    implementation "androidx.navigation:navigation-ui-ktx:$nav_version"

    //LifeCycle

    def lifecycle_version = "2.4.0"


    // ViewModel
    implementation "androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycle_version"

    // LiveData
    implementation "androidx.lifecycle:lifecycle-livedata-ktx:$lifecycle_version"
    implementation "androidx.lifecycle:lifecycle-common-java8:$lifecycle_version"

    def room_version = "2.3.0"

    implementation "androidx.room:room-ktx:$room_version"
    implementation "androidx.room:room-runtime:$room_version"
    kapt "androidx.room:room-compiler:$room_version"
    //annotationProcessor "androidx.room:room-compiler:$room_version"


    //Coil

    implementation("io.coil-kt:coil:1.4.0")

    //Coroutines Test
    androidTestImplementation 'org.jetbrains.kotlinx:kotlinx-coroutines-test:1.4.2'

    //test google
    androidTestImplementation "com.google.truth:truth:1.1.3"
    androidTestImplementation "androidx.arch.core:core-testing:2.0.0"

    //MockWebServer
    androidTestImplementation "com.squareup.okhttp3:mockwebserver:4.9.2"
    androidTestImplementation "com.squareup.okhttp3:okhttp:4.9.2"
    debugImplementation 'com.squareup.okhttp3:okhttp:4.9.2'

}


```