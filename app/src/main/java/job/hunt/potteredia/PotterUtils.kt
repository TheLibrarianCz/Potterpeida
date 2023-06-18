package job.hunt.potteredia

import androidx.compose.ui.graphics.Color
import job.hunt.potteredia.model.Character
import job.hunt.potteredia.ui.theme.GryffindorRed
import job.hunt.potteredia.ui.theme.HufflepuffYellow
import job.hunt.potteredia.ui.theme.RavenclawBlue
import job.hunt.potteredia.ui.theme.SlytherinGreen

fun houseColors(character: Character): Color? {
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

fun speciesToIconMap(character: Character): Int {
    return when (character.species) {
        "half-human",
        "human" -> humanToCrest(character)

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

fun humanToCrest(character: Character): Int {
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
