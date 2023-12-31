package job.hunt.potteredia

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import job.hunt.potteredia.model.Character
import job.hunt.potteredia.network.ConnectionManager
import job.hunt.potteredia.network.NoInternetException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val connectionManager: ConnectionManager,
    private val characterRepository: CharacterRepository
) : ViewModel() {

    private val filterPhotoOnly: MutableStateFlow<Boolean> = MutableStateFlow(true)

    private val _uiState: MutableStateFlow<MainUiState> = MutableStateFlow(MainUiState.Loading)

    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            characterRepository.characters
                .combine(filterPhotoOnly) { result: Result<List<Character>>, filter: Boolean ->
                    result to filter
                }.collect { (charactersResult: Result<List<Character>>, filter: Boolean) ->

                    _uiState.value = if (charactersResult.isSuccess) {
                        handleSuccessResult(charactersResult, filter)
                    } else {
                        handleFailureResult(charactersResult)
                    }
                }
        }
        viewModelScope.launch {
            connectionManager.isConnected.collect { connected ->
                if (_uiState.value is MainUiState.NoInternet && connected) {
                    characterRepository.reload()
                }
            }
        }
    }

    private fun handleSuccessResult(
        charactersResult: Result<List<Character>>,
        filter: Boolean
    ): MainUiState {
        Timber.d("handleSuccessResult")
        val characters = charactersResult.getOrNull()

        return if (characters == null) {
            MainUiState.Error
        } else {
            val filtered = if (filter) {
                characters.filter { it.image.isNotBlank() }
            } else {
                characters
            }

            MainUiState.HasCharacters(
                photoOnly = filter,
                characters = filtered
                    .sortedBy { character -> character.name }
                    .groupBy { character -> character.name.first() }
            )
        }
    }

    private fun handleFailureResult(charactersResult: Result<List<Character>>): MainUiState {
        Timber.d("handleFailureResult")
        return when (charactersResult.exceptionOrNull()) {
            is UnInitialized -> MainUiState.Loading
            is NoInternetException -> MainUiState.NoInternet
            else -> MainUiState.Error
        }
    }

    fun onFilterChange(newValue: Boolean) {
        filterPhotoOnly.value = newValue
    }
}

sealed class MainUiState {
    object Loading : MainUiState()
    object NoInternet : MainUiState()
    object Error : MainUiState()
    data class HasCharacters(
        val photoOnly: Boolean,
        val characters: Map<Char, List<Character>>
    ) : MainUiState()
}
