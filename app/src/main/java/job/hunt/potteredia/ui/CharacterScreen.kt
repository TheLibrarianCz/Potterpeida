package job.hunt.potteredia.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import job.hunt.potteredia.CharacterUiState
import job.hunt.potteredia.CharacterViewModel

@Composable
fun CharacterScreen(
    characterViewModel: CharacterViewModel,
    navController: NavController
) {
    val characterUiState by characterViewModel.characterUiState.collectAsState()
    Scaffold { paddingValues ->
        Surface(
            modifier = Modifier.fillMaxSize().padding(paddingValues),
            color = MaterialTheme.colorScheme.background
        ) {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                CharacterScreenUiState(uiState = characterUiState)
            }
        }
    }
}

@Composable
fun CharacterScreenUiState(uiState: CharacterUiState) {
    when (uiState) {
        is CharacterUiState.UnderConstruction -> CharacterScreenUnderConstruction(uiState)
    }
}

@Composable
fun CharacterScreenUnderConstruction(uiState: CharacterUiState.UnderConstruction) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Text(text = "Passed ID:${uiState.passedId}")
    }
}
