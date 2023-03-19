package com.example.breakingnewscompose.ui.main_screen

import android.annotation.SuppressLint
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.breakingnewscompose.ui.home_screen.HomeViewModel
import com.example.breakingnewscompose.ui.navigation.HomeNavGraph
import com.example.breakingnewscompose.ui.search_screen.SearchViewModel

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MainScreen(
    navHostController : NavHostController = rememberNavController() ,
    homeViewModel : HomeViewModel,
    searchViewModel : SearchViewModel
) {
    Scaffold(bottomBar = {BottomBar(navController = navHostController)}) {
       HomeNavGraph(navController = navHostController , homeViewModel = homeViewModel , searchViewModel = searchViewModel )
    }
}

@Composable
fun BottomBar(
    navController : NavHostController
) {
    val screens = listOf(
        BottomBarScreens.Home,
        BottomBarScreens.Search
    )
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = backStackEntry?.destination
    val bottomBarDestination = screens.any { it.route == currentDestination?.route }
    if (bottomBarDestination){
        BottomNavigation() {
            screens.forEach {screen->
                BottomNavigationItem(
                    label = { Text(text = screen.title)},
                    icon = {screen.icon},
                    selected =  currentDestination?.hierarchy?.any {
                        it.route == screen.route
                    } == true,
                    onClick = {
                        navController.navigate(screen.route){
                            popUpTo(navController.graph.findStartDestination().id)
                            launchSingleTop = true
                        }
                    }
                )
            }
        }
    }
}