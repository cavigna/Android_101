<h1>Interface para Obtener item fuera del RecyclerView</h1>


<h2> Adapter</h2>

<h3>1- Crear Interfaz </h3>
Dentro del Adaptador debemos crear una interfaz con el objeto a sacar del Recycler:


```kotlin

interface MiEscuchador { // 1 ==> Aca Creamos la Interfaz
        fun alHacerClick(url: String)
```

<h3>2- Implementación en el onBindViewHolder</h3>
Ponemos dentro del onBindViewHolder, un setonclick con nuestra interfaz


```kotlin

   override fun onBindViewHolder(holder: MiViewHolder, position: Int) {

        val currentModelo = getItem(position)
        
        holder.binding.item.setOnClickListener{
            miEscuchador.alHacerClick(currentModelo) // 2 ==> Implementamos la interfaz en bindviewHolder
        }

```


<h3>2- Agregar argumento a Nuesto adaptador</h3>



```kotlin
class AlgunListAdapter(private val miEscuchador: MiEscuchador): ListAdapter<AlgunModelo, MiViewHolder>(AlgunComparador()) {
    //......
}

```

Finalmente nos quedaría algo así...


```kotlin

class AlgunListAdapter(private val miEscuchador: MiEscuchador): ListAdapter<AlgunModelo, MiViewHolder>(AlgunComparador()) {
// 3 ==> Poner como argumento del Adaptador la Interfaz
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FotosViewHolder {
        return MiViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: MiViewHolder, position: Int) {

        val currentModelo = getItem(position)
        
        holder.binding.item.setOnClickListener{
            miEscuchador.alHacerClick(currentModelo) // 2 ==> Implementamos la interfaz en bindviewHolder
        }

    }

    interface MiEscuchador { // 1 ==> Aca Creamos la Interfaz
        fun alHacerClick(url: String)
    }
}
```
<h2> En Activity o Fragment</h2>

<h3>4- Implementamos en Activity/Fragment </h3>

En la actividad/fragment, implementamos la interfaz


```kotlin

class AlgunFragment : Fragment(), AlgunListAdapter.MiEscuchador{
    //.....
} 
```


<h3>4- Implementamos en Activity/Fragment </h3>

En la actividad/fragment, implementamos la interfaz


```kotlin

class AlgunFragment : Fragment(), AlgunListAdapter.MiEscuchador{ 
    //                               ↑
    // Avisamos que Implementaremos la interfaz
        override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavBinding.inflate(layoutInflater, container, false)
    //.....

        val recyclerView = binding.recyclerViewFav
       
        val adapter = FavListAdapter(this)
        //              ↑ (alt+24)

        // como argunemento del adaptador, referimos a this!
        return binding.root
    }

    //4 - Implementación De la interfaz

    override fun alHacerClick(algunModelo: AlgunModelo) {
        viewModel.acciónDelViewModel(algunModelo)
    }

} 
```
