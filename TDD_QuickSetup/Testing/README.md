# UNIT TEST

```kotlin

class UtilsKtTest {

    @Test
    fun toCurrencyTest() {
        val input = 323760
        val output = "$323.760"

        assertEquals(toCurrency(input), output)

    }
}

```

# INSTRUMENTAL TEST

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

## CAMBIAR EN GRADLE - IMPORTANTE

```gradle
// Cambiamos Esto
testInstrumentationRunner "androidx.test.runner.AndroidJUnitRunner"

//Por esto
testInstrumentationRunner "com.example.myapplication.TestRunner"

```



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
