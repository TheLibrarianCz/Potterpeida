package job.hunt.potteredia.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Character(
    val id: String,
    val name: String,
    @SerialName("alternate_names")
    val alternateNames: List<String>,
    val species: String,
    val gender: String,
    val house: String,
    val dateOfBirth: String?,
    val yearOfBirth: Int?,
    val wizard: Boolean,
    val ancestry: String,
    val eyeColour: String,
    val hairColour: String,
    val wand: Wand,
    val patronus: String,
    val hogwartsStudent: Boolean,
    val hogwartsStaff: Boolean,
    val actor: String,
    @SerialName("alternate_actors")
    val alternateActors: List<String>,
    val alive: Boolean,
    val image: String
)

@Serializable
data class Wand(
    val wood: String,
    val core: String,
    val length: Float?
)

enum class Species {
    Human,
    HalfGiant,
    Werewolf,
    Cat,
    Goblin,
    Owl,
    Ghost,
    Poltergeist,
    ThreeHeadedDog,
    Dragon,
    Centaur,
    HouseElf,
    Acromantula,
    Hippogriff,
    Giant,
    Vampire,
    HalfHuman
}

enum class Gender {
    Male,
    Female
}

enum class House {
    Gryffindor,
    Slytherin,
    Hufflepuff,
    Ravenclaw,
    None
}

enum class Ancestry {
    HalfBlood,
    Muggleborn,
    PureBlood,
    Squib,
    Muggle,
    HalfVeela,
    QuarterVeela
}

enum class Patronus {
    Stag,
    Otter,
    JackRussellTerrier,
    TabbyCat,
    Swan,
    Doe,
    NonCorporeal,
    Hare,
    Horse,
    Wolf,
    Weasel,
    Lynx,
    PersianCat,
    Boar,
    Goat
}
