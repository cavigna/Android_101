package com.example.tutoexpressroom.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "mensaje_tabla")
data class Mensaje(
    @PrimaryKey(autoGenerate = true)
    val id : Int = 0,
    val mensaje: String

)
