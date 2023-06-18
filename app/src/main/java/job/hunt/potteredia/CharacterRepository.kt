package job.hunt.potteredia

import job.hunt.potteredia.di.ApplicationScope
import job.hunt.potteredia.model.Character
import job.hunt.potteredia.network.HarryPotterApiClient
import job.hunt.potteredia.storage.PotterpediaRoom
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CharacterRepository @Inject constructor(
    @ApplicationScope private val applicationScope: CoroutineScope,
    private val potterpediaRoom: PotterpediaRoom,
    private val harryPotterApiClient: HarryPotterApiClient
) {
    private val _characters: MutableStateFlow<Result<List<Character>>> =
        MutableStateFlow(Result.failure(UnInitialized()))

    val characters: Flow<Result<List<Character>>> = _characters.asStateFlow()

    init {
        load()
    }

    fun reload() {
        _characters.value = Result.failure(UnInitialized())

        load()
    }

    private fun load() {
        applicationScope.launch(Dispatchers.IO) {
            val previouslyStored = potterpediaRoom.characterDao().getAllCharacters()

            if (previouslyStored.isNotEmpty()) {
                _characters.value = Result.success(previouslyStored)
            } else {
                val fetchResult = harryPotterApiClient.getAllCharacters()
                storeCharacters(fetchResult)
                _characters.value = fetchResult
            }
        }
    }

    private fun storeCharacters(result: Result<List<Character>>) {
        if (result.isSuccess) {
            result.getOrNull()?.let { characters ->
                applicationScope.launch(Dispatchers.IO) {
                    potterpediaRoom.characterDao().insert(characters)
                }
            }
        }
    }

    suspend fun fetchCharacterData(characterId: String): Result<Character> =
        withContext(Dispatchers.IO) {
            val characterData = potterpediaRoom.characterDao().getCharacter(characterId)
            if (characterData != null) {
                Result.success(characterData)
            } else {
                val networkCharacterFetch = harryPotterApiClient.getCharacter(characterId)
                storeCharacter(networkCharacterFetch)
                networkCharacterFetch
            }
        }

    private suspend fun storeCharacter(fetchResult: Result<Character>): Unit =
        withContext(Dispatchers.IO) {
            if (fetchResult.isSuccess) {
                fetchResult.getOrNull()?.let { character ->
                    potterpediaRoom.characterDao().insert(listOf(character))
                }
            }
        }
}

class UnInitialized : Exception()
