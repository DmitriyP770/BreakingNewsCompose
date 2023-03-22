package com.example.breakingnewscompose.ui.favorites_screen

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
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
    navController : NavController
) {
    LazyColumn(modifier = modifier.fillMaxSize(), contentPadding = PaddingValues(8.dp)){
        val state = favoritesViewModel.state
        items(state.value.artices){article ->
            ArticleItem(article = article , navController = navController)
            Divider(thickness = 2.dp)
        }
    }
}