package com.example.breakingnewscompose.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.breakingnewscompose.data.local.model.ArticleEntity
import com.example.breakingnewscompose.data.local.model.ArticleFavoriteEntity
import com.example.breakingnewscompose.domain.Article
import kotlinx.coroutines.flow.Flow

@Dao
interface ArticleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArticles(articles: List<ArticleEntity>)
    @Query("SELECT * FROM article_entity")
     fun getAllArticles(): Flow<List<ArticleEntity>>
    @Query("DELETE FROM article_entity")
    suspend fun deleteAllArticles()
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoriteArticles(article: ArticleFavoriteEntity)
    @Query("SELECT * FROM articlefavorite")
    fun getFavoriteArticles(): Flow<List<ArticleFavoriteEntity>>
    @Query("DELETE FROM articlefavorite")
    suspend fun deleteAllFavoriteArticles()

    @Update(entity = ArticleEntity::class)
    suspend fun updateArticleInfo(article : Article)

    @Query("SELECT * FROM article_entity WHERE title LIKE '%' || :query || '%'" )
    suspend fun searchArticle(query: String): List<ArticleEntity>

    @Query("DELETE FROm articlefavorite WHERE url LIKE '%' || :url || '%'")
    suspend fun deleteArticleFromFavorites(url : String)

}