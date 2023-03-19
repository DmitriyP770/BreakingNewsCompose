package com.example.breakingnewscompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.rememberScaffoldState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
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
                val viewModel: HomeViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
                val searchVm: SearchViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
                RootNavGraph(navController = rememberNavController(), homeViewModel = viewModel, searchViewModel = searchVm)
            }
        }
    }
}
