package job.hunt.potteredia.network

import job.hunt.potteredia.model.Character
import retrofit2.Call
import retrofit2.http.GET

interface HarryPotterApi {

    @GET("api/characters")
    fun getAllCharacters(): Call<List<Character>>
}
