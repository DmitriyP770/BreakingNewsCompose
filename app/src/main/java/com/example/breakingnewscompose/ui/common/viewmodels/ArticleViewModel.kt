package com.example.breakingnewscompose.ui.common.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.breakingnewscompose.domain.Article
import com.example.breakingnewscompose.domain.repository.ArticleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArticleViewModel @Inject constructor(
    private val repository : ArticleRepository
) : ViewModel(){

    fun saveArticle(article : Article) = viewModelScope.launch {
        repository.saveArticleToFavorites(article)
    }

    fun deleteArticle(article : Article) = viewModelScope.launch {
        repository.deleteArticleFromFavorites(article)
    }

    fun updateArticleInfo(article : Article) = viewModelScope.launch {
        repository.updateLocalArticleInfo(article)
    }
}