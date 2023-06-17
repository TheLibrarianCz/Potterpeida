package job.hunt.potteredia.network

import job.hunt.potteredia.model.Character
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HarryPotterApiClient @Inject constructor(private val harryPotterApi: HarryPotterApi) {

    suspend fun getAllCharacters(): Result<List<Character>> = withContext(Dispatchers.IO) {
        try {
            // Check internet here, if not then -> NoInternetException
            val allCharactersCall = harryPotterApi.getAllCharacters().body()

            if (allCharactersCall == null) {
                Result.failure(NoContentException())
            } else {
                Result.success(allCharactersCall)
            }
        } catch (e: IOException) {
            Result.failure(e)
        }
    }

    suspend fun getCharacter(id: String): Result<String> {
        TODO("Not yet implemented")
    }

    suspend fun getAllStudents(): Result<String> {
        TODO("Not yet implemented")
    }

    suspend fun getAllStaff(): Result<String> {
        TODO("Not yet implemented")
    }

    suspend fun getHouse(house: String): Result<String> {
        TODO("Not yet implemented")
    }

    suspend fun getAllSpells(): Result<String> {
        TODO("Not yet implemented")
    }
}

class NoInternetException : IOException()
class NoContentException : IOException()