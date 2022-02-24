package com.cavigna.mmotd.utils

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.os.Build
import android.text.Html
import android.widget.Toast
import com.cavigna.mmotd.R
import com.cavigna.mmotd.model.models.GameDetail
import java.text.SimpleDateFormat


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

fun parseHtml(string: String)= if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
    Html.fromHtml(string, Html.FROM_HTML_MODE_COMPACT).toString()
} else {
    Html.fromHtml(string).toString()
}