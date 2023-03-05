package com.example.breakingnewscompose.util

import com.example.breakingnewscompose.data.local.model.ArticleEntity
import com.example.breakingnewscompose.data.network.dto.ArticleDto
import com.example.breakingnewscompose.domain.Article

fun ArticleDto.toArticleEntity() = ArticleEntity(
    author, content, description, publishedAt, title, url, urlToImage
)

fun ArticleEntity.toArticle() = Article(author, content ?: "",urlToImage?:"",url )