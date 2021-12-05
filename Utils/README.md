<h1>Utils</h1>


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

```kotlin
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
}

```