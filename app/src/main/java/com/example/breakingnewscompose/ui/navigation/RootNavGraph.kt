package com.example.breakingnewscompose.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.breakingnewscompose.ui.home_screen.HomeViewModel
import com.example.breakingnewscompose.ui.main_screen.MainScreen
import com.example.breakingnewscompose.ui.search_screen.SearchViewModel

@Composable
fun RootNavGraph(
    navController : NavHostController,
    homeViewModel : HomeViewModel,
    searchViewModel : SearchViewModel
) {
    NavHost(navController = navController , startDestination = Graph.MAIN , route = Graph.ROOT ){
        composable(route = Graph.MAIN){
            MainScreen(homeViewModel = homeViewModel, searchViewModel = searchViewModel)
        }
    }

}

object Graph{
    const val ROOT = "ROOT"
    const val MAIN = "MAIN"
    const val DETAILS = "details"
}