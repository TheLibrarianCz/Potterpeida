@file:OptIn(ExperimentalMaterial3Api::class)

package job.hunt.potteredia.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults.enterAlwaysScrollBehavior
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.TopAppBarState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import job.hunt.potteredia.MainUiState
import job.hunt.potteredia.MainViewModel
import job.hunt.potteredia.R

@Composable
fun MainScreen(
    navController: NavController,
    mainViewModel: MainViewModel
) {
    val uiState by mainViewModel.uiState.collectAsState()

    MainScreen(navController = navController, uiState = uiState)
}

@Composable
fun MainScreen(
    navController: NavController,
    uiState: MainUiState
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val onCharacterClick: (String) -> Unit = { characterId ->
        navController.navigate(
            PotterpediaDestinations.CHARACTER_ROUTE.replace(
                oldValue = "{characterId}",
                newValue = characterId
            )
        )
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(title = stringResource(id = R.string.app_name))
        }
    ) { padding ->
        Surface(
            modifier = Modifier.fillMaxSize().padding(padding),
            color = MaterialTheme.colorScheme.background
        ) {
            MainScreenUiState(uiState, snackbarHostState, onCharacterClick)
        }
    }
}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun MainScreenUiState(
    uiState: MainUiState,
    snackbarHostState: SnackbarHostState,
    onCharacterClick: (String) -> Unit
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
        Text("Could not reach the API")
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
    onCharacterClick: (String) -> Unit
) {
    LazyColumn {
        items(uiState.characters) { character ->
            CharacterCard(character, onCharacterClick)
        }
    }
}

@Composable
fun TopAppBar(
    title: String,
    modifier: Modifier = Modifier,
    topAppBarState: TopAppBarState = rememberTopAppBarState(),
    scrollBehavior: TopAppBarScrollBehavior? = enterAlwaysScrollBehavior(topAppBarState)
) {
    CenterAlignedTopAppBar(
        title = { Text(text = title) },
        navigationIcon = { },
        scrollBehavior = scrollBehavior,
        modifier = modifier
    )
}
