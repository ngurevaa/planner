package ru.kpfu.itis.gureva.app.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import ru.kpfu.itis.gureva.feature.tasklist.api.navigation.GroupRoute
import ru.kpfu.itis.gureva.feature.tasklist.impl.presentation.GroupScreen
import ru.kpfu.itis.gureva.feature.home.api.navigation.HomeRoute
import ru.kpfu.itis.gureva.feature.home.impl.presentation.ui.screen.HomeScreen

@Composable
fun Navigation(
    navController: NavHostController = rememberNavController()
) {
    NavHost(navController = navController, startDestination = HomeRoute) {
        composable<HomeRoute> {
            HomeScreen(
                navigateToGroup = { id ->
                    navController.navigate(GroupRoute(id))
                }
            )
        }

        composable<GroupRoute> { backStackEntry ->
            val group = backStackEntry.toRoute<GroupRoute>()
            GroupScreen(group.id)
        }
    }
}
