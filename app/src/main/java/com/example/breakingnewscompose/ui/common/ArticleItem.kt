package com.example.breakingnewscompose.ui.common

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.breakingnewscompose.domain.Article

@Composable
fun ArticleItem(
    modifier : Modifier = Modifier,
    article : Article
) {
    Column(modifier = Modifier.fillMaxWidth().padding(4.dp)) {
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
            Text(text = article.title, fontWeight = FontWeight.Bold, fontSize = 24.sp)
            Text(text = article.content)
        }
    }
}