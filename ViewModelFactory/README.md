<h1>ViewModelFactory</h1>


```kotlin

class AlgunModelFactory(private val repositorio: Repositorio): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AlgunViewModel(repositorio) as T
    }
}

```