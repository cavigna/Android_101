# Hacer un ListAdapter

El ListAdapter es la recomendación de Google al utilizar elementos como ```LiveData<T>```. En definitiva exitiende de la misma base que un reciclador normal, de ```RecyclerView.Adapter```


### 0 - Código Completo

### 1 - Creación Clase ListAdapter

1. Creamos una clase que implementé ListAdapter, el cual requiere dos objetos
   1. El Primero, es la clase que queremos reproducir en el recycler. Puede ser una string, o un objeto complejo como un modelo.
   2. EL segundo es el ViewHolder
   3. Como argumento de ```import androidx.recyclerview.widget.ListAdapter``` debemos implementar un comparador de elementos. Más adelante lo vamos a ver...

```kotlin
class MyListAdapter: ListAdapter<AlgunModelo, MyViewHolder>(MyComparator()) {
    //                  ↑           ↑              ↑          ↑ 

    //              ListAdapter ==> 1. =======> 2. ==========>3.
}

```

### 2 - Creación Del ViewHolder

2. Debemos crear una clase ViewHolder que hereda de RecyclerView ```RecyclerView.ViewHolder(itemView)```
   1. Lo importante acá es que el ViewHolder requiere un argumento de tipo view, ```itemView:View``` que será parámetro para RecyclerVIew.ViewHolder.
   2. Lo segundo, más importante que el primero, es que debemos crear un ```companion object``` que contenga una función create(esto nos permite que sea estático). Como argumento requiere ```parent: ViewGroup```para retornarnos my ```MyViewHolder```
   3. Aquí se define el bindining, y para inflar la vista requerimos el contexto del padre ```ItemRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)```
   4. Si lo deseamos, en esta clase podemos crear una función que una los datos. 

```kotlin
class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val binding = ItemRowBinding.bind(itemView)

    companion object{
        fun create(parent: ViewGroup): MyViewHolder{
            val layoutInflaterB = LayoutInflater.from(parent.context)
            val binding = ItemRowBinding.inflate(layoutInflaterB, parent, false)

            return MyViewHolder(binding.root)
        }
    }
}

```

### 3 - Implementación de las funciones recyclerview.widget.ListAdapter

3. Hay dos funciones que implementar:
   1. ```onCreateViewHolder```: Nos devuelve la función create que implementamos en el viewHolder.
   2.  ```onBindViewHolder(holder: MyViewHolder)```: esta nos permite obtener el item del listado y hacer un bind con los elementos de la vista


```kotlin
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val algunModelo = getItem(position)

        holder.binding.textView.text = algunModelo.nombre

        // alternativamente se puede hacer:

        with(holder.binding){
            textView.text = algunModelo.nombre

        }

    }
```
### 4 - El Comparador

Según [Google](https://developer.android.com/reference/androidx/recyclerview/widget/DiffUtil)
>DiffUtil is a utility class that calculates the difference between two lists and outputs a list of update operations that converts the first list into the second one.

4. El comparador hereda de DiffUtil y requiere un modelo al que comparar, por ejemplo ``` DiffUtil.ItemCallback<AlgunModelo>()```. Debemos sobre escribir dos métodos:
   1. ``` override fun areItemsTheSame```. Lo importante es que aquí el return es : ```return oldItem == newItem```
   2. ``` areContentsTheSame```. El return es: ```return oldItem.id == newItem.id```

```Kotlin
class MyComparator : DiffUtil.ItemCallback<AlgunModelo>(){
    override fun areItemsTheSame(oldItem: AlgunModelo, newItem: AlgunModelo): Boolean {
       return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: AlgunModelo, newItem: AlgunModelo): Boolean {
        return oldItem.id == newItem.id
    }

}
```

### 5 - En el fragment/activity:
Recordemos que debemos setear el adaptador y definir su layout:
```kotlin
        val recyclerView = binding.recyclerViewRaza
        val adapter = MyListAdapter()

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        
        viewModel.listadoAObservar.observe(viewLifecycleOwner, {
             adapter.submitList(it)        

        })

```
### 6 - El código Completo:


```kotlin

class MyListAdapter: ListAdapter<AlgunModelo, MyViewHolder>(MyComparator()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val algunModelo = getItem(position)

        holder.binding.textView.text = algunModelo.nombre

        // alternativamente se puede hacer:

        with(holder.binding){
            textView.text = algunModelo.nombre

        }

    }


}

class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val binding = ItemRowBinding.bind(itemView)

    companion object{
        fun create(parent: ViewGroup): MyViewHolder{
            val layoutInflaterB = LayoutInflater.from(parent.context)
            val binding = ItemRowBinding.inflate(layoutInflaterB, parent, false)

            return MyViewHolder(binding.root)
        }
    }
}

class MyComparator : DiffUtil.ItemCallback<AlgunModelo>(){
    override fun areItemsTheSame(oldItem: AlgunModelo, newItem: AlgunModelo): Boolean {
       return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: AlgunModelo, newItem: AlgunModelo): Boolean {
        return oldItem.id == newItem.id
    }

}

```
