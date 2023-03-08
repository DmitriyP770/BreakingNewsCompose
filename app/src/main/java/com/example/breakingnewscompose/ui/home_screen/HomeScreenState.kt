package com.example.breakingnewscompose.ui.home_screen

import com.example.breakingnewscompose.domain.Article

data class HomeScreenState(
    val articles: List<Article> = emptyList(),
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val page: Int = 1,
    val canPaginate: Boolean = true
)
