package com.example.breakingnewscompose.ui.search_screen

import com.example.breakingnewscompose.domain.Article

data class SearchScreenState(
    val articles: List<Article> = emptyList(),
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val page: Int = 1,
    val canPaginate: Boolean = true
)
