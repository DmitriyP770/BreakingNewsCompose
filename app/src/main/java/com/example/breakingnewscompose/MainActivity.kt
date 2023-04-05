package com.example.breakingnewscompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel

import androidx.navigation.compose.rememberNavController
import com.example.breakingnewscompose.ui.favorites_screen.FavoritesViewModel
import com.example.breakingnewscompose.ui.home_screen.HomeViewModel
import com.example.breakingnewscompose.ui.navigation.RootNavGraph
import com.example.breakingnewscompose.ui.search_screen.SearchViewModel
import com.example.breakingnewscompose.ui.theme.BreakingNewsComposeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BreakingNewsComposeTheme {
//                val navController1 = rememberNavController()
//                val scaffoldState= rememberScaffoldState()
//                val viewModel: HomeViewModel = viewModel()
//                val searchVm: SearchViewModel = viewModel()
//                val favoritesViewModel: FavoritesViewModel = viewModel()
                RootNavGraph(
                    navController = rememberNavController() ,
//                    homeViewModel = viewModel ,
//                    searchViewModel = searchVm ,
//                    favoritesViewModel = favoritesViewModel
                )
            }
        }
    }
}

//TODO("Dissmiss / UNDO 5 sec snackbar")
// TODO: Swipe to delete
// TODO: SearchBar with chips