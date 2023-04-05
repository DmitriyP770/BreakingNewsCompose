package com.example.breakingnewscompose.ui.favorites_screen

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.breakingnewscompose.domain.Article
import com.example.breakingnewscompose.ui.common.ArticleItem

@Composable
fun FavoritesScreen(
    modifier : Modifier = Modifier,
    favoritesViewModel : FavoritesViewModel,
    navController : NavController,
    scaffoldState : ScaffoldState
) {

    FavoriteScreenColumn(
        articles = favoritesViewModel.state.value.artices ,
        navController =  navController,
        scaffoldState = scaffoldState
    )

//    Scaffold() {
//        LazyColumn(modifier = modifier
//            .fillMaxSize()
//            .padding(it), contentPadding = it){
//
//            items(items = state.value.artices, key = {it.url}){article ->
//                ArticleItem(article = article , navController = navController, scaffoldState = scaffoldState)
//                Divider(thickness = 2.dp)
//            }
//        }
//    }

}

@Composable
fun FavoriteScreenColumn(
    modifier : Modifier = Modifier,
    articles: List<Article>,
    navController : NavController,
    scaffoldState : ScaffoldState
) {
    Scaffold() {
        LazyColumn(modifier = modifier
            .fillMaxSize()
            .padding(it), contentPadding = it){

            items(items = articles, key = {it.url}){article ->
                ArticleItem(article = article , navController = navController, scaffoldState = scaffoldState)
                Divider(thickness = 2.dp)
            }
        }
    }
}