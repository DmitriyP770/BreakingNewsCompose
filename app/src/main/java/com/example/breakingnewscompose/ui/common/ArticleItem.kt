package com.example.breakingnewscompose.ui.common

import android.annotation.SuppressLint
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.breakingnewscompose.domain.Article
import com.example.breakingnewscompose.ui.artile_detail.ArticleDetail
import com.example.breakingnewscompose.ui.common.viewmodels.ArticleViewModel
import com.example.breakingnewscompose.ui.home_screen.HomeViewModel
import com.example.breakingnewscompose.ui.navigation.Graph
import com.squareup.moshi.Moshi
import kotlinx.coroutines.flow.collectLatest
import java.net.URLEncoder
import java.nio.charset.StandardCharsets


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ArticleItem(
    modifier : Modifier = Modifier ,
    article : Article ,
    navController : NavController ,
    scaffoldState: ScaffoldState,
) {
    val articleViewModel: ArticleViewModel = hiltViewModel()
    val url = URLEncoder.encode(article.url, StandardCharsets.UTF_8.toString())
    var isLiked by remember {
        mutableStateOf(article.isFavorite)
    }
    

    LaunchedEffect(key1 = true ){
        articleViewModel.uiEvent.collectLatest {
            when(it){
                is ArticleViewModel.ArticleUIEvent.ShowSnackBar -> {
                   val snackBarresult =  scaffoldState.snackbarHostState.showSnackbar(message = it.msg, actionLabel = "UNDO")
                    when(snackBarresult){
                        SnackbarResult.Dismissed -> {}
                        SnackbarResult.ActionPerformed -> {
                            articleViewModel.saveArticle(article)
                        }
                    }
                }
            }
        }}

    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(4.dp)
//        .clickable { navController.navigate(NewsScreens.ArticleDetail.withArgs(article.url)) }
    ) {
        AsyncImage(modifier = Modifier.fillMaxSize(1f),
                model = ImageRequest.Builder(LocalContext.current)
                .data(article.imgUrl)
                .crossfade(true)
                .build(),
            contentDescription = "Article's Image",

        )
        Column(modifier = Modifier
            .fillMaxSize(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = article.title, fontWeight = FontWeight.Bold, fontSize = 24.sp, modifier = Modifier.clickable (onClick = { navController.navigate(
                Graph.DETAILS+"/" + url
            ) })
               )
            Text(text = article.content)
            Row(
                modifier = Modifier.fillMaxWidth() ,
                verticalAlignment = Alignment.CenterVertically ,
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(
                    onClick = {
                        articleViewModel.handleLikeButtonClick(article)
                        isLiked = !isLiked
                    }
                //                    {
////                        if(!articleState.isFavorite){
////                            articleState.isFavorite = true
////                            /**
////                            how can i correctly store status of article?
////                            1. maybe use global list of local articles (not from db) and update article
////                            info in it.
////                            2. store all articles in db (now im trying to store up to 40 articles -
////                            and statuses i'll retrieve from db. Doesn't work properly for now...
////                            3. somehow share info between two viewmodels. Doesn't seem to be possible...
////
////
////                            ==================
////
////                            i decided to remove like button because i connot see a way to correctly
////                            restore status of an article, since i retrieve it from db, which i nullify
////                            each data request when i paginating. So i belive i should implement some
////                            sort of swipe to save functionality and snackbar which is shown when article
////                            is already in db.
////
////
////                             ===============
////                             or what if i iterate throught favorite articles (from db) each time when
////                             i load new articles and if article which is favorite in my list of all
////                             articles , i'll update thah particular article in my list (in home VM)
////                             */
////
////                            articleViewModel.saveArticle(article = articleState)
////                        } else{
////                            articleState.isFavorite = false
////                            articleViewModel.deleteArticle(articleState)
////                        }
//                },
                ) {
                    LikeButton(isLiked = isLiked)
                }
            }

        }
    }}

@Composable
fun LikeButton(
    isLiked: Boolean
) {
    Icon(
        imageVector = Icons.Default.Favorite ,
        contentDescription = "Like button" ,
        tint = if (isLiked) Color.Red else Color.LightGray
    )
}