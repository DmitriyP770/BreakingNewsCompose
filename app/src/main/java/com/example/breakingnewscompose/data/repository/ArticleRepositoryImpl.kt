package com.example.breakingnewscompose.data.repository

import com.example.breakingnewscompose.data.local.ArticleDatabase
import com.example.breakingnewscompose.data.local.model.ArticleFavoriteEntity
import com.example.breakingnewscompose.data.network.NewsApi
import com.example.breakingnewscompose.domain.Article
import com.example.breakingnewscompose.domain.repository.ArticleRepository
import com.example.breakingnewscompose.util.Resource
import com.example.breakingnewscompose.util.toArticle
import com.example.breakingnewscompose.util.toArticleEntity
import com.example.breakingnewscompose.util.toArticleFavoriteEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class ArticleRepositoryImpl @Inject constructor(
    private val db : ArticleDatabase ,
    private val api : NewsApi ,
) : ArticleRepository {
    override suspend fun getAllArticles(page : Int) : Flow<Resource<List<Article>>> = flow {
        //cached data
        emit(Resource.Loading())
        var localNews = emptyList<Article>()
        db.dao.getAllArticles().collect{
            val localArticles = it.map { it.toArticle() }
            if (page == 1) emit(Resource.Success(localArticles))
            localNews = it.map { it.toArticle() }
        }

        // load data form network
        try {
            val remoteResponce = api.getBreakingNews(page = page)
            val remoteArticles = remoteResponce.articles
            db.dao.deleteAllArticles()
            db.dao.insertArticles(remoteArticles.map { it.toArticleEntity() })
            val newArticles = db.dao.getAllArticles().collect {
                val newArticles = it.map { it.toArticle() }
                emit(Resource.Success( newArticles))
            }

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
            emit(Resource.Error(data = null , msg = e.localizedMessage ?: "unknown error"))
        }

    }

    override suspend fun saveArticleToFavorites(article : Article) {
        db.dao.insertFavoriteArticles(article.toArticleFavoriteEntity())
    }

    override suspend fun getFavoriteArticles(isStreamNeeded: Boolean) : Flow<Resource<List<Article>>> = flow {
        emit(Resource.Loading())
//        while (isStreamNeeded){
        try {
            val localArticles = db.dao.getFavoriteArticles().collect{localArticles ->
                emit(Resource.Success(data = localArticles.map { it.toArticle() }))
            }

        } catch (e : Exception) {
            emit(Resource.Error(msg = e.localizedMessage ?: "Unknown error occurred"))
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun deleteArticleFromFavorites(article : Article) {
        db.dao.deleteArticleFromFavorites(article.url)
    }

    override suspend fun updateLocalArticleInfo(article : Article) {
        db.dao.updateArticleInfo(article = article)
    }
}