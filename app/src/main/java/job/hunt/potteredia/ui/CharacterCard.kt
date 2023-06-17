package job.hunt.potteredia.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import job.hunt.potteredia.model.Character
import job.hunt.potteredia.model.Wand
import job.hunt.potteredia.ui.theme.PotterpeidaTheme

@Composable
fun CharacterCard(
    character: Character,
    onCharacterClick: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable { onCharacterClick(character.id) },
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
fun CharacterCardPreview() {
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
            ),
            onCharacterClick = {}
        )
    }
}
