package com.example.breakingnewscompose.data.pagination

interface Paginator {
    suspend fun loadNextItems()
    fun reset()
}