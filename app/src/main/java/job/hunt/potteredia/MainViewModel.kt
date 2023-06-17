package job.hunt.potteredia

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import job.hunt.potteredia.model.Character
import job.hunt.potteredia.network.ConnectionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val connectionManager: ConnectionManager,
    private val characterRepository: CharacterRepository
) : ViewModel() {

    private val _uiState: MutableStateFlow<MainUiState> = MutableStateFlow(MainUiState.Loading)

    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            characterRepository.characters.collect { charactersResult ->
                Timber.d(charactersResult.toString())
                _uiState.value = if (charactersResult.isSuccess) {
                    handleSuccessResult(charactersResult)
                } else {
                    handleFailureResult(charactersResult)
                }
            }
        }
        viewModelScope.launch {
            connectionManager.isConnected.collect {
                if (_uiState.value is MainUiState.NoArticles) {
                    characterRepository.reload()
                }
            }
        }
    }

    private fun handleSuccessResult(charactersResult: Result<List<Character>>): MainUiState {
        Timber.d("handleSuccessResult")
        val characters = charactersResult.getOrNull()

        return if (characters == null) {
            MainUiState.NoArticles(errorMessage = "Something went wrong!")
        } else {
            MainUiState.HasCharacters(
                characters = characters
                    .sortedBy { character -> character.name }
                    .groupBy { character -> character.name.first() }
            )
        }
    }

    private fun handleFailureResult(charactersResult: Result<List<Character>>): MainUiState {
        Timber.d("handleFailureResult")
        val exception = charactersResult.exceptionOrNull()

        return if (exception is UnInitialized) {
            MainUiState.Loading
        } else {
            MainUiState.NoArticles(errorMessage = mapExceptionToErrorMessage(exception))
        }
    }

    private fun mapExceptionToErrorMessage(exception: Throwable?): String = when {
        exception == null -> "Could not reach the API"
        !connectionManager.isCurrentlyConnected -> "No internet connection."
        else -> "Something went wrong!"
    }
}

sealed class MainUiState {
    object Loading : MainUiState()
    data class NoArticles(val errorMessage: String) : MainUiState()
    data class HasCharacters(val characters: Map<Char, List<Character>>) : MainUiState()
}
