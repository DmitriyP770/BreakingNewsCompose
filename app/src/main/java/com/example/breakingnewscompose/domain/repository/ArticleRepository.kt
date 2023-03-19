package com.example.breakingnewscompose.domain.repository

import com.example.breakingnewscompose.domain.Article
import com.example.breakingnewscompose.util.Resource
import kotlinx.coroutines.flow.Flow

interface ArticleRepository {

    suspend fun getAllArticles(page: Int ): Flow<Resource<List<Article>>>

    suspend fun searchArticles(query: String, page : Int): Flow<Resource<List<Article>>>

}