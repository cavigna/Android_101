```kotlin

@TypeConverters
class Converters {

    @TypeConverter
    fun fromScreenShot(spokenLanguage: List<Screenshot>):String{
        val type = object : TypeToken<List<Screenshot>>(){}.type

        return Gson().toJson(spokenLanguage, type)?: "[]"
    }


    @TypeConverter
    fun toScreenShot(json:String):List<Screenshot>{
        val type=object : TypeToken<List<Screenshot>>(){}.type
        return Gson().fromJson(json,type)
    }
}

```

```kotlin
fun parseDateGame(fecha: String): String {
    val parser = SimpleDateFormat("yyyy-MM-dd")
    val formatter = SimpleDateFormat("dd MMM yyyy")
    return formatter.format(parser.parse(fecha))
}

fun sendEmailIntent(gameDetails: GameDetail, context: Context) {

    val i = Intent(Intent.ACTION_SEND)
    i.type = "message/rfc822"

    i.putExtra(Intent.EXTRA_EMAIL, arrayOf("email@emial.com"))
    i.putExtra(Intent.EXTRA_SUBJECT, ": Solicito información sobre  ${gameDetails.id}")
    i.putExtra(
        Intent.EXTRA_TEXT, "“Hola\n" +
                "Quisiera pedir información sobre esta torta ${gameDetails.title}, me gustaría que me contactaran a\n" +
                "este correo o al siguiente número _________\n" +
                "Quedo atento.”\n"
    )

    try {
        context.startActivity(Intent.createChooser(i, "Send mail..."))
    } catch (ex: ActivityNotFoundException) {
        Toast.makeText(
            context,
            R.string.error_email,
            Toast.LENGTH_SHORT
        ).show()
    }
}
```