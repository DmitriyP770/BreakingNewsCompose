package com.example.breakingnewscompose.data.network

import com.example.breakingnewscompose.data.network.dto.ArticleDto
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {
    @GET("v2/top-headlines")
    suspend fun getBreakingNews(
        @Query("country")
        country: String = "us",
        @Query("page")
        page: Int = 1
    ): List<ArticleDto>
}