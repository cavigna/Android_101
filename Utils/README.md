<h1>Utils</h1>

<h2>Hide KeyBoard</h2>

```kotlin

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment

fun Fragment.hideKeyboard() {
    view?.let { activity?.hideKeyboard(it) }
}

fun Activity.hideKeyboard() {
    hideKeyboard(currentFocus ?: View(this))
}

fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

```

<h2>Some fun funs</h2>

```kotlin
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.simpledict.model.models.api.ResultWordApi
import com.example.simpledict.model.models.Word
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

inline fun <T> Flow<T>.launchAndCollectIn(
    owner: LifecycleOwner,
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
    crossinline action: suspend CoroutineScope.(T) -> Unit
) = owner.lifecycleScope.launch {
    owner.repeatOnLifecycle(minActiveState) {
        collect {
            action(it)
        }
    }
}

//To reduce BiolerPlate.
//Needs to use viewLifecyleOwner 'cause it's a fragment.
inline fun Fragment.launchAndRepeatWithViewLifecycle(
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
    crossinline block: suspend CoroutineScope.() -> Unit
) {
    viewLifecycleOwner.lifecycleScope.launch {
        viewLifecycleOwner.lifecycle.repeatOnLifecycle(minActiveState) {
            block()
        }
    }
}

fun toListOfWords(list: List<ResultWordApi>):List<Word>{
    val listadoWord = mutableListOf<Word>()
    list.forEach {
        listadoWord.add(it.toWord())
    }
    return listadoWord
}

inline fun <T> List<ResultWordApi>.toWords():List<Word>{
    val listadoWord = mutableListOf<Word>()
    this.forEach {
        listadoWord.add(it.toWord())
    }
    return listadoWord
}

fun toCurrency(precio:Int): String {
    val numberFormat = NumberFormat.getCurrencyInstance(Locale("ES", "CL"))
    numberFormat.maximumFractionDigits =2
    return numberFormat.format(precio)
}

fun sendEmailIntent(cakeDetails: CakeDetails, context: Context){

    val i = Intent(Intent.ACTION_SEND)
    i.type = "message/rfc822"

    i.putExtra(Intent.EXTRA_EMAIL, arrayOf("email@emial.com"))
    i.putExtra(Intent.EXTRA_SUBJECT, ": Solicito información sobre  ${cakeDetails.id}")
    i.putExtra(Intent.EXTRA_TEXT, "“Hola\n" +
            "Quisiera pedir información sobre esta torta ${cakeDetails.title}, me gustaría que me contactaran a\n" +
            "este correo o al siguiente número _________\n" +
            "Quedo atento.”\n")

    try {
        context.startActivity(Intent.createChooser(i, "Send mail..."))
    } catch (ex: ActivityNotFoundException) {
        Toast.makeText(
            context,
            R.string.error_email,
            Toast.LENGTH_SHORT
        ).show()
    }
```


<h2>TypeConverters</h2>

```kotlin
@TypeConverters
import androidx.room.TypeConverter

class Converters {

    @TypeConverter

    fun fromListadoAString(listado: List<String?>):String?{
        return listado.joinToString {
            it.toString()
        }
    }

    @TypeConverter
    fun fromStringToListado(string: String):List<String>{
        return string.split(",")
    }

    @TypeConverter
    fun fromListadoIntToString(list: List<Int>):String{
        return list.joinToString {
            it.toString()
        }
    }

    @TypeConverter
    fun fromStringToListadoInt(string: String?):List<Int?>?{
        val numeros = string?.split(",")

        return numeros?.map { it.toIntOrNull() }
    }

        @TypeConverter
    fun fromMeaning(json:String):List<Meaning>{
        val type=object : TypeToken<List<Meaning>>(){}.type
        return Gson().fromJson(json,type)
    }
    @TypeConverter
    fun toMeaning(meanings:List<Meaning>):String{

        val type =object :TypeToken<List<Meaning>>(){}.type

        return Gson().toJson(meanings,type)?: "[]"
    }
}

```


</h2>Media Player</h2>

```kotlin
 val mediaPlayer = MediaPlayer().apply {
                            setAudioAttributes(
                                AudioAttributes.Builder()
                                    .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                                    .setUsage(AudioAttributes.USAGE_MEDIA)
                                    .build()
                            )
                            setDataSource("https://...")
                            prepare()
                            start()

                        }
```




</h2>Media Player</h2>

```kotlin
inline fun <ResultType, RequestType> networkBoundResource(
    crossinline query: () -> Flow<ResultType>,
    crossinline fetch: suspend () -> RequestType,
    crossinline saveFetchResult: suspend (RequestType) -> Unit,
    crossinline shouldFetch: (ResultType?) -> Boolean = { true },
    coroutineDispatcher: CoroutineDispatcher = IO
) = flow<Resource<ResultType>> {

    // check for data in database
    val data = query().firstOrNull()

    if (data != null) {
        Log.v("pruebas", "IN DB")
        // data is not null -> update loading status
        emit(Resource.Loading(data))
    }else{
        // Need to fetch data -> call backend
        val fetchResult = fetch()
        // got data from backend, store it in database
        saveFetchResult(fetchResult)
        Log.v("pruebas", "BUSCADO EN LA API")
    }

    // load updated data from database (must not return null anymore)
    val updatedData = query().first()

    // emit updated data
    emit(Resource.Success(updatedData))

}.onStart {
    Log.v("pruebas", "Loading")
    emit(Resource.Loading(null))

}.catch { exception ->
    Log.v("pruebas", "error")
    emit(Resource.Error(exception, null))


}.flowOn(coroutineDispatcher)
```