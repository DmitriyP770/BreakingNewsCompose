package com.example.breakingnewscompose.data.pagination

interface Paginator <Key, Item>{
    suspend fun loadNextItems()
    fun reset()
}