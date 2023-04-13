package com.example.breakingnewscompose.ui.search_screen

import android.annotation.SuppressLint
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.example.breakingnewscompose.ui.common.ArticleItem
import com.example.breakingnewscompose.ui.main_screen.BottomBarScreens
import com.example.breakingnewscompose.util.Constants
import kotlinx.coroutines.flow.collectLatest

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun SearchScreen(
    modifier : Modifier = Modifier ,
    viewModel : SearchViewModel,
    navController : NavController,
    scaffoldState : ScaffoldState
) {
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
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 8.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            SearchField(
                query = viewModel.searchQuery.value ,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done
                ) ,
                keyboardActions = KeyboardActions(onDone = {
                    focusManager.clearFocus()
                }),
                onQueryChanged = viewModel::searchArticles,
                modifier = Modifier.padding(horizontal = 12.dp),
                onClear = { viewModel.searchArticles("") }
            )
            Spacer(modifier = Modifier.height(8.dp))
            SearchChipsRow(items = Constants.searchChips, viewModel = viewModel)
            if (viewModel.searchQuery.value.isNotBlank()) {
                ArticleSearchColumn(
                    viewModel = viewModel ,
                    modifier = Modifier.fillMaxSize() ,
                    navController = navController ,
                    scaffoldState = scaffoldState
                )
            }
        }

}

@Composable
fun SearchField(
    modifier : Modifier = Modifier,
    query: String,
    keyboardOptions : KeyboardOptions,
    keyboardActions : KeyboardActions,
    onQueryChanged: (String) -> Unit,
    onClear: () -> Unit
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
        modifier = Modifier
            .fillMaxWidth()
            .shadow(2.dp , shape = CircleShape)
            .background(Color.White , CircleShape) ,
        leadingIcon = {Icon(
            imageVector = Icons.Default.Search ,
            contentDescription = "Search Icon"
        )},
        trailingIcon = { IconButton(onClick = onClear) {
            Icon(imageVector = Icons.Default.Close , contentDescription = "Clear button" )
        }
        }


    )

}
@Composable
fun ArticleSearchColumn(
    modifier : Modifier = Modifier ,
    viewModel : SearchViewModel ,
    navController : NavController,
    scaffoldState : ScaffoldState
){

    LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(8.dp)){
        val state = viewModel.state.value
        items(state.articles.size){
            val item = state.articles[it]
            ArticleItem(article = item, navController = navController, scaffoldState = scaffoldState,)
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

@Composable
fun SearchChipsRow(
    modifier : Modifier = Modifier,
    items: List<String>,
    viewModel : SearchViewModel

) {
    LazyRow(modifier = modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
        items(Constants.searchChips){
            SearchChipItem(text = it, ) {
                viewModel.searchArticles(it)
            }
            Spacer(modifier = Modifier.width(6.dp))
        }
    }
}

@Composable
fun SearchChipItem(
    text: String,
    modifier : Modifier = Modifier,
    onclick: () -> Unit
) {
    Box(modifier = Modifier
        .clip(RoundedCornerShape(20.dp,20.dp, 20.dp, 20.dp))
        .background(MaterialTheme.colors.primary)
        .clickable(onClick = onclick),
    contentAlignment = Alignment.Center,
    ) {
        Text(text = text, fontSize = 16.sp, color = Color.White ,modifier = Modifier.padding(8.dp))
        }
}
