package com.example.breakingnewscompose.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.breakingnewscompose.data.local.model.ArticleEntity

@Dao
interface ArticleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArticles(articles: List<ArticleEntity>)
    @Query("SELECT * FROM article_entity")
    suspend fun getAllArticles(): List<ArticleEntity>
    @Query("DELETE FROM article_entity")
    suspend fun deleteAllArticles()
}