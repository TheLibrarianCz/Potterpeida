package job.hunt.potteredia

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import job.hunt.potteredia.network.ConnectionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class CharacterViewModel @Inject constructor(
    private val connectionManager: ConnectionManager,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val characterId: String = checkNotNull(savedStateHandle["characterId"])

    private val _characterUiState: MutableStateFlow<CharacterUiState> =
        MutableStateFlow(CharacterUiState.UnderConstruction(characterId))
    val characterUiState: StateFlow<CharacterUiState> = _characterUiState.asStateFlow()

    init {
        Timber.d("job.hunt.potterpedia", "Character ID: $characterId")
    }
}

sealed class CharacterUiState {
    data class UnderConstruction(val passedId: String) : CharacterUiState()
}
