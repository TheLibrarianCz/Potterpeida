@file:OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalFoundationApi::class,
    ExperimentalFoundationApi::class
)

package job.hunt.potteredia.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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

    MainScreen(
        uiState = uiState,
        onCheckChange = mainViewModel::onFilterChange,
        onCharacterClick = onCharacterClick
    )
}

@Composable
fun MainScreen(
    uiState: MainUiState,
    onCheckChange: (Boolean) -> Unit,
    onCharacterClick: (Pair<String, String>) -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = stringResource(id = R.string.app_name),
                actions = { TopAppBarActions(uiState, onCheckChange) }
            )
        }
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
        is MainUiState.Error -> MainScreenError()
        is MainUiState.NoInternet -> MainScreenNoInternet(snackbarHostState)
        is MainUiState.HasCharacters -> MainScreenHasCharacters(uiState, onCharacterClick)
        MainUiState.Loading -> MainScreenLoading()
    }
}

@Composable
fun MainScreenError() {
    ErrorCommon(
        infoMessage = stringResource(id = R.string.something_went_wrong)
    )
}

@Composable
fun MainScreenNoInternet(
    snackbarHostState: SnackbarHostState
) {
    ErrorCommon(
        infoMessage = stringResource(id = R.string.network_error_message),
        errorMessage = stringResource(id = R.string.no_internet_error_message),
        snackbarHostState = snackbarHostState
    )
}

@Composable
fun MainScreenLoading() {
    LoadingCommon()
}

@Composable
fun MainScreenHasCharacters(
    uiState: MainUiState.HasCharacters,
    onCharacterClick: (Pair<String, String>) -> Unit
) {
    Column {
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
}

@Composable
private fun TopAppBarActions(
    uiState: MainUiState,
    onCheckChange: (Boolean) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    if (uiState is MainUiState.HasCharacters) {
        IconButton(onClick = { expanded = !expanded }) {
            Icon(
                imageVector = Icons.Filled.MoreVert,
                tint = MaterialTheme.colorScheme.onPrimary,
                contentDescription = null
            )
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                DropdownMenuItem(
                    trailingIcon = {
                        Checkbox(checked = uiState.photoOnly, onCheckedChange = onCheckChange)
                    },
                    text = { Text("Image only characters") },
                    onClick = { onCheckChange(!uiState.photoOnly) }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMainScreenHasCharacters() {
    MainScreenHasCharacters(
        uiState = MainUiState.HasCharacters(
            photoOnly = false,
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
    MainScreenNoInternet(
        snackbarHostState = snackbarHostState
    )
}
