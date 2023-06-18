package job.hunt.potteredia.storage

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import job.hunt.potteredia.model.Character
import kotlinx.coroutines.flow.Flow

@Dao
interface CharacterDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(characters: List<Character>)

    @Query("SELECT * FROM Character")
    fun observableAllCharacters(): Flow<List<Character>>

    @Query("SELECT * FROM Character")
    suspend fun getAllCharacters(): List<Character>

    @Query("SELECT * FROM Character WHERE id=:characterId")
    fun observableCharacter(characterId: String): Flow<Character?>

    @Query("SELECT * FROM Character WHERE id=:characterId")
    suspend fun getCharacter(characterId: String): Character?
}
