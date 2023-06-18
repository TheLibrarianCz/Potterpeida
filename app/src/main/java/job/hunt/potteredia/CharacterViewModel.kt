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
    private val characterRepository: CharacterRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val characterId: String = checkNotNull(savedStateHandle["characterId"])

    private val _characterUiState: MutableStateFlow<CharacterUiState> =
        MutableStateFlow(CharacterUiState.Loading)
    val characterUiState: StateFlow<CharacterUiState> = _characterUiState.asStateFlow()

    val characterName: String = checkNotNull(savedStateHandle["characterName"])

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val char = characterRepository.fetchCharacterData(characterId)
            _characterUiState.value = CharacterUiState.Loaded(char)
        }
    }
}

sealed class CharacterUiState {

    object Loading : CharacterUiState()
    data class Loaded(val character: Character) : CharacterUiState()
}
