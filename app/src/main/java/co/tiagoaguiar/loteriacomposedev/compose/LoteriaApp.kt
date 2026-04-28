package co.tiagoaguiar.loteriacomposedev.compose

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import co.tiagoaguiar.loteriacomposedev.compose.detail.BetListDetailScreen
import co.tiagoaguiar.loteriacomposedev.compose.home.HomeScreen
import co.tiagoaguiar.loteriacomposedev.compose.megasena.MegaScreen

@Composable
fun LoteriaApp() {
    val navController = rememberNavController()
    LoteriaAppNavHost(navController)
}

enum class AppRouter(val route: String) {
    HOME("home"),
    MEGA_SENA("megasena"),
    QUINA("quina"),
    BET_LIST_DETAIL("betlistdetail")
}

@Composable
fun LoteriaAppNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = AppRouter.HOME.route
    ) {
        composable(AppRouter.HOME.route) {
            HomeScreen {
                val router = when (it.id) {
                    1 -> AppRouter.MEGA_SENA
                    2 -> AppRouter.QUINA
                    else -> AppRouter.HOME
                }
                navController.navigate(router.route)
            }
        }
        composable(AppRouter.MEGA_SENA.route) {
            MegaScreen(onBackClick = {
                navController.navigateUp()
            }) {
                navController.navigate(AppRouter.BET_LIST_DETAIL.route + "/$it")
            }
        }
        composable(
            route = AppRouter.BET_LIST_DETAIL.route + "/{type}",
            arguments = listOf(
                navArgument("type") {
                    type = NavType.StringType
                }
            )
        ) {
            val type = it.arguments?.getString("type") ?: throw Exception("Tipo não encontrado!")
            BetListDetailScreen(type = type)
        }
    }
}