package com.cavigna.tdcackes

import android.content.Context
import androidx.room.Room
import com.cavigna.tdcackes.model.local.db.BaseDeDatos
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
object DataTestModule {

    @Provides @Named("test.db")
    fun provideInMemoryDb(@ApplicationContext context: Context) =
        Room.inMemoryDatabaseBuilder(
            context, BaseDeDatos::class.java
        ).allowMainThreadQueries()
            .build()
}