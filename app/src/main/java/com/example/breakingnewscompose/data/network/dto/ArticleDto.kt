package com.example.breakingnewscompose.data.network.dto

import com.example.breakingnewscompose.domain.Article

data class ArticleDto(
    val author: String? ,
    val content: String? ,
    val description: String? ,
    val publishedAt: String ,
    val title: String ,
    val url: String ,
    val urlToImage: String?
)