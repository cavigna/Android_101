# Injección de Dependencia Manual

Supongamos que tenemos una app con 10 activitys y 5 fragmentos cada una, eso implicaría que si tenemos un viewmodel con API/ROOM, necesitariamos instanciar 50 veces el repositorio, la base de datos, etc. Por ende, para simplificar un poco las cosas podemos utilizar una mini injección de dependencias. Suponiendo que tenemos un servicio de retrofit, una base de datos y un repositorio de la siguiente manera:

#### - ```Dao.kt``` 

```kotlin
@Dao
interface MiDao{


    @Query("SELECT * FROM mi_tabla")
    fun buscarDatosLocales() : Flow<List<AlgunModelo>>
} 

```

#### - ```ApiService.kt``` 

```kotlin
interface ApiService {

    @GET("url/list") 
    suspend fun buscarDatosRemotos(): List<AlgunModelo>
}

```

#### - ```Repositorio.kt``` 
```kotlin
class Repositiorio(private val api: ApiService, private val dao: MiDao){
    
    suspend fun buscarDatosRemotos() = api.buscarDatosRemotos()
    fun buscarDatosLocales : Flow<List<AlgunModelo>> = dao.buscarDatosLocales()
}
```
#### - ```ViewModel.kt``` 
```kotlin
class MyViewModel(private val repo : Repositorio) :ViewModel(){

    var listadoRemoto = repo.buscarDatosRemoto()

    private var _listadoDatoLocal  = MutableLiveData<List<AlgunModelo>>()

    val listadoDatoLocal :LiveData<List<AlgunModelo>> = _listadoDatolocal

    fun buscarDatosLocales() {
        viewModelScope.launch {
            repo.buscarDatosLocales()
   
            }
        }
}

```

## MiApplication()

Hacemos un nuevo paquete, llamado application la cual herede de Application y dentro de la misma instanciaremos el cliente de retrofit, la base de datos y  el repositorio

```kotlin
class PerroApplication : Application() {

    private val retrofitCliente by lazy {
        Retrofit.Builder()
            .baseUrl("https://urlbase/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)

    }

    private val baseDeDatos by lazy { BaseDeDatos.getDataBase(this) }

    val repositorio by lazy { Repositorio(retrofitCliente, baseDeDatos.dao()) }


}
```

**Importane**
Debemos agregar en el MANIFEST nuestra Application

```xml
 <application
        android:name=".application.MyApplication"
        ....
```

## En Fragmento

```kotlin

class AlgunFragment : Fragment() {

    private lateinit var binding: FragmentAlgunBinding
    private lateinit var application: Application

    private val viewModel: MyViewModel by activityViewModels {
        MyModelFactory((application as MyApplication).repositorio)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        application = requireActivity().application

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
      //....

        return binding.root
    }

}

```

## En Activity

```kotlin
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    private val viewModel: PerroViewModel by viewModels {
         MyModelFactory((application as MyApplication).repositorio)
    }
}
```

