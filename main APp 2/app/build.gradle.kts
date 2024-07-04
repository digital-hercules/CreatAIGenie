plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-parcelize")
    id("com.google.gms.google-services")

}

android {
    namespace = "com.ai.genie"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.ai.genie"
        minSdk = 24
        targetSdk = 34
        versionCode = 8
        versionName = "1.2.5"
        multiDexEnabled = true

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
    buildFeatures{
        viewBinding = true
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    packagingOptions {
        resources {
            excludes.add("META-INF/DEPENDENCIES")
        }
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.google.firebase:firebase-storage-ktx:20.3.0")
    implementation("androidx.activity:activity:1.8.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("com.github.bumptech.glide:glide:4.16.0")
    annotationProcessor("com.github.bumptech.glide:compiler:4.16.0")
    implementation("com.squareup.okhttp3:okhttp:4.11.0")
    implementation(platform("com.google.firebase:firebase-bom:32.4.0"))
    implementation("com.google.firebase:firebase-database")
    implementation("com.google.firebase:firebase-analytics-ktx")
    implementation("com.google.firebase:firebase-auth-ktx")
    implementation("com.google.firebase:firebase-messaging-ktx")
    implementation("com.google.firebase:firebase-iid:21.1.0")

    /*  implementation( "com.google.firebase:firebase-ml-vision")
      implementation( "com.google.firebase:firebase-ml-vision-image-label-model")
  */
    implementation("androidx.work:work-runtime:2.9.0")
    implementation("com.orhanobut:logger:2.2.0")
    implementation("com.airbnb.android:lottie:3.6.1")
    implementation("com.github.bumptech.glide:glide:4.14.2")
    implementation("com.google.firebase:firebase-ml-vision:24.0.3")
    implementation("com.google.firebase:firebase-ml-vision-image-label-model:20.0.1")
    implementation("com.airbnb.android:lottie:3.4.0")
    implementation( "com.amazonaws:aws-android-sdk-polly:2.16.+")
    implementation( "com.amazonaws:aws-android-sdk-mobile-client:2.16.+")
    implementation(  "com.github.denzcoskun:ImageSlideshow:0.1.2")


/*
//    def banubaSdkVersion = '1.31.2'
    implementation( "com.banuba.sdk:ffmpeg:5.1.3")
    implementation( "com.banuba.sdk:camera-sdk:1.31.2")
    implementation( "com.banuba.sdk:camera-ui-sdk:1.31.2")
    implementation( "com.banuba.sdk:core-sdk:1.31.2")
    implementation( "com.banuba.sdk:core-ui-sdk:1.31.2")
    implementation( "com.banuba.sdk:ve-flow-sdk:1.31.2")
    implementation( "com.banuba.sdk:ve-timeline-sdk:1.31.2")
    implementation( "com.banuba.sdk:ve-sdk:1.31.2")
    implementation( "com.banuba.sdk:ve-ui-sdk:1.31.2")
    implementation( "com.banuba.sdk:ve-gallery-sdk:1.31.2")
    implementation( "com.banuba.sdk:ve-effects-sdk:1.31.2")
    implementation( "com.banuba.sdk:effect-player-adapter:1.31.2")
    implementation( "com.banuba.sdk:ar-cloud:1.31.2")
    implementation( "com.banuba.sdk:ve-audio-browser-sdk:1.31.2")
    implementation( "com.banuba.sdk:banuba-token-storage-sdk:1.31.2")
    implementation( "com.banuba.sdk:ve-export-sdk:1.31.2")
    implementation( "com.banuba.sdk:ve-playback-sdk:1.31.2")*/

    implementation("com.squareup.picasso:picasso:2.71828")

    implementation("com.android.volley:volley:1.2.1")
    implementation("com.github.mreram:showcaseview:1.4.1")
    implementation("com.razorpay:checkout:1.6.33")
    implementation("org.jbundle.util.osgi.wrapped:org.jbundle.util.osgi.wrapped.org.apache.http.client:4.1.2")
    implementation("org.apache.pdfbox:pdfbox:2.0.29")
    implementation("com.itextpdf:itextg:5.5.10")
    implementation( "de.hdodenhof:circleimageview:3.1.0")
    implementation( "com.github.smarteist:autoimageslider:1.4.0")
    implementation( "com.steelkiwi:cropiwa:1.0.3")
    implementation( "org.wysaid:gpuimage-plus:2.6.3-min")
    implementation( "com.cepheuen.elegant-number-button:lib:1.0.2")
    implementation( "com.github.siyamed:android-shape-imageview:0.9.+@aar")
    implementation( "com.github.pavlospt:circleview:1.3")
    implementation( "ro.holdone:keyboardheightprovider:1.0.3")
    implementation( "com.isseiaoki:simplecropview:1.1.8")
    implementation( "com.agrawalsuneet.androidlibs:dotsloader:1.4")
    implementation( "androidx.multidex:multidex:2.0.1")

    // Java language implementation
    implementation("androidx.preference:preference:1.2.1")
    implementation("com.google.android.gms:play-services-ads:23.0.0")

}