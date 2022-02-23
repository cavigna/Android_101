
## Gradle

```gradle

    android{
        /**

        */
            packagingOptions {
        exclude 'META-INF/atomicfu.kotlin_module'
        exclude "META-INF/licenses/**" //testing
        exclude "META-INF/AL2.0" //testing
        exclude "META-INF/LGPL2.1" //testing
    }
    }

    //Coroutines Test
    androidTestImplementation 'org.jetbrains.kotlinx:kotlinx-coroutines-test:1.4.2'

    //test google
    androidTestImplementation "com.google.truth:truth:1.1.3"
    androidTestImplementation "androidx.arch.core:core-testing:2.0.0"

    // For instrumentation tests HILT
    androidTestImplementation  'com.google.dagger:hilt-android-testing:2.40.5'
    kaptAndroidTest 'com.google.dagger:hilt-compiler:2.40.5'

    // For local unit tests HILT
    testImplementation 'com.google.dagger:hilt-android-testing:2.40.5'
    kaptTest 'com.google.dagger:hilt-compiler:2.40.5'

```

## Runner
Para poder ejecutar el Test, necesitamos un runner. Para eso creamos una clase en la carpeta de AndroidTest(donde está el archivo *ExampleInstrumentedTest*). El runner es así:

```kotlin
class TestRunner : AndroidJUnitRunner() {
    override fun newApplication(
        cl: ClassLoader?,
        className: String?,
        context: Context?
    ): Application {
        return super.newApplication(cl, HiltTestApplication::class.java.name, context)
    }
}
```

A su vez, en el gradle a nivel módulo modificamos el *testInstrumentationRunner* con la ruta de la clase del runner(``click derecho en la clase, copy path, seleccionar la opción Copy Refeerence``)

```gradle
// Cambiamos Esto
testInstrumentationRunner "androidx.test.runner.AndroidJUnitRunner"

//Por esto
testInstrumentationRunner "com.example.myapplication.TestRunner"
```

## Crear un Módulo de Test
En la misma carpeta de Android test, replicamos un módulo que será la base de injección para el Test. **Es importante recordar que si estamos haciendo test de ROOM, debemos agregar la anotación ``@Named("test.db")``**

```kotlin
@Module
@InstallIn(SingletonComponent::class)
object DataTestModule {

    @Provides @Named("test.db")
    fun provideInMemoryDb(@ApplicationContext context: Context) =
        Room.inMemoryDatabaseBuilder(
            context, BaseDeDatos::class.java
        ).allowMainThreadQueries()
            .build()
}
```

## Crear Test
En la misma carpeta, creamos el test. 

```kotlin
@HiltAndroidTest
@SmallTest
class LocalTestDB {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    @Named("test.db")
    lateinit var db: BaseDeDatos

    private lateinit var  dao: CakeDao

    @Before
    fun init(){
        hiltRule.inject()
        dao = db.dao
    }

    @After
    fun tearDown() {
        db.close()
    }

    @ExperimentalCoroutinesApi
    @Test
    fun insertCakeTest()= runBlockingTest{
        val cake = Cake(120, "Prueba")
        val cakeNot = Cake(150, " No está")


        dao.insertCakes(listOf(cake))
        val allCakes = dao.selectAllCAkes()
        assertThat(allCakes).contains(cake)
       // assertThat(allCakes).contains(cakeNot)

    }

    @ExperimentalCoroutinesApi
    @Test
    fun insertCakeDetailsTest() = runBlockingTest {
        val cakeDetails = CakeDetails(500, "CakeDetails")

        dao.insertCakeDetails(cakeDetails)
        assertThat(dao.selectCakeDetail(500)).isEqualTo(cakeDetails)
    }
}
```
