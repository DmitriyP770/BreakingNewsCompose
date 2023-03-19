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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.breakingnewscompose.domain.Article
import com.example.breakingnewscompose.ui.artile_detail.ArticleDetail
import com.example.breakingnewscompose.ui.navigation.Graph
import com.squareup.moshi.Moshi
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

val moshi = Moshi.Builder().build()
val jsonAdapter = moshi.adapter(Article::class.java).lenient()
@Composable
fun ArticleItem(
    modifier : Modifier = Modifier ,
    article : Article ,
    navController : NavController,
) {
    val url = URLEncoder.encode(article.url, StandardCharsets.UTF_8.toString())
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
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Text(text = "See more", color = Color.Blue, fontSize = 20.sp)
                IconButton(
                    modifier = Modifier
                        .alpha(ContentAlpha.medium)
                        .weight(1f)
                        .rotate(rotationState) ,
                    onClick = { isExpanded = !isExpanded }
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown ,
                        contentDescription = "Drop Down Error" ,
                    )
                }
            }

        }
    }
}
