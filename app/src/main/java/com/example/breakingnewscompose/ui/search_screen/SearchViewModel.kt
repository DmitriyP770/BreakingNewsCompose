package com.example.breakingnewscompose.ui.search_screen

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.breakingnewscompose.domain.Article
import com.example.breakingnewscompose.domain.repository.ArticleRepository
import com.example.breakingnewscompose.util.Constants
import com.example.breakingnewscompose.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository : ArticleRepository ,
) : ViewModel() {

    private val _searchQuery = mutableStateOf<String>("")
    val searchQuery : State<String>
        get() = _searchQuery
    private val _articles = mutableStateListOf<Article>()
    private val _state = mutableStateOf(SearchScreenState())
    val state : State<SearchScreenState>
        get() = _state
    private var _searchJob : Job? = null
    private val _eventFlow = MutableSharedFlow<UIEvent>()
    val eventFlow : SharedFlow<UIEvent>
        get() = _eventFlow
    private var _page by mutableStateOf<Int>(1)


    sealed class UIEvent {
        data class ShowSnackBar(val msg : String) : UIEvent()
        object ShowProgressBar : UIEvent()
    }

    fun searchArticles(query : String) {
        if (query !== _searchQuery.value) {
            _page = 1
        }
        _searchQuery.value = query
        _searchJob?.cancel()
        _searchJob = viewModelScope.launch {
            delay(Constants.SEARCH_DELAY)
            repository.searchArticles(query = query , page = _page).onEach { result ->
                when (result) {
                    is Resource.Success -> {
                        if (_page == 1) _articles.clear()
                        _articles.addAll(result.data ?: emptyList())
                        _state.value = _state.value.copy(
                            articles = _articles ,
                            isLoading = false ,
                            canPaginate = result.data?.size == 20
                        )
                        if (_state.value.canPaginate) _page++
                    }
                    is Resource.Error -> {
                        _articles.addAll(result.data ?: emptyList())
                        _state.value = _state.value.copy(
                            articles = _articles ,
                            isLoading = false ,
                            canPaginate = false
                        )
                        _eventFlow.emit(
                            UIEvent.ShowSnackBar(msg = result.msg ?: "Unknown error")
                        )

                    }
                    is Resource.Loading -> {
                        _articles.addAll(result.data ?: emptyList())
                        _state.value = _state.value.copy(
                            articles = _articles ,
                            isLoading = true ,
                            canPaginate = false
                        )
                        _eventFlow.emit(UIEvent.ShowProgressBar)
                    }
                }
            }.launchIn(this)
        }

    }


}



