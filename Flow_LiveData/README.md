# Flow and LiveData

### ``BookDao.kt``

```kotlin
@Dao
interface BookDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
     fun insertBooks(books: List<Books>)

    @Query("SELECT * FROM books_table")
    fun selectAllBooksLD(): LiveData<List<Books>>
    
    @Query("SELECT * FROM books_table")
    fun selectAllBooksFlow():Flow<List<Books>>
}

```


Supongamos que tenemos el Siguiente Repositorio:

### ``Repositorio.kt``

```kotlin   
    
class Repositorio(private val api: ApiService, private val dao: BookDao) {

    suspend fun fetchBooks() = api.fetchBooksListApi()

    fun insertBooks(books: List<Books>) = dao.insertBooks(books)

    val selectAllBooksAsLiveData: LiveData<List<Books>> = liveData {
        emit(dao.selectAllBooks())
    }

    val selectAllBooksAsFlow:Flow<List<Books>> = flow {
        emit(dao.selectAllBooks())
    }

    val selectAllBooksLD: LiveData<List<Books>> = dao.selectAllBooksLD()


    val selectAllBooksFlow = dao.selectAllBooksFlow()

}
```
    
    








```kotlin


```




