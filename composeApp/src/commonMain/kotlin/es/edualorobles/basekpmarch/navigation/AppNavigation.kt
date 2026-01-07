package es.edualorobles.basekpmarch.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import es.edualorobles.basekpmarch.ui.DashboardScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Route.Dashboard
    ) {
        composable<Route.Dashboard> {
            DashboardScreen()
        }
    }
}