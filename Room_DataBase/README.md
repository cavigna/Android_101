# Room Database

## 1 - Gradle

Agrueguemos las librerías necesarias:
https://developer.android.com/jetpack/androidx/releases/room

```Gradle
android {
    apply plugin: 'kotlin-kapt'
    //....
    
    }

dependencies {
    //LifeCycle
    def lifecycle_version = "2.4.0"


    // ViewModel
    implementation "androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycle_version"

    // LiveData
    implementation "androidx.lifecycle:lifecycle-livedata-ktx:$lifecycle_version"
    implementation "androidx.lifecycle:lifecycle-common-java8:$lifecycle_version"

    def room_version = "2.3.0"

    implementation "androidx.room:room-ktx:$room_version"
    implementation "androidx.room:room-runtime:$room_version"
    kapt "androidx.room:room-compiler:$room_version"
}

```

>**Importante: Recordar de usar el JDK 11, eso se puede cambiar en el icono del gradle**

## 2 - Modelo
Creamos un modelo con la anotación ```@Entity```. 
**Un consejo, si la llave primaria es de tipo int,podemos ponerle que se autogenere y darle un valor por defecto. Room se encargará de asignarle el valor al id**

```kotlin
@Entity(tableName = "mensaje_tabla")
data class Mensaje(
    @PrimaryKey(autoGenerate = true)
    val id : Int = 0,
    val mensaje: String
    
)
```

## 3 - Dao
Creamos una interfaz con la anotación ```@Dao```, la cual representa las acciones que se haran sobre la Base de Datos. Estás tendrán anotaciones como:
1. ```@Insert```: Aquí podemos seleccionar la estrategia con respecto al conflicto al ingresar nuevos datos. Las opciones más usadas son:
    1. ```@Insert(onConflict = OnConflictStrategy.REPLACE)```
    2. ```@Insert(onConflict = OnConflictStrategy.IGNORE)```


2. ```@Delete```
3. ```@Query```

Hay que tener en claro que debemos usar LiveData/Flow para las query de selección, y esta no requiere que las funciones sean de tipo suspend por que LiveData lo implementa de forma nativa.

```kotlin
@Dao
interface MensajeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun agregarMensaje(mensaje: Mensaje)

    @Delete
    suspend fun borrarMensaje(mensaje: Mensaje)

    @Query("SELECT * FROM mensaje_tabla")
    fun listadoMensajes(): Flow<List<Mensaje>>
}
```

## 4 - Base De Datos

La base de datos requiere la anotación ```@Database```:
```kotlin
@Database(entities = [Mensaje::class], version = 1, exportSchema = false)
//         ↑ 1.
//                                      ↑ 2.
//                                                      ↑ 3.

```
Como argumentos requiere:
1. Un listado con los modelos/entidades que conformarán la base de datos.
2. La versión, por si hay cambios en la estructura de la DB.
3. La opción de exportar el esquema


Nuestra Base de Datos hereda de ``RoomDatabase()``, por lo cual debe ser ``abstract class``.
A su vez tenemos que delcarar una función que representa nuestro DAO, y también debe ser abstracta ``asbtract fun dao(): MensajeDao``.
Por ultimo y no menos importante, creamos un patrón de tipo singleton estático con ``companion object``. representamos una instancia de nuestra db con la anotación ``@Volatile``. Esta se refiere que va a ser accesible imediatamente por otros [threads](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.jvm/-volatile/). Esta parte es un poco compleja de entender:

```kotlin
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
```

>**Basicamente dice: Si la instancia es nula crear la baase de datos con ``Room.databaseBuilder...``. Si la db existe, entonces devolver la instancia existente. En terminos de programación:**
```kotlin
    if(db == null){
        //crear base de datos
        return db creada
    }else{
        // devolver la db existente
        return instanciaExistente
    }
```
> *Observación: La base de datos requiere un contexto, una clase de DB, y el nombre de la db.*


```kotlin
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

```