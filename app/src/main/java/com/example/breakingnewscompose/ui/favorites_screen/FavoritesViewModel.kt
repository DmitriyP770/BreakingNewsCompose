package com.example.breakingnewscompose.ui.favorites_screen

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
            repository.getFavoriteArticles(isStreamNeeded = true).collect{ result ->
                when(result){
                    is Resource.Error -> {
                        _uiEvent.emit(
                            FavoritesScreenUIEvent.ShowSnackBar(
                                msg = result.msg ?: "An error occurred"
                            )
                        )
                    }
                    is Resource.Success ->{
                        _state.value = _state.value.copy(
                            artices = result.data ?: emptyList(),
                            isLoading = false
                        )
                    }
                    is Resource.Loading -> {
                        _state.value = _state.value.copy(
                            isLoading = true
                        )
                    }
                }

            }
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