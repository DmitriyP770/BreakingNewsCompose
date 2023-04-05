package com.example.breakingnewscompose.domain.repository

import com.example.breakingnewscompose.data.local.model.ArticleFavoriteEntity
import com.example.breakingnewscompose.domain.Article
import com.example.breakingnewscompose.util.Resource
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

interface ArticleRepository {

    suspend fun getAllArticles(page: Int, date: LocalDateTime ): Flow<Resource<List<Article>>>

    suspend fun searchArticles(query: String, page : Int): Flow<Resource<List<Article>>>

    suspend fun saveArticleToFavorites(article : Article)

    suspend fun getFavoriteArticles(isStreamNeeded: Boolean): Flow<Resource<List<Article>>>

    suspend fun deleteArticleFromFavorites(article : Article)

    suspend fun updateArticle(article : Article)

    suspend fun onClose()



}