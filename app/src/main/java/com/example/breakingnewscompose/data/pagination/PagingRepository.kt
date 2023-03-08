package com.example.breakingnewscompose.data.pagination

import com.example.breakingnewscompose.data.local.ArticleDatabase
import com.example.breakingnewscompose.data.network.NewsApi
import com.example.breakingnewscompose.domain.Article
import com.example.breakingnewscompose.util.Resource
import com.example.breakingnewscompose.util.toArticle
import com.example.breakingnewscompose.util.toArticleEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class PagingRepository @Inject constructor(
    private val db: ArticleDatabase,
    private val api : NewsApi
) {

    suspend fun getAllNews(page: Int): Flow<Resource<List<Article>>> = flow {
        emit(Resource.Loading())
        val localNews = db.dao.getAllArticles().map { it.toArticle() }
        emit(Resource.Success(localNews))
        try {
            val remoteResponce = api.getBreakingNews()
            val remoteArticles = remoteResponce.articles
            db.dao.deleteAllArticles()
            db.dao.insertArticles(remoteArticles.map { it.toArticleEntity() })
            emit(Resource.Success(data = db.dao.getAllArticles().map { it.toArticle() }))
        } catch (e: Exception){
            emit(Resource.Error(msg = e.localizedMessage ?:"unknown error", data = localNews))
        }
    }.flowOn(Dispatchers.IO)
}