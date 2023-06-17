@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)

package job.hunt.potteredia.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import job.hunt.potteredia.MainUiState
import job.hunt.potteredia.MainViewModel
import job.hunt.potteredia.R
import job.hunt.potteredia.model.Character
import job.hunt.potteredia.model.Wand

@Composable
fun MainScreen(
    navController: NavController,
    mainViewModel: MainViewModel
) {
    val uiState by mainViewModel.uiState.collectAsState()

    val onCharacterClick: (Pair<String, String>) -> Unit = { (characterId, characterName) ->
        navController.navigate(
            route = PotterpediaDestinations.CHARACTER_ROUTE
                .replace(
                    oldValue = "{characterId}",
                    newValue = characterId
                )
                .replace(
                    oldValue = "{characterName}",
                    newValue = characterName
                )
        )
    }

    MainScreen(uiState = uiState, onCharacterClick = onCharacterClick)
}

@Composable
fun MainScreen(
    uiState: MainUiState,
    onCharacterClick: (Pair<String, String>) -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = { TopAppBar(title = stringResource(id = R.string.app_name)) }
    ) { padding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            color = MaterialTheme.colorScheme.background
        ) {
            MainScreenUiState(uiState, snackbarHostState, onCharacterClick)
        }
    }
}

@Composable
fun MainScreenUiState(
    uiState: MainUiState,
    snackbarHostState: SnackbarHostState,
    onCharacterClick: (Pair<String, String>) -> Unit
) {
    when (uiState) {
        is MainUiState.NoArticles -> MainScreenNoArticles(uiState, snackbarHostState)
        is MainUiState.HasCharacters -> MainScreenHasCharacters(uiState, onCharacterClick)
        MainUiState.Loading -> MainScreenLoading()
    }
}

@Composable
fun MainScreenNoArticles(
    uiState: MainUiState.NoArticles,
    snackbarHostState: SnackbarHostState
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = stringResource(id = R.string.network_error_message))
        LaunchedEffect(snackbarHostState) {
            snackbarHostState.showSnackbar(
                message = uiState.errorMessage,
                duration = SnackbarDuration.Indefinite
            )
        }
    }
}

@Composable
fun MainScreenLoading() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun MainScreenHasCharacters(
    uiState: MainUiState.HasCharacters,
    onCharacterClick: (Pair<String, String>) -> Unit
) {
    LazyColumn {
        uiState.characters.forEach { (initial, contactsForInitial) ->
            stickyHeader {
                HeaderRow(initial)
            }
            items(contactsForInitial) { character ->
                CharacterCard(character, onCharacterClick)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMainScreenHasCharacters() {
    MainScreenHasCharacters(
        uiState = MainUiState.HasCharacters(
            characters = mapOf(
                'J' to listOf(
                    Character(
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
                ),
                'K' to listOf(
                    Character(
                        id = "42",
                        name = "Kohn Doe",
                        alternateNames = listOf("Don Kon"),
                        species = "human",
                        gender = "male",
                        house = "Hufflepuff",
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
            )
        ),
        onCharacterClick = {}
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewMainScreenLoading() {
    MainScreenLoading()
}

@Preview(showBackground = true)
@Composable
fun PreviewMainScreenNoArticles() {
    val snackbarHostState = remember { SnackbarHostState() }
    MainScreenNoArticles(
        uiState = MainUiState.NoArticles(errorMessage = "Out of mana"),
        snackbarHostState = snackbarHostState
    )
}
