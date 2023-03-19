package com.example.breakingnewscompose.ui.artile_detail

import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.example.breakingnewscompose.domain.Article

@Composable
fun ArticleDetail(url : String , modifier : Modifier = Modifier , ) {
    AndroidView(factory = {
        WebView(it).apply {

            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
            )
            webViewClient = WebViewClient()

            loadUrl(url)
        }
    }, update = {
        it.loadUrl(url)
    }
    )
}

@Composable
fun TESTTEST(n: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = n, fontSize = 48.sp)
    }
}