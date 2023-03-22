package com.example.breakingnewscompose.ui.common

import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
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
import com.example.breakingnewscompose.ui.navigation.Graph
import com.squareup.moshi.Moshi
import java.net.URLEncoder
import java.nio.charset.StandardCharsets


@Composable
fun ArticleItem(
    modifier : Modifier = Modifier ,
    article : Article ,
    navController : NavController ,

) {
    val articleViewModel: ArticleViewModel = hiltViewModel()
    val url = URLEncoder.encode(article.url, StandardCharsets.UTF_8.toString())
    var articleL = article
    var articleState by remember {
        mutableStateOf<Article>(article)
    }
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(4.dp)
//        .clickable { navController.navigate(NewsScreens.ArticleDetail.withArgs(article.url)) }
    ) {    var isExpanded by remember {
        mutableStateOf(false)
    }
        val rotationState by animateFloatAsState(targetValue = if (isExpanded) 180f else 0f)
        AsyncImage(modifier = Modifier.fillMaxSize(1f),
                model = ImageRequest.Builder(LocalContext.current)
                .data(articleState.imgUrl)
                .crossfade(true)
                .build(),
            contentDescription = "Article's Image",

        )
        Column(modifier = Modifier
            .fillMaxSize(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = articleState.title, fontWeight = FontWeight.Bold, fontSize = 24.sp, modifier = Modifier.clickable (onClick = { navController.navigate(
                Graph.DETAILS+"/" + url
            ) })
               )
            Text(text = articleState.content)
            Row(
                modifier = Modifier.fillMaxWidth() ,
                verticalAlignment = Alignment.CenterVertically ,
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(
                    onClick = {
                        if(!articleL.isFavorite){
                            articleState = articleState.copy(
                                isFavorite = true
                            )
                            articleViewModel.updateArticleInfo(articleState)
                            articleViewModel.saveArticle(article = articleState)
                        } else{
                            articleViewModel.deleteArticle(articleState)
                            articleState = articleState.copy(
                                isFavorite = false
                            )
                            articleViewModel.updateArticleInfo(articleState)

                        }

//                        TODO(reason = "add option to unlike a news and delete it from db")
                },
                ) {
                    Icon(
                        imageVector = Icons.Default.Favorite ,
                        contentDescription = "Like button" ,
                        tint = if (articleState.isFavorite) Color.Red else Color.LightGray
                    )
                }
            }

        }
    }
}

@Preview
@Composable
fun SomeFun() {
    Row(
        modifier = Modifier.fillMaxWidth() ,
        verticalAlignment = Alignment.CenterVertically ,
        horizontalArrangement = Arrangement.End
    ) {
        IconButton(onClick = { /*TODO*/ }) {
            Icon(
                imageVector = Icons.Default.Favorite ,
                contentDescription = "Like button" ,
                tint = Color.LightGray
            )
        }
    }
}