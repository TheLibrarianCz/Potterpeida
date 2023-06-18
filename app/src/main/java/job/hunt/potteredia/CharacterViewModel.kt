package job.hunt.potteredia

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import job.hunt.potteredia.model.Character
import job.hunt.potteredia.network.ConnectionManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CharacterViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val connectionManager: ConnectionManager,
    private val characterRepository: CharacterRepository
) : ViewModel() {

    private val characterId: String = checkNotNull(savedStateHandle["characterId"])

    private val _characterUiState: MutableStateFlow<CharacterUiState> =
        MutableStateFlow(CharacterUiState.Loading)
    val characterUiState: StateFlow<CharacterUiState> = _characterUiState.asStateFlow()

    val characterName: String = checkNotNull(savedStateHandle["characterName"])

    init {
        load()

        viewModelScope.launch {
            connectionManager.isConnected.collect { connected ->
                if (_characterUiState.value is CharacterUiState.NoInternet && connected) {
                    load()
                }
            }
        }
    }

    private fun load() {
        viewModelScope.launch(Dispatchers.IO) {
            val fetchResult = characterRepository.fetchCharacterData(characterId)

            _characterUiState.value = translateFetchResult(fetchResult)
        }
    }

    private fun translateFetchResult(fetchResult: Result<Character>): CharacterUiState {
        return if (fetchResult.isSuccess) {
            val data = fetchResult.getOrNull()

            if (data != null) {
                if (connectionManager.isCurrentlyConnected) {
                    CharacterUiState.Loaded(data)
                } else {
                    CharacterUiState.NoInternet(data)
                }
            } else {
                CharacterUiState.Error
            }
        } else {
            CharacterUiState.Error
        }
    }
}

sealed class CharacterUiState {

    object Loading : CharacterUiState()

    data class Loaded(val character: Character) : CharacterUiState()

    object Error : CharacterUiState()

    data class NoInternet(val character: Character) : CharacterUiState()
}
