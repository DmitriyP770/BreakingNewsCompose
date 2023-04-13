package com.example.breakingnewscompose.util

import com.example.breakingnewscompose.BuildConfig

object Constants {
    const val BASE_URL = "https://newsapi.org/"
    const val API_KEY = BuildConfig.API_KEY
    const val SEARCH_DELAY = 800L
    val searchChips = listOf<String>(
        "Politics",
        "Economics",
        "Technology",
        "Games",
        "Sport",

    )
}