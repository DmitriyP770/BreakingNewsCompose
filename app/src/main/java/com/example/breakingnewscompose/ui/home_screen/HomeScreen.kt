package com.example.breakingnewscompose.ui.home_screen

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.breakingnewscompose.domain.Article
import com.example.breakingnewscompose.ui.common.ArticleItem
import kotlinx.coroutines.flow.collectLatest

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun HomeScreen (
    modifier : Modifier = Modifier ,
    viewModel : HomeViewModel,
    scaffoldState : ScaffoldState,
    navController : NavController,
) {
    val list = viewModel.state.value.articles
    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest {
            when (it) {
                is HomeViewModel.UIEvent.ShowSnackBar -> {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = it.msg
                    )
                }
            }
        }
    }
        ArticleColumn(
            state = viewModel.state ,
            navController = navController ,
            scaffoldState = scaffoldState ,
            articles = list,
            viewModel = viewModel,

        )
}
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ArticleColumn(
    modifier : Modifier = Modifier ,
    articles: List<Article>,
    navController : NavController,
    scaffoldState : ScaffoldState,
    state: State<HomeScreenState>,
    viewModel : HomeViewModel

){


    LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(8.dp)){
        items(state.value.articles.size){
            val item = articles[it]
            ArticleItem(
                article = item ,
                navController = navController ,
                scaffoldState = scaffoldState ,
            )
            if (it >= articles.size - 1 && state.value.canPaginate && !state.value.isLoading){
                viewModel.loadNewsWithPagination()
            }
            Divider(thickness = 2.dp)
        }
        item {
            if (state.value.isLoading){
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(6.dp) ,
                    horizontalArrangement = Arrangement.Center
                ) {
                   CircularProgressIndicator()
                }
            }
        }
    }
}



