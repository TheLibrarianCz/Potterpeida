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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import job.hunt.potteredia.R
import job.hunt.potteredia.model.Character
import job.hunt.potteredia.model.Wand
import job.hunt.potteredia.ui.theme.GryffindorRed
import job.hunt.potteredia.ui.theme.HufflepuffYellow
import job.hunt.potteredia.ui.theme.PotterpeidaTheme
import job.hunt.potteredia.ui.theme.RavenclawBlue
import job.hunt.potteredia.ui.theme.SlytherinGreen

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
        painter = painterResource(id = SpeciesToIconMap(character)),
        tint = HouseColors(character) ?: MaterialTheme.colorScheme.onSurface,
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

fun HouseColors(character: Character): Color? {
    return if (character.wizard) {
        when (character.house) {
            "Gryffindor" -> GryffindorRed
            "Slytherin" -> SlytherinGreen
            "Hufflepuff" -> HufflepuffYellow
            "Ravenclaw" -> RavenclawBlue
            else -> null
        }
    } else {
        null
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

fun SpeciesToIconMap(character: Character): Int {
    return when (character.species) {
        "half-human",
        "human" -> HumanToCrest(character)
        "half-giant" -> R.drawable.ic_half_giant
        "werewolf" -> R.drawable.ic_werewolf
        "cat" -> R.drawable.ic_cat
        "goblin" -> R.drawable.ic_goblin
        "owl" -> R.drawable.ic_owl
        "ghost" -> R.drawable.ic_ghost
        "poltergeist" -> R.drawable.ic_poltergeist
        "three-headed dog" -> R.drawable.ic_cerberus
        "dragon" -> R.drawable.ic_dragon
        "centaur" -> R.drawable.ic_centaur
        "house-elf" -> R.drawable.ic_house_elf
        "acromantula" -> R.drawable.ic_spider
        "hippogriff" -> R.drawable.ic_hippogriff
        "giant" -> R.drawable.ic_giant
        "vampire" -> R.drawable.ic_vampire
        else -> R.drawable.ic_vampire
    }
}

fun HumanToCrest(character: Character): Int {
    return if (character.wizard) {
        when (character.house) {
            "Gryffindor" -> R.drawable.ic_gryffindor_crest
            "Slytherin" -> R.drawable.ic_slytherin_crest
            "Hufflepuff" -> R.drawable.ic_hufflepuff_crest
            "Ravenclaw" -> R.drawable.ic_ravenclaw_crest
            else -> R.drawable.ic_human
        }
    } else {
        R.drawable.ic_human
    }
}
