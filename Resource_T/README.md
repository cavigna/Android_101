# RESOURCE T
Wrapper Class that handle States
```kotlin
/**
 * Wrapper class for handling states on API requests.
 * States:
 * [Success] ==> When Api Response is succesfull
 * [ErrorThrowable] ==> Handles Exception like IO
 * [ErrorApi] ==> Handles unsuccessfully request to API
 * [Loading] ==> Handles the state of Loading
 */
sealed class Resource<T>(
    val data: T? = null,
    val errorThrowable: Throwable? = null,
    val errorMessage:String?=null
) {
    class Success<T>(data: T) : Resource<T>(data)
    class ErrorThrowable<T>(errorThrowable: Throwable, data: T? = null) :
        Resource<T>(data, errorThrowable)

    class ErrorApi<T>(errorMessage: String) : Resource<T>(errorMessage = errorMessage)
    class Loading<T>(data: T? = null) : Resource<T>(data)

    /**
     * This Companion Object simplifies the uses of states, especially to handle errors in flow.
     */
    companion object {
        fun <T> success(data: T): Resource<T> = Success(data)
        fun <T> errorThrowable(errorThrowable: Throwable): Resource<T> =
            ErrorThrowable(errorThrowable)

        fun <T> errorApi(errorMessage: String): Resource<T> = ErrorApi(errorMessage)
        fun <T> loading(): Resource<T> = Loading()
    }
}
```

## ApiService

```kotlin
interface ApiService {  
  
    @GET("news")
    suspend fun fetchLatestNews() : Response<NewsResponse>
    }
```

## Repositrorio
En el repositorio podemos manejar los estados de la siguiente manera:

```kotlin
class Repository(){
    /***/

        suspend fun fetchLatestNews(): Flow<Resource<NewsResponse?>> = flow {
        val response = apiService.fetchLatestNews()

        when (response.isSuccessful) {
            true -> emit(Resource.Success(response.body()))
            false -> emit(Resource.ErrorApi(response.errorBody().toString()))
        }
    }.catch { e: Throwable ->
        emit(Resource.ErrorThrowable(e))
    }
}
```

## ViewModel

```kotlin
@HiltViewModel
class NewsViewModel @Inject constructor(private val repo: Repository) : ViewModel() {
    private val _newsState: MutableLiveData<Resource<NewsResponse>> =
        MutableLiveData(Resource.loading())
    val newsState: LiveData<Resource<NewsResponse>> = _newsState

    init {
        fetchLatestNews()
    }

    /**
     * Function that fetch latestNews and post it in a mutable LiveData.
     * Collects from Repository and handle diverse States of response.
     * When
     * [Resource.Success] -> Request From Api was Succesfull
     * [Resource.ErrorApi] -> Request from Api had Errors
     * [Resource.ErrorThrowable] -> Catch Exceptions
     */
    fun fetchLatestNews() {
        viewModelScope.launch(Dispatchers.IO) {

            repo.fetchLatestNews()
                .catch { throwable -> println(throwable.message) }
                .collect { resource ->
                    when (resource) {
                        is Resource.Success ->
                            _newsState.postValue(Resource.success(resource.data!!))

                        is Resource.ErrorApi ->
                            _newsState.postValue(Resource.errorApi(resource.errorMessage!!))

                        is Resource.ErrorThrowable ->
                            _newsState.postValue(Resource.errorThrowable(resource.errorThrowable!!))

                        is Resource.Loading ->
                            _newsState.postValue(Resource.loading())

                    }
                }
        }
    }
}
```

## View

En la vista podemos mostrar u ocultar elementos dado el estado de la Respuesta. Por ejemplo, mientras carga podemos mostrar un laoding spinner y esconder el recycler.

```kotlin
@AndroidEntryPoint
class NewsFragment : Fragment() {


    // Properties
    private lateinit var binding: FragmentNewsBinding
    private val newsViewModel: NewsViewModel by activityViewModels()
    private val newsAdapter = NewsItemAdapter()

    // Initialization
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewsBinding.inflate(layoutInflater, container, false)

        //News title in top bar
        val actionBar = (requireActivity() as AppCompatActivity).supportActionBar
        actionBar?.title = getString(R.string.novedades)

 
        initNewsRecyclerview()

        return binding.root
    }

    /**
     * Observes state in VM.
     * when:
     * [Resource.Success] -> Disable Spinner Loading, shows Recycler, submit list from API.
     * [Resource.Loading] -> Disable Recycler and Title, shows Spinner.
     * [Resource.ErrorApi] and [Resource.ErrorThrowable] -> Shows layout for errors. And has a button
     * listener that retry api request for news. If [Resource.Success], the same as before.
     */

    private fun initNewsRecyclerview() {
        newsViewModel.newsState.observe(viewLifecycleOwner) { resourceNews ->

            when (resourceNews) {
                is Resource.Success -> {
                    binding.apply {
                        rvNews.adapter = newsAdapter
                        rvNews.layoutManager = GridLayoutManager(context, 2)
                        newsAdapter.submitList(resourceNews.data?.novedades?.toMutableList())
                        rvNews.visible()
                        progressLoader.root.gone()

                    }
                }
                is Resource.Loading -> {
                    binding.apply {
                        rvNews.gone()
                        progressLoader.root.visible()
                    }
                }

                is Resource.ErrorThrowable ->{
                    binding.apply {
                        progressLoader.root.gone()
                        fragmentNewsTitle.gone()
                        constraintError.root.visible()
                        binding.constraintError.buttonErrorRetry.setOnClickListener {
                            progressLoader.root.visible()
                            newsViewModel.fetchLatestNews()
                            constraintError.root.gone()
                        }
                    }
                }

                is Resource.ErrorApi ->{
                    binding.apply {
                        progressLoader.root.gone()
                        fragmentNewsTitle.gone()
                        constraintError.root.visible()
                        binding.constraintError.buttonErrorRetry.setOnClickListener {
                            progressLoader.root.visible()
                            newsViewModel.fetchLatestNews()
                            constraintError.root.gone()
                        }
                    }
                }
            }

        }
    }
```