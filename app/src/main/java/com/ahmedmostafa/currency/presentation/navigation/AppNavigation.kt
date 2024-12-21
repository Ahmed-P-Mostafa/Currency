package com.ahmedmostafa.currency.presentation.navigation
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ahmedmostafa.currency.presentation.converter.ConverterRoute
import com.ahmedmostafa.currency.presentation.history.HistoryRoute

@Composable
fun AppNavigation(paddingValues: PaddingValues) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Converter.route,
        modifier = Modifier.padding(paddingValues = paddingValues)
    ) {
        composable(Screen.Converter.route) {
            ConverterRoute(
                onNavigateToHistory = { from, to ->
                    navController.navigate(Screen.History.createRoute(from, to))
                }
            )
        }
        composable(
            route = Screen.History.route,
            arguments = Screen.History.arguments
        ) {
            HistoryRoute(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}