package com.example.tutoexpressroom.dao

import androidx.room.*
import com.example.tutoexpressroom.model.Mensaje
import kotlinx.coroutines.flow.Flow

@Dao
interface MensajeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun agregarMensaje(mensaje: Mensaje)

    @Delete
    suspend fun borrarMensaje(mensaje: Mensaje)

    @Query("SELECT * FROM mensaje_tabla")
    fun listadoMensajes(): Flow<List<Mensaje>>
}