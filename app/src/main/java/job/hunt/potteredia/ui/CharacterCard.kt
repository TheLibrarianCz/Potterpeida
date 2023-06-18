package job.hunt.potteredia.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import job.hunt.potteredia.houseColors
import job.hunt.potteredia.model.Character
import job.hunt.potteredia.model.Wand
import job.hunt.potteredia.speciesToIconMap
import job.hunt.potteredia.ui.theme.PotterpeidaTheme

@Composable
fun CharacterCard(
    character: Character,
    onCharacterClick: (Pair<String, String>) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable { onCharacterClick(character.id to character.name) },
        elevation = CardDefaults.elevatedCardElevation()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CharacterIcon(character)
            CharacterText(character)
        }
    }
}

@Composable
fun CharacterIcon(character: Character) {
    Icon(
        modifier = Modifier
            .width(64.dp)
            .height(64.dp)
            .padding(4.dp),
        painter = painterResource(id = speciesToIconMap(character)),
        tint = houseColors(character) ?: MaterialTheme.colorScheme.onSurface,
        contentDescription = null
    )
}

@Composable
fun CharacterText(character: Character) {
    Column {
        Text(
            text = character.name,
            fontWeight = FontWeight.Bold,
            fontStyle = MaterialTheme.typography.headlineSmall.fontStyle,
            fontSize = MaterialTheme.typography.headlineSmall.fontSize
        )
        if (character.alternateNames.isNotEmpty()) {
            Text(
                text = character.alternateNames.first(),
                fontStyle = FontStyle.Italic
            )
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
                alternateNames = listOf("Don Jon"),
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
