package com.example.breakingnewscompose.data.repository

import com.example.breakingnewscompose.data.local.ArticleDatabase
import com.example.breakingnewscompose.data.network.NewsApi
import com.example.breakingnewscompose.data.network.dto.ArticleDto
import com.example.breakingnewscompose.domain.Article
import com.example.breakingnewscompose.domain.repository.ArticleRepository
import com.example.breakingnewscompose.util.Resource
import com.example.breakingnewscompose.util.toArticle
import com.example.breakingnewscompose.util.toArticleEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ArticleRepositoryImpl @Inject constructor(
    private val db: ArticleDatabase,
    private val api : NewsApi
) : ArticleRepository {
    override suspend fun getAllArticles() : Flow<Resource<List<Article>>> = flow {
        //cached data
        emit(Resource.Loading())
        val localNews = db.dao.getAllArticles().map { it.toArticle() }
        emit(Resource.Success(localNews))

        // load data form network
        try {
            val remoteArticles = api.getBreakingNews()
            db.dao.insertArticles(remoteArticles.map { it.toArticleEntity() })
            emit(Resource.Success(data = db.dao.getAllArticles().map { it.toArticle() }))
        } catch (e: Exception){
            emit(Resource.Error(msg = e.localizedMessage ?:"unknown error", data = localNews))
        }

    }




}