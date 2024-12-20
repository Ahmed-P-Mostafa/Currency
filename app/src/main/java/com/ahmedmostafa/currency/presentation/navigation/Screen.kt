package com.ahmedmostafa.currency.presentation.navigation
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed class Screen(
    val route: String,
    val arguments: List<NamedNavArgument> = emptyList()
) {
    object Converter : Screen(CONVERTER_ROUTE)
    
    object History : Screen(
        route = "$HISTORY_ROUTE/{$HISTORY_FROM_ARG}/{$HISTORY_TO_ARG}",
        arguments = listOf(
            navArgument(HISTORY_FROM_ARG) { type = NavType.StringType },
            navArgument(HISTORY_TO_ARG) { type = NavType.StringType }
        )
    ) {
        fun createRoute(fromCurrency: String, toCurrency: String) =
            "$HISTORY_ROUTE/$fromCurrency/$toCurrency"
    }
}