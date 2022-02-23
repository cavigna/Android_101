<h1>Quick Set up TDD</h1>

<h2>Gradle</h2>

<h4>Gradle App</h4>

```kotlin

classpath "com.google.dagger:hilt-android-gradle-plugin:2.40.5"

```



<h4>Gradle Module</h4>


```kotlin
plugins {
    id 'com.android.application'
    id 'kotlin-android'
}

android {
    compileSdk 31
    buildToolsVersion '30.0.2'

    apply plugin: 'kotlin-kapt'
    apply plugin: 'dagger.hilt.android.plugin'

    buildFeatures{
        viewBinding true
    }


    defaultConfig {
        applicationId "com.example.myapplication" // CAMBIAR NOMBRE DE ESTO!!!!!!!!!/////////////////////////////////////
        minSdk 23
        targetSdk 30
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
    implementation 'androidx.appcompat:appcompat:1.4.1'
    implementation 'com.google.android.material:material:1.5.0'
    implementation 'androidx.constraintlayout:constraintlayout:2.1.3'
    implementation 'androidx.legacy:legacy-support-v4:1.0.0'
    testImplementation 'junit:junit:4.+'
    androidTestImplementation 'androidx.test.ext:junit:1.1.3'
    androidTestImplementation 'androidx.test.espresso:espresso-core:3.4.0'


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
    




    //Coil

    implementation("io.coil-kt:coil:1.4.0")
    implementation("io.coil-kt:coil-svg:1.4.0")

    //Coroutines Test
    androidTestImplementation 'org.jetbrains.kotlinx:kotlinx-coroutines-test:1.4.2'

    //test google
    androidTestImplementation "com.google.truth:truth:1.1.3"
    androidTestImplementation "androidx.arch.core:core-testing:2.0.0"

    //MockWebServer
    androidTestImplementation "com.squareup.okhttp3:mockwebserver:4.9.2"
    androidTestImplementation "com.squareup.okhttp3:okhttp:4.9.2"
    debugImplementation 'com.squareup.okhttp3:okhttp:4.9.2'

    implementation 'com.google.dagger:hilt-android:2.40.5'
    kapt 'com.google.dagger:hilt-compiler:2.40.5'

    // For instrumentation tests
    androidTestImplementation  'com.google.dagger:hilt-android-testing:2.40.5'
    kaptAndroidTest 'com.google.dagger:hilt-compiler:2.40.5'

    // For local unit tests
    testImplementation 'com.google.dagger:hilt-android-testing:2.40.5'
    kaptTest 'com.google.dagger:hilt-compiler:2.40.5'

    implementation "androidx.lifecycle:lifecycle-runtime-ktx:$lifecycle_version"
}

kapt {
    correctErrorTypes true
}
```



<h3>DataModule</h3>

```kotlin

@Module
@InstallIn(SingletonComponent::class)
object DataModule {


    @Singleton
    @Provides
    fun providesRetrofit(): ApiService = Retrofit.Builder()
        .baseUrl("https://fake-server-app-crypto.herokuapp.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ApiService::class.java)

    @Singleton
    @Provides
    fun providesDatabase(@ApplicationContext context: Context) =
        Room.databaseBuilder(context, BaseDeDatos::class.java, "coin.db").build()

    @Singleton
    @Provides
    fun providesCoinDao(db: BaseDeDatos) = db.dao
}

```

<h3>Adapter</h3>

```kotlin
class AdaptadorGenerico : ListAdapter<Coins, MyViewHolder>(Comparador()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val coin = getItem(position)

        with(holder.binding){
            /*
            LOGICA VISUAL
             */
        }

    }
}

class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    val binding: ItemRowBinding = ItemRowBinding.bind(itemView)

    companion object{
        fun create(parent: ViewGroup):MyViewHolder{
            val layoutInflaterB = LayoutInflater.from(parent.context)
            val binding = ItemRowBinding.inflate(layoutInflaterB, parent, false)

            return MyViewHolder(binding.root)
        }
    }

}

class Comparador: DiffUtil.ItemCallback<Coins>() {
    override fun areItemsTheSame(oldItem: Coins, newItem: Coins): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Coins, newItem: Coins): Boolean {
        return oldItem.id == newItem.id

    }

}

```


