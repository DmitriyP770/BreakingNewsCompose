package com.example.breakingnewscompose.ui.search_screen

import android.annotation.SuppressLint
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.example.breakingnewscompose.ui.common.ArticleItem
import kotlinx.coroutines.flow.collectLatest

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun SearchScreen(
    modifier : Modifier = Modifier ,
    viewModel : SearchViewModel,
    navController : NavController
) {
    val scaffoldState = rememberScaffoldState()
    val state = viewModel.state
    val focusManager = LocalFocusManager.current
    LaunchedEffect(key1 = true){
        viewModel.eventFlow.collectLatest {
            when(it){
                is SearchViewModel.UIEvent.ShowSnackBar -> {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = it.msg
                    )
                }
                is SearchViewModel.UIEvent.ShowProgressBar -> {
                    
                }
            }

        }
    }
    Scaffold(scaffoldState = scaffoldState) {
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(4.dp)
        ) {
            SearchField(
                query = viewModel.searchQuery.value ,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done
                ) ,
                keyboardActions = KeyboardActions(onDone = {
                    focusManager.clearFocus()
                }),
                onQueryChanged = viewModel::searchArticles,
                modifier = Modifier.padding(horizontal = 12.dp)
            )
            Divider(modifier = Modifier.height(8.dp))
            ArticleSearchColumn(viewModel = viewModel, modifier = Modifier.fillMaxSize(), navController = navController )

        }

    }

}

@Composable
fun SearchField(
    modifier : Modifier = Modifier,
    query: String,
    keyboardOptions : KeyboardOptions,
    keyboardActions : KeyboardActions,
    onQueryChanged: (String) -> Unit
) {
    TextField(
        value = query ,
        onValueChange = onQueryChanged ,
        placeholder = {
            Text(text = "Enter your search query")
        },
        singleLine = true,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        modifier = Modifier.fillMaxWidth().shadow(5.dp, shape = CircleShape)
    )

}
@Composable
fun ArticleSearchColumn(
    modifier : Modifier = Modifier ,
    viewModel : SearchViewModel ,
    navController : NavController
){

    LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(8.dp)){
        val state = viewModel.state.value
        items(state.articles.size){
            val item = state.articles[it]
            ArticleItem(article = item, navController = navController)
            if (it >= state.articles.size - 1 && state.canPaginate && !state.isLoading){
                viewModel.searchArticles(viewModel.searchQuery.value)
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

