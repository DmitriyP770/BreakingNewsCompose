package com.example.breakingnewscompose.ui.navigation

import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.navigation.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.breakingnewscompose.ui.artile_detail.ArticleDetail
import com.example.breakingnewscompose.ui.favorites_screen.FavoritesScreen
import com.example.breakingnewscompose.ui.favorites_screen.FavoritesViewModel
import com.example.breakingnewscompose.ui.home_screen.HomeScreen
import com.example.breakingnewscompose.ui.home_screen.HomeViewModel
import com.example.breakingnewscompose.ui.main_screen.BottomBarScreens
import com.example.breakingnewscompose.ui.search_screen.SearchScreen
import com.example.breakingnewscompose.ui.search_screen.SearchViewModel

@Composable
fun HomeNavGraph(
    navController: NavHostController,
    homeViewModel : HomeViewModel,
    searchViewModel : SearchViewModel,
    favoritesViewModel : FavoritesViewModel

) {
    NavHost(
        navController = navController ,
        route = Graph.MAIN ,
        startDestination = BottomBarScreens.Home.route
    ){
        composable(route = BottomBarScreens.Home.route){
            HomeScreen(viewModel = homeViewModel , scaffoldState = rememberScaffoldState() , navController = navController)
        }
        composable(route = BottomBarScreens.Search.route){
            SearchScreen(viewModel = searchViewModel , navController = navController)
        }
        composable(route = BottomBarScreens.Favorites.route){
            FavoritesScreen(favoritesViewModel = favoritesViewModel , navController = navController)
        }
        detailsNavGraph(navController = navController)
    }
}

fun NavGraphBuilder.detailsNavGraph(navController : NavHostController){
    navigation(route = Graph.DETAILS+"/{article}", startDestination = "detailsScreen/{article}"){
        composable(route = "detailsScreen/{article}", arguments = listOf(
            navArgument("article"){
                type = NavType.StringType
            }
        )
        ){
            val url = it.arguments?.getString("article") ?: "https://www.google.com/"
             ArticleDetail(url = url)
        }

    }
}