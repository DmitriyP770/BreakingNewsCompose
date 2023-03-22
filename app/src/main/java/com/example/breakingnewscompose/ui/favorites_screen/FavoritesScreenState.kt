package com.example.breakingnewscompose.ui.favorites_screen

import com.example.breakingnewscompose.domain.Article

data class FavoritesScreenState (
    val artices: List<Article> = emptyList(),
    val isLoading: Boolean = false,

        )