package com.example.breakingnewscompose.ui.home_screen

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.breakingnewscompose.ui.common.ArticleItem
import kotlinx.coroutines.flow.collectLatest

@Composable
fun ArticleColumn(modifier : Modifier = Modifier , state : HomeScreenState){
    LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(8.dp)){
        items(state.articles){
            ArticleItem(article = it)
            Divider(thickness = 2.dp)
        }
    }
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun HomeScreen(
    modifier : Modifier = Modifier ,
    viewModel : HomeViewModel  = androidx.lifecycle.viewmodel.compose.viewModel(),
) {
    val scaffoldState = rememberScaffoldState()
    val state = viewModel.state.value
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
    Scaffold(scaffoldState = scaffoldState) {
        ArticleColumn(state = state)
    }


}