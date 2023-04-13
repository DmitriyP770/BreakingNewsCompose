package com.example.breakingnewscompose.data.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.breakingnewscompose.data.local.ArticleDatabase
import com.example.breakingnewscompose.data.local.model.ArticleEntity
import com.example.breakingnewscompose.data.local.model.ArticleFavoriteEntity
import com.example.breakingnewscompose.data.network.NewsApi
import com.example.breakingnewscompose.data.network.dto.ArticleDto
import com.example.breakingnewscompose.domain.Article
import com.example.breakingnewscompose.domain.repository.ArticleRepository
import com.example.breakingnewscompose.util.Resource
import com.example.breakingnewscompose.util.toArticle
import com.example.breakingnewscompose.util.toArticleEntity
import com.example.breakingnewscompose.util.toArticleFavoriteEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import java.time.LocalDateTime
import javax.inject.Inject

class ArticleRepositoryImpl @Inject constructor(
    private val db : ArticleDatabase ,
    private val api : NewsApi ,
) : ArticleRepository {
    @RequiresApi(Build.VERSION_CODES.O)
//    override suspend fun getAllArticles(
//        page : Int ,
//        date : LocalDateTime ,
//    ) : Flow<Resource<List<Article>>> = flow {
//        //cached data
//        emit(Resource.Loading())
//        if (page == 1) {
//            db.dao.deleteFirstSeveralArticles()
//
//        }
//        val localNews = db.dao.getAllArticles().map { it.toArticle() }
//        val now = LocalDateTime.now().dayOfMonth
//        //clear cache if articles from tomorrow or if it's qty more than 50
//
//
//        // load data form network
//        try {
//            val remoteResponce = api.getBreakingNews(page = page)
//            if (now != date.dayOfMonth || page ==1){
//                db.dao.deleteAllArticles()
//            }
//            val remoteArticles = remoteResponce.articles
////            db.dao.deleteAllArticles()
//            db.dao.insertArticles(remoteArticles.map { it.toArticleEntity() })
//            emit(Resource.Success(data = db.dao.getAllArticles().map { it.toArticle() }))
//        } catch (e : Exception) {
//            emit(Resource.Error(msg = e.localizedMessage ?: "unknown error" , data = localNews))
//        }
//
//    }.flowOn(Dispatchers.IO)

   private suspend fun loadArticlesFromNetwork(page : Int) : Resource<Boolean> {
        return try {
           val response =  api.getBreakingNews(page = page)
            val remoteArticles = response.articles
            if (page==1 && remoteArticles.isNotEmpty()) {
                db.dao.deleteAllArticles()
            }
            db.dao.insertArticles(remoteArticles.map { it.toArticleEntity() })
             Resource.Success(data = true)
        } catch (e : Exception) {
             Resource.Error(data = false, msg = e.localizedMessage ?: "An error occurred during network call")
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
  override suspend fun observeBreakingNewsArticlesFromDb(page : Int): Flow<Resource<List<Article>>> = flow {
        val networkCallResult = loadArticlesFromNetwork(page)
        emit(Resource.Loading())
        when(networkCallResult){
            is Resource.Success ->{

                db.dao.getAllArticles().collect{
                    emit(Resource.Success(it.map { it.toArticle() }))
                }
            }
            else -> {
                db.dao.getAllArticles().onEach {
                    emit(Resource.Error(msg = networkCallResult.msg ?: "E", data = it.map { it.toArticle() }))
               }.collect()
            }
        }
//        try {
//            db.dao.getAllArticles().collect{localArticles ->
//                emit(Resource.Success(localArticles.map { it.toArticle() }))
//            }
//        } catch (e:Exception){
//            emit(Resource.Error(msg = "An error occurred when trying to reach local database"))
//        }

    }.flowOn(Dispatchers.IO)



    override suspend fun searchArticles(
        query : String ,
        page : Int ,
    ) : Flow<Resource<List<Article>>> = flow {
        emit(Resource.Loading())
        try {
            val response = api.searchArticles(query = query , page = page)
            val remoteArticles = response.articles.map { it.toArticle() }
            emit(Resource.Success(data = remoteArticles))
        } catch (e : Exception) {
            emit(Resource.Error(data = null , msg = e.localizedMessage ?: "unknown error"))
        }

    }

    override suspend fun saveArticleToFavorites(article : Article) {
        db.dao.insertFavoriteArticles(article.toArticleFavoriteEntity())
    }

    override suspend fun getFavoriteArticles(isStreamNeeded : Boolean) : Flow<Resource<List<Article>>> =
        flow {
            emit(Resource.Loading())
            try {
                db.dao.getFavoriteArticles().collect { localArticles ->
                    emit(Resource.Success(data = localArticles.map { it.toArticle() }))
                }

            } catch (e : Exception) {
                emit(Resource.Error(msg = e.localizedMessage ?: "Unknown error occurred"))
            }
        }.flowOn(Dispatchers.IO)

    override suspend fun deleteArticleFromFavorites(article : Article) {
        db.dao.deleteArticleFromFavorites(article.url)
    }

    override suspend fun updateArticle(article : Article) {
        db.dao.updateArticle(article.url, article.isFavorite)
    }

    override suspend fun onClose(){
            db.dao.deleteFirstSeveralArticles()

    }
}