package com.example.tutoexpressroom.application

import android.app.Application
import com.example.tutoexpressroom.db.BaseDeDatos
import com.example.tutoexpressroom.repository.Repositorio

class MensajeApplication : Application() {

    private val database by lazy { BaseDeDatos.getDataBase(this) }
    val repositorio by lazy { Repositorio(database.dao()) }
}