# Injección de Dependencia Manual

Siguiendo lo explicado en la [pagina de Google](https://developer.android.com/training/dependency-injection/manual?hl=es-419#:~:text=La%20inyecci%C3%B3n%20de%20dependencia%20es,instancias%20de%20clases%20con%20f%C3%A1bricas.),  nos recomienda la di por lo siguiente:
>" La inyección de dependencia es una buena técnica a la hora de crear apps para Android escalables que puedan someterse a prueba. Usa contenedores como una manera de compartir instancias de clases en diferentes partes de tu app y como un lugar centralizado para crear instancias de clases con fábricas. Cuando se expanda tu aplicación, comenzarás a ver que escribes mucho código estándar (como fábricas), que puede ser propenso a errores. También debes administrar el alcance y el ciclo de vida de los contenedores por tu cuenta, optimizando y descartando los contenedores que ya no se necesitan para liberar memoria. Hacer esto de forma incorrecta puede generar errores sutiles y pérdidas de memoria en tu app"

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

>**Importante:** Si estamos en un fragmento, debemos usar by activityViewModels de la librería : ```import androidx.fragment.app.activityViewModels```.
  "Returns a property delegate to access ViewModel by default scoped to this Fragment. Custom ViewModelProvider.Factory can be defined via factoryProducer parameter, factory returned by it will be used to create ViewModel":

```kotlin
class MyFragment : Fragment() {
    val viewmodel: MyViewModel by viewmodels { myFactory }
}
```

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
>**Importante:** Si estamos en una actividad, debemos usar by viewModels de la librería : ```import androidx.activity.viewModels```.
  "Returns a property delegate to access parent activity's ViewModel, if factoryProducer is specified then ViewModelProvider.Factory returned by it will be used to create ViewModel first time. Otherwise, the activity's default factory will be used.":

```kotlin
class MyFragment : Fragment() {
    val viewmodel: MyViewModel by activityViewModels()
}
```

```kotlin
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    private val viewModel: PerroViewModel by viewModels {
         MyModelFactory((application as MyApplication).repositorio)
    }
}
```

