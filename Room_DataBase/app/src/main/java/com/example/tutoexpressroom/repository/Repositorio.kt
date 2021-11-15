package com.example.tutoexpressroom.repository

import com.example.tutoexpressroom.dao.MensajeDao
import com.example.tutoexpressroom.model.Mensaje
import kotlinx.coroutines.flow.Flow

class Repositorio(private val dao: MensajeDao) {

    suspend fun agregarMensaje(mensaje: Mensaje) = dao.agregarMensaje(mensaje)

    suspend fun borrarMensaje(mensaje: Mensaje) = dao.borrarMensaje(mensaje)

    fun listadoMensaje(): Flow<List<Mensaje>> = dao.listadoMensajes()
}