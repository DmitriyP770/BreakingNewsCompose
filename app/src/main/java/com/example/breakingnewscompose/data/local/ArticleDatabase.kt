package com.example.breakingnewscompose.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.breakingnewscompose.data.local.model.ArticleEntity
import com.example.breakingnewscompose.data.local.model.ArticleFavoriteEntity

@Database(entities = [ArticleEntity::class, ArticleFavoriteEntity::class], version = 9)
abstract class ArticleDatabase : RoomDatabase() {
    abstract val dao: ArticleDao

}