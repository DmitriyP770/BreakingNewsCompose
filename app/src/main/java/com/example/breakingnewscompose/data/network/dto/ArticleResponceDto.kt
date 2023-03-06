package com.example.breakingnewscompose.data.network.dto

import com.squareup.moshi.Json

data class ArticleResponceDto(
    @Json(name = "articles")
    val articles: List<ArticleDto>,
    val status: String?,
    val totalResults: Int
)