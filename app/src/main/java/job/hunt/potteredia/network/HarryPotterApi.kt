package job.hunt.potteredia.network

import job.hunt.potteredia.model.Character
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface HarryPotterApi {

    @GET("api/characters")
    suspend fun getAllCharacters(): Response<List<Character>>

    @GET("api/character/{id}")
    suspend fun getCharacter(@Path("id") id: String): Response<Character>

    @GET("api/characters/students")
    suspend fun getAllStudents(): Response<String>

    @GET("api/characters/staff")
    suspend fun getAllStaff(): Response<String>

    @GET("api/characters/house/{house}")
    suspend fun getHouse(@Path("house") house: String): Response<String>

    @GET("api/spells")
    suspend fun getAllSpells(): Response<String>
}
