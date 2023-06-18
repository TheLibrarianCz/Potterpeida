package job.hunt.potteredia.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import job.hunt.potteredia.ui.theme.PotterpeidaTheme

@Composable
fun PotterpediaApp() {
    PotterpeidaTheme {
        PotterpediaNavGraph()
    }
}

@Composable
fun PotterpediaNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = PotterpediaDestinations.HOME_ROUTE
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination
    ) {
        composable(route = PotterpediaDestinations.HOME_ROUTE) { backStackEntry ->
            MainScreen(
                mainViewModel = hiltViewModel(),
                navController = navController
            )
        }

        composable(
            route = PotterpediaDestinations.CHARACTER_ROUTE,
            arguments = listOf(
                navArgument("characterId") { type = NavType.StringType },
                navArgument("characterName") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            CharacterScreen(
                characterViewModel = hiltViewModel(),
                navController = navController
            )
        }
    }
}

object PotterpediaDestinations {
    const val HOME_ROUTE = "home"
    const val CHARACTER_ROUTE = "character/{characterId}?characterName={characterName}"
}
