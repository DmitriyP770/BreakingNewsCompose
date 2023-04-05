package com.example.breakingnewscompose.ui.common.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.breakingnewscompose.domain.Article
import com.example.breakingnewscompose.domain.repository.ArticleRepository
import com.example.breakingnewscompose.ui.search_screen.SearchViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArticleViewModel @Inject constructor(
    private val repository : ArticleRepository
) : ViewModel(){

    private val _uiEvent = MutableSharedFlow<ArticleUIEvent>()
    val uiEvent  = _uiEvent.asSharedFlow()

    fun saveArticle(article : Article) = viewModelScope.launch {
        repository.saveArticleToFavorites(article)
    }

    fun deleteArticle(article : Article) = viewModelScope.launch {
        repository.deleteArticleFromFavorites(article)
        _uiEvent.emit(ArticleUIEvent.ShowSnackBar(msg = "Article has been deleted"))
    }

    fun handleLikeButtonClick(article : Article){
        viewModelScope.launch {
            if (article.isFavorite){
                repository.deleteArticleFromFavorites(article)
                article.isFavorite = false
                repository.updateArticle(article)
            } else{
                repository.saveArticleToFavorites(article)
                article.isFavorite = true
                repository.updateArticle(article)
            }
        }
    }


    sealed class ArticleUIEvent{
        data class ShowSnackBar(val msg: String) : ArticleUIEvent()
    }
}