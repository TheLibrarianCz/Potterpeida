package job.hunt.potteredia.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import job.hunt.potteredia.CharacterUiState
import job.hunt.potteredia.CharacterViewModel
import job.hunt.potteredia.R
import job.hunt.potteredia.houseColors
import job.hunt.potteredia.model.Character
import job.hunt.potteredia.model.Wand

@Composable
fun CharacterScreen(
    characterViewModel: CharacterViewModel,
    navController: NavController
) {
    val characterUiState by characterViewModel.characterUiState.collectAsState()
    val onUpClick: () -> Unit = { navController.navigateUp() }
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = characterViewModel.characterName,
                isPrimaryBackground = false,
                isTopDestination = false,
                onNavigationIconClick = onUpClick
            )
        }
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            color = MaterialTheme.colorScheme.background
        ) {
            CharacterScreenUiState(uiState = characterUiState, snackbarHostState = snackbarHostState)
        }
    }
}

@Composable
fun CharacterScreenUiState(
    uiState: CharacterUiState,
    snackbarHostState: SnackbarHostState
) {
    when (uiState) {
        is CharacterUiState.Loading -> CharacterScreenUiLoading()
        is CharacterUiState.Loaded -> CharacterScreenUiLoaded(uiState.character)
        CharacterUiState.Error -> CharacterScreenError()
        is CharacterUiState.NoInternet -> CharacterScreenUiLoadedNoInternet(uiState, snackbarHostState)
    }
}

@Composable
fun CharacterScreenError() {
    ErrorCommon(infoMessage = stringResource(id = R.string.something_went_wrong))
}

@Composable
fun CharacterScreenUiLoading() {
    LoadingCommon()
}

@Composable
fun CharacterScreenUiLoaded(character: Character) {
    Column {
        if (character.alternateNames.isNotEmpty()) {
            val altName = character.alternateNames.first()
            Text(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                text = stringResource(id = R.string.also_known_as)
            )
            Text(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                text = altName,
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.Bold
            )
        }
        CharacterRow(character = character)
    }
}

@Composable
fun CharacterScreenUiLoadedNoInternet(
    uiState: CharacterUiState.NoInternet,
    snackbarHostState: SnackbarHostState
) {
    Column(modifier = Modifier.fillMaxSize()) {
        CharacterScreenUiLoaded(uiState.character)

        val errorMessage = stringResource(id = R.string.no_internet_error_message)

        LaunchedEffect(snackbarHostState) {
            snackbarHostState.showSnackbar(
                message = errorMessage,
                duration = SnackbarDuration.Short
            )
        }
    }
}

@Composable
private fun CharacterRow(character: Character) {
    Row(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxWidth()
    ) {
        CharacterImage(
            imageUrl = character.image,
            borderColor = houseColors(character) ?: MaterialTheme.colorScheme.primary,
            width = 128,
            height = 128
        )
        CharacterDescription(
            modifier = Modifier.align(Alignment.CenterVertically),
            character = character
        )
    }
}

@Composable
private fun CharacterDescription(
    modifier: Modifier,
    character: Character
) {
    Column(modifier = modifier) {
        if (character.actor.isNotBlank()) {
            Text(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                text = stringResource(id = R.string.portrayed_by),
                fontStyle = FontStyle.Italic
            )
            Text(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                text = character.actor,
                fontWeight = FontWeight.Bold,
                fontStyle = MaterialTheme.typography.headlineMedium.fontStyle,
                fontSize = MaterialTheme.typography.headlineMedium.fontSize
            )
        }
    }
}

@Composable
private fun CharacterImage(
    imageUrl: String,
    borderColor: Color,
    width: Int,
    height: Int
) {
    Box(
        modifier = Modifier.padding(16.dp)
    ) {
        ProgressImageLoader(
            imageUrl = imageUrl,
            modifier = Modifier
                .width(width.dp)
                .height(height.dp)
                .clip(CircleShape)
                .border(4.dp, borderColor, CircleShape),
            contentDescription = null
        )
    }
}

@Preview
@Composable
fun PreviewCharacterScreen() {
    CharacterScreenUiLoaded(
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
        )
    )
}

@Preview
@Composable
fun PreviewCharacterError() {
    ErrorCommon(infoMessage = stringResource(id = R.string.something_went_wrong))
}

@Preview
@Composable
fun PreviewCharacterRow() {
    CharacterRow(
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
        )
    )
}
