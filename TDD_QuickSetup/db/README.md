

```kotlin

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(entities = [Producto::class, ProductoDetail::class], version = 1)
abstract class BaseDeDatos :RoomDatabase(){
    abstract val dao: ProductDao
}

```