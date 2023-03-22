package com.example.breakingnewscompose.util

import com.example.breakingnewscompose.data.local.model.ArticleEntity
import com.example.breakingnewscompose.data.local.model.ArticleFavoriteEntity
import com.example.breakingnewscompose.data.network.dto.ArticleDto
import com.example.breakingnewscompose.domain.Article

fun ArticleDto.toArticleEntity() = ArticleEntity(
    author?: "", content, description, publishedAt, title, url, urlToImage
)

fun ArticleDto.toArticle() = Article(author ?: "", content ?: "", urlToImage ?: "", url, title, id = null)

fun ArticleEntity.toArticle() = Article(author, content ?: "",urlToImage?:"",url, title, isFavorite, id )

fun ArticleFavoriteEntity.toArticle() = Article(author, content ?: "", urlToImage ?: "", url, title, isFavorite = isFavorite, id = id)

fun Article.toArticleFavoriteEntity() = ArticleFavoriteEntity(
    author = author ,
    content = content ,
    description = null ,
    publishedAt = "" ,
    title = title,
    url = url,
    urlToImage = imgUrl,
    isFavorite = true
)