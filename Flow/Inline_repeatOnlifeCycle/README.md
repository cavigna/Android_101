

Source:
https://github.com/google/iosched/blob/main/mobile/src/main/java/com/google/samples/apps/iosched/util/UiUtils.kt#L60

```kotlin
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch


//To reduce BiolerPlate.
//Needs to use viewLifecyleOwner 'cause it's a fragment.
inline fun Fragment.launchAndRepeatWithViewLifecycle(
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
    crossinline block: suspend CoroutineScope.() -> Unit
) {
    viewLifecycleOwner.lifecycleScope.launch {
        viewLifecycleOwner.lifecycle.repeatOnLifecycle(minActiveState) {
            block()
        }
    }
}

inline fun <T> Flow<T>.launchAndCollectIn(
    owner: LifecycleOwner,
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
    crossinline action: suspend CoroutineScope.(T) -> Unit
) = owner.lifecycleScope.launch {
    owner.repeatOnLifecycle(minActiveState) {
        collect {
            action(it)
        }
    }
}
```


Después en el Fragmento:

```kotlin
   this.launchAndRepeatWithViewLifecycle {
            viewmodel.uiState.collect { uiState ->
                when (uiState) {
                    is UiState.Success -> {
                        isLoading(false)
                        adapter.submitList(uiState.listado)
                    }
                    is UiState.Loading -> isLoading(true)

                    is UiState.Error -> Snackbar.make(
                        binding.root, uiState.message, Snackbar.LENGTH_LONG
                    ).show()

                }

            }
        }
```
