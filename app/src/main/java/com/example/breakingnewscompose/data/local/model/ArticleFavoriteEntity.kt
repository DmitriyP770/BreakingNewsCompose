package com.example.breakingnewscompose.data.local.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "articlefavorite")
data class ArticleFavoriteEntity(
    val author: String,
    val content: String?,
    val description: String?,
    val publishedAt: String,
    val title: String,
    val url: String,
    val urlToImage: String?,
    @PrimaryKey
    val id: Int? = null,
    val isFavorite: Boolean = true
)
