@file:OptIn(ExperimentalMaterial3Api::class)

package job.hunt.potteredia.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
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
            modifier = Modifier.fillMaxSize().padding(paddingValues),
            color = MaterialTheme.colorScheme.background
        ) {
            CharacterScreenUiState(uiState = characterUiState)
        }
    }
}

@Composable
fun CharacterScreenUiState(uiState: CharacterUiState) {
    when (uiState) {
        is CharacterUiState.NoInternet -> CharacterScreenUiNoInternet(uiState)
        is CharacterUiState.Loading -> CharacterScreenUiLoading(uiState)
    }
}

@Composable
fun CharacterScreenUiLoading(uiState: CharacterUiState.Loading) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun CharacterScreenUiNoInternet(uiState: CharacterUiState.NoInternet) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
    }
}
