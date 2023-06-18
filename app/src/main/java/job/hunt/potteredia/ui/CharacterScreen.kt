package job.hunt.potteredia.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import job.hunt.potteredia.CharacterUiState
import job.hunt.potteredia.CharacterViewModel
import job.hunt.potteredia.R
import job.hunt.potteredia.houseColors
import job.hunt.potteredia.model.Character

@Composable
fun CharacterScreen(
    characterViewModel: CharacterViewModel,
    navController: NavController
) {
    val characterUiState by characterViewModel.characterUiState.collectAsState()
    val onUpClick: () -> Unit = { navController.navigateUp() }

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
            CharacterScreenUiState(uiState = characterUiState)
        }
    }
}

@Composable
fun CharacterScreenUiState(uiState: CharacterUiState) {
    when (uiState) {
        is CharacterUiState.Loading -> CharacterScreenUiLoading()
        is CharacterUiState.Loaded -> CharacterScreenUiLoaded(uiState.character)
        CharacterUiState.Error -> CharacterScreenError()
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
private fun CharacterRow(character: Character) {
    Row(
        modifier = Modifier.background(MaterialTheme.colorScheme.background)
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
