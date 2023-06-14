package job.hunt.potteredia

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.hilt.android.AndroidEntryPoint
import job.hunt.potteredia.model.Character
import job.hunt.potteredia.model.Wand
import job.hunt.potteredia.network.HarryPotterApi
import job.hunt.potteredia.ui.theme.PotterpeidaTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import retrofit2.Retrofit

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val characters: MutableStateFlow<List<Character>> = MutableStateFlow(emptyList())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PotterpeidaTheme {
                val chars by characters.collectAsState(initial = emptyList())
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    LazyColumn {
                        items(chars) { character ->
                            CharacterCard(character)
                        }
                    }
                }
            }
        }
        lifecycleScope.launchWhenCreated {
            characters.value = getAllCharacters()
        }
    }

    private suspend fun getAllCharacters(): List<Character> = withContext(Dispatchers.IO) {
        val retrofit =
            Retrofit.Builder().baseUrl("https://hp-api.onrender.com/").addConverterFactory(
                Json.asConverterFactory(MediaType.parse("application/json")!!)
            ).build()

        val harryPotterApi = retrofit.create(HarryPotterApi::class.java)

        val call = harryPotterApi.getAllCharacters().execute()

        if (call.isSuccessful) {
            call.body() ?: emptyList()
        } else {
            Log.d("Call", "Failed")
            emptyList()
        }
    }
}

@Composable
fun CharacterCard(character: Character) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = CardDefaults.elevatedCardElevation()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text("Name:${character.name}")
            Text("Species:${character.species}")
            Text("House:${character.house}")
            if (character.ancestry.isNotEmpty()) {
                Text("Ancestry:${character.ancestry}")
            }
            Text("Actor:${character.actor}")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CharactersPreview() {
    PotterpeidaTheme {
        CharacterCard(
            character = Character(
                id = "42",
                name = "John Doe",
                alternateNames = emptyList(),
                species = "human",
                gender = "male",
                house = "Gryffindor",
                dateOfBirth = "01-01-1970",
                yearOfBirth = 1970,
                wizard = true,
                ancestry = "pure-blood",
                eyeColour = "yellow",
                hairColour = "brown",
                wand = Wand(
                    wood = "oak",
                    core = "core",
                    length = 7.8f
                ),
                patronus = "dog",
                hogwartsStudent = true,
                hogwartsStaff = false,
                actor = "John Doe",
                alternateActors = emptyList(),
                alive = true,
                image = ""
            )
        )
    }
}
