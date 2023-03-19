package com.example.breakingnewscompose.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.breakingnewscompose.data.local.model.ArticleEntity
import com.example.breakingnewscompose.data.local.model.ArticleFavoriteEntity

@Dao
interface ArticleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArticles(articles: List<ArticleEntity>)
    @Query("SELECT * FROM article_entity")
    suspend fun getAllArticles(): List<ArticleEntity>
    @Query("DELETE FROM article_entity")
    suspend fun deleteAllArticles()
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoriteArticles(articles: List<ArticleFavoriteEntity>)
    @Query("SELECT * FROM articlefavorite")
    suspend fun getFavoriteArticles(): List<ArticleFavoriteEntity>
    @Query("DELETE FROM articlefavorite")
    suspend fun deleteAllFavoriteArticles()

    @Query("SELECT * FROM article_entity WHERE title LIKE '%' || :query || '%'" )
    suspend fun searchArticle(query: String): List<ArticleEntity>

}