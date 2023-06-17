package job.hunt.potteredia

import job.hunt.potteredia.di.ApplicationScope
import job.hunt.potteredia.di.IoDispatcher
import job.hunt.potteredia.model.Character
import job.hunt.potteredia.network.HarryPotterApiClient
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CharacterRepository @Inject constructor(
    @ApplicationScope private val applicationScope: CoroutineScope,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val harryPotterApiClient: HarryPotterApiClient
) {
    private val _characters: MutableStateFlow<Result<List<Character>>> = MutableStateFlow(Result.failure(UnInitialized()))

    val characters: Flow<Result<List<Character>>> = _characters.asStateFlow()

    init {
        load()
    }

    fun reload() {
        _characters.value = Result.failure(UnInitialized())

        load()
    }

    private fun load() {
        applicationScope.launch(ioDispatcher) {
            _characters.value = harryPotterApiClient.getAllCharacters()
        }
    }
}

class UnInitialized : Exception()
