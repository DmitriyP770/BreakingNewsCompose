package com.example.breakingnewscompose.ui.home_screen

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.breakingnewscompose.ui.common.ArticleItem
import kotlinx.coroutines.flow.collectLatest

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun HomeScreen (
    modifier : Modifier = Modifier ,
    viewModel : HomeViewModel,
    scaffoldState : ScaffoldState,
    navController : NavController,

//    viewModel : HomeViewModel  = androidx.lifecycle.viewmodel.compose.viewModel(),
) {
    LaunchedEffect(key1 = true){
        viewModel.eventFlow.collectLatest {
            when(it){
                is HomeViewModel.UIEvent.ShowSnackBar -> {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = it.msg
                    )
                }
            }

        }
    }

//    Scaffold(scaffoldState = scaffoldState) {
//        HomeScreenNavGraph(navHostController = navController as NavHostController , scaffoldState = scaffoldState )
    ArticleColumn(viewModel = viewModel, navController = navController)

//    }


}
@Composable
fun ArticleColumn(
    modifier : Modifier = Modifier ,
    viewModel : HomeViewModel ,
    navController : NavController,

){
    LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(8.dp)){
        val state = viewModel.state.value
        items(state.articles.size){
            val item = state.articles[it]
            ArticleItem(article = item , navController = navController)
            if (it >= state.articles.size - 1 && state.canPaginate && !state.isLoading){
                viewModel.loadNewsWithPagination()
            }
            Divider(thickness = 2.dp)
        }
        item {
            if (state.isLoading){
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



