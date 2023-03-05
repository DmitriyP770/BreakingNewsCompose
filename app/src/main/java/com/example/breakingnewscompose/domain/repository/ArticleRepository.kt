package com.example.breakingnewscompose.domain.repository

import com.example.breakingnewscompose.domain.Article
import com.example.breakingnewscompose.util.Resource
import kotlinx.coroutines.flow.Flow

interface ArticleRepository {

    suspend fun getAllArticles(): Flow<Resource<List<Article>>>

}