# Pruebas

La ubicación del código de tu prueba dependerá del tipo de prueba que escribas. Android Studio proporciona directorios de código fuente (conjuntos de orígenes) para los siguientes dos tipos de pruebas:

Pruebas de unidad local
Ubicación: module-name/src/test/java/.

Estas son pruebas que se ejecutan en la máquina virtual Java (JVM) local de tu máquina. Usa estas pruebas para minimizar el tiempo de ejecución cuando tus pruebas no tengan dependencias de framework de Android o cuando puedas simularlas.

Durante el tiempo de ejecución, las pruebas se ejecutan en relación con la versión modificada de android.jar, en la que se quitan todos los modificadores final. De esta manera, puedes usar bibliotecas de simulación populares, como Mockito.

Pruebas instrumentadas
Ubicación: module-name/src/androidTest/java/.

Son pruebas que se ejecutan en un dispositivo o emulador de hardware. Esas pruebas tienen acceso a las API de Instrumentation, te brindan acceso a información como el Context de la app que estás probando y te permiten controlar la app desde el código de prueba. Úsalas cuando escribas pruebas de IU de integración y funcionales para automatizar la interacción de usuarios, o cuando tus pruebas tengan dependencias de Android que los objetos ficticios no puedan contemplar.

Debido a que las pruebas instrumentadas se compilan en un APK (independiente del APK de tu app), deben tener su propio archivo AndroidManifest.xml. Sin embargo, Gradle genera automáticamente ese archivo durante la compilación para que no se vea en el conjunto de orígenes de tu proyecto. Puedes agregar tu propio archivo de manifiesto si es necesario, por ejemplo, a fin de especificar un valor diferente para "minSdkVersion" o registrar objetos de escucha de ejecución solo para tus pruebas. Cuando se compila tu app, Gradle combina varios archivos de manifiesto en uno solo.


##RetroFit

Crear una carpeta llamada assets a nivel app. Ir a APP, new directory y elegir uno que dice srrc/main/assets


Crear un archivo de tipo json para replicar la respuesta de RetroFit

Crear en la Carpeta module-name/src/androidTest/java/ un archivo llamado file reader:

```kotlin
import androidx.test.platform.app.InstrumentationRegistry
import java.io.IOException
import java.io.InputStreamReader

object FileReader {
    fun readStringFromFile(fileName: String): String {
        try {
            val inputStream = (InstrumentationRegistry.getInstrumentation().targetContext
                .applicationContext).assets.open(fileName)
            val builder = StringBuilder()
            val reader = InputStreamReader(inputStream, "UTF-8")
            reader.readLines().forEach {
                builder.append(it)
            }
            return builder.toString()
        } catch (e: IOException) {
            throw e
        }
    }
}
```

Crear un archivo Para prueba remota:

```kotlin



@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@MediumTest
class RemoteTest {

    @get: Rule

    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private val mockWebServer = MockWebServer()
    private val MOCK_WEBSERVER_PORT = 8000
    private lateinit var repositorioTest: Repositorio
    private lateinit var repositorio: Repositorio

    private lateinit var database: BaseDeDatos
    private lateinit var dao: CryptoDao

    private val dispatcher = TestCoroutineDispatcher()
    private val testScope = TestCoroutineScope(dispatcher)


    @Before
    fun setup() {
        mockWebServer.start(MOCK_WEBSERVER_PORT)
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, BaseDeDatos::class.java)
            .allowMainThreadQueries()
            .build()

        dao = database.dao()

        val retrofitClientTest by lazy {
            Retrofit.Builder()
                .baseUrl(mockWebServer.url("/"))
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiService::class.java)
        }


        val retrofitClient by lazy {
            Retrofit.Builder()
                .baseUrl("https://fake-server-app-crypto.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiService::class.java)

        }



        repositorioTest = Repositorio(retrofitClientTest, dao)
        repositorio = Repositorio(retrofitClient, dao)
    }

    @After
    fun teardown() {
        mockWebServer.shutdown()
        database.close()
    }

    @Test
    fun cryptoListaComparador() {
        val cryptoOriginal = CryptoItem(
            id = "BTC",
            currency = "BTC",
            logoUrl = "https://s3.us-east-2.amazonaws.com/nomics-api/static/images/currencies/btc.svg",
            name = "Bitcoin",
            price = "57908.88281873",
            status = "active",
            priceDate = "2021-05-08T00:00:00Z",
            priceTimestamp = "2021-05-08T06:15:00Z",
            rank = "1",
            symbol = "BTC"


        )

        mockWebServer.apply {
            enqueue(
                MockResponse()
                    .setResponseCode(200)
                    .setBody(
                        FileReader.readStringFromFile("mock_response.json")
                    )
            )
        }

        testScope.launch {
            val crypto = repositorioTest.fecthListApi()[0]

            assertThat(crypto).isEqualTo(cryptoOriginal)
        }

    }
}
```

### Unit Testing

```kotlin
@RunWith(JUnit4::class)
class UtilsKtTest {

    @Test
    fun testRedondeo(){
        val input = "3538.44529165"
        val ouput = "3538,45"
        val resultado = redondeo(input)
        assertEquals(resultado,ouput)
    }

}

```

