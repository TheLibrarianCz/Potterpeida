package job.hunt.potteredia

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import job.hunt.potteredia.model.Character
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CharacterViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val characterRepository: CharacterRepository
) : ViewModel() {

    private val characterId: String = checkNotNull(savedStateHandle["characterId"])

    private val _characterUiState: MutableStateFlow<CharacterUiState> =
        MutableStateFlow(CharacterUiState.Loading)
    val characterUiState: StateFlow<CharacterUiState> = _characterUiState.asStateFlow()

    val characterName: String = checkNotNull(savedStateHandle["characterName"])

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val fetchResult = characterRepository.fetchCharacterData(characterId)

            _characterUiState.value = translateFetchResult(fetchResult)
        }
    }

    private fun translateFetchResult(fetchResult: Result<Character>): CharacterUiState {
        return if (fetchResult.isSuccess) {
            val data = fetchResult.getOrNull()

            if (data != null) {
                CharacterUiState.Loaded(data)
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
}
