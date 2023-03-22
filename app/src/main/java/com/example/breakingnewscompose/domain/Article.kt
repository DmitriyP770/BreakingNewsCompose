package com.example.breakingnewscompose.domain

import androidx.room.Ignore

data class Article(
     val author: String ,
     val content: String ,
     @Ignore
     val imgUrl: String ,
     val url: String ,
     val title : String,
     val isFavorite: Boolean = false,
     val id: Int?
)
