package com.example.breakingnewscompose.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.breakingnewscompose.data.local.model.ArticleEntity

@Database(entities = [ArticleEntity::class], version = 1)
abstract class ArticleDatabase : RoomDatabase() {
    abstract val dao: ArticleDao

}