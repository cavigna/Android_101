package com.example.tutoexpressroom.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.tutoexpressroom.dao.MensajeDao
import com.example.tutoexpressroom.model.Mensaje

@Database(entities = [Mensaje::class], version = 1, exportSchema = false)
abstract class BaseDeDatos : RoomDatabase() {
    abstract fun dao() : MensajeDao

    companion object {

        @Volatile
        private var INSTANCE: BaseDeDatos? = null

        fun getDataBase(context: Context): BaseDeDatos {

            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BaseDeDatos::class.java,
                    "mensaje_db"
                )
                    .build()
                INSTANCE = instance

                instance
            }
        }
    }
}