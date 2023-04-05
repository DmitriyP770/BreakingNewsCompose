package com.example.breakingnewscompose.ui.favorites_screen

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.breakingnewscompose.domain.Article
import com.example.breakingnewscompose.domain.repository.ArticleRepository
import com.example.breakingnewscompose.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val repository : ArticleRepository
) : ViewModel() {
    private var _favoriteArticles = mutableListOf<Article>()

    private val _state = mutableStateOf(FavoritesScreenState())
    val state: State<FavoritesScreenState>
        get() = _state
    private val _uiEvent = MutableSharedFlow<FavoritesScreenUIEvent>()
    val uiEvent: SharedFlow<FavoritesScreenUIEvent>
        get() = _uiEvent

    init {
        getArticlesFavorite()
    }

    private fun getArticlesFavorite(){
        viewModelScope.launch {
            repository.getFavoriteArticles(isStreamNeeded = true).onEach { result ->
                when (result) {
                    is Resource.Error -> {
                        _uiEvent.emit(
                            FavoritesScreenUIEvent.ShowSnackBar(
                                msg = result.msg ?: "An error occurred"
                            )
                        )
                    }
                    is Resource.Success -> {
                        _favoriteArticles = (result.data as MutableList<Article>?)!!
                        _state.value = _state.value.copy(
                            artices = _favoriteArticles ,
                            isLoading = false
                        )
                        println("FAVARTS = $_favoriteArticles")
                    }
                    is Resource.Loading -> {
                        _state.value = _state.value.copy(
                            isLoading = true
                        )
                    }
                }

            }.collect()
        }
    }

    override fun onCleared() {
        viewModelScope.launch { repository.getFavoriteArticles(isStreamNeeded = false) }

        super.onCleared()
    }
}
sealed class FavoritesScreenUIEvent{
    data class ShowSnackBar(val msg: String) : FavoritesScreenUIEvent()
}