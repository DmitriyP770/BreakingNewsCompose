package com.example.breakingnewscompose.data.repository

import com.example.breakingnewscompose.data.local.ArticleDatabase
import com.example.breakingnewscompose.data.network.NewsApi
import com.example.breakingnewscompose.domain.Article
import com.example.breakingnewscompose.domain.repository.ArticleRepository
import com.example.breakingnewscompose.util.Resource
import com.example.breakingnewscompose.util.toArticle
import com.example.breakingnewscompose.util.toArticleEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ArticleRepositoryImpl @Inject constructor(
    private val db : ArticleDatabase ,
    private val api : NewsApi ,
) : ArticleRepository {
    override suspend fun getAllArticles(page : Int) : Flow<Resource<List<Article>>> = flow {
        //cached data
        emit(Resource.Loading())
        val localNews = db.dao.getAllArticles().map { it.toArticle() }
        if (page == 1) emit(Resource.Success(localNews))

        // load data form network
        try {
            val remoteResponce = api.getBreakingNews(page = page)
            val remoteArticles = remoteResponce.articles
            db.dao.deleteAllArticles()
            db.dao.insertArticles(remoteArticles.map { it.toArticleEntity() })
            emit(Resource.Success(data = db.dao.getAllArticles().map { it.toArticle() }))
        } catch (e : Exception) {
            emit(Resource.Error(msg = e.localizedMessage ?: "unknown error" , data = localNews))
        }

    }

    override suspend fun searchArticles(
        query : String ,
        page : Int ,
    ) : Flow<Resource<List<Article>>> = flow {
        emit(Resource.Loading())
        try {
            val response = api.searchArticles(query = query , page = page)
            val remoteArticles = response.articles.map { it.toArticle() }
            emit(Resource.Success(data = remoteArticles.map { it }))
        } catch (e : Exception) {
            emit(Resource.Error(data = null, msg = e.localizedMessage ?: "unknown error"))
        }

    }
}