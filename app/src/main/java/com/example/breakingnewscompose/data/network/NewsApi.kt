package com.example.breakingnewscompose.data.network

import com.example.breakingnewscompose.BuildConfig
import com.example.breakingnewscompose.data.network.dto.ArticleDto
import com.example.breakingnewscompose.data.network.dto.ArticleResponceDto
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {
    @GET("v2/top-headlines")
    suspend fun getBreakingNews(
        @Query("country")
        country: String = "us",
        @Query("page")
        page: Int = 1,
        @Query("pageSize")
        pageSize: Int = 20,
        @Query("apiKey")
        apiKey: String = BuildConfig.API_KEY
    ): ArticleResponceDto
}