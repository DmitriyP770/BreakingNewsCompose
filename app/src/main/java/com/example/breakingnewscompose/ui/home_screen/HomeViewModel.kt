package com.example.breakingnewscompose.ui.home_screen

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.breakingnewscompose.domain.Article
import com.example.breakingnewscompose.domain.repository.ArticleRepository
import com.example.breakingnewscompose.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository : ArticleRepository
) : ViewModel() {
    private val _articles = mutableStateListOf<Article>()
    private val _state = mutableStateOf(HomeScreenState())
    val state : State<HomeScreenState>
        get() = _state
    private val _eventFlow = MutableSharedFlow<UIEvent>()
    val eventFlow = _eventFlow.asSharedFlow()
    private val _listState = mutableStateOf(ListState.IDLE)
    private var _page by mutableStateOf<Int>(1)



    private fun loadNews() {
        viewModelScope.launch {
            repository.getAllArticles(page = _page).onEach {result ->
                when(result){
                    is Resource.Success -> {
                        _state.value = _state.value.copy(
                            articles = _articles,
                            isLoading = false,
                            isRefreshing = false
                        )

                    }
                    is Resource.Loading -> {
                        _state.value = _state.value.copy(
                            articles = result.data ?: emptyList(),
                            isLoading = true,
                            isRefreshing = false
                        )
                    }
                    is Resource.Error ->{
                        _state.value = _state.value.copy(
                            articles = result.data ?: emptyList(),
                            isLoading = false,
                            isRefreshing = false
                        )
                        _eventFlow.emit(
                            UIEvent.ShowSnackBar(msg = result.msg ?: "Unknown error UI Event")
                        )
                    }
                }
            }.launchIn(this)
        }
    }

     fun loadNewsWithPagination(){
        viewModelScope.launch {
//            if (_page==1 ||
//                (_page != 1 && _state.value.canPaginate) &&
//                _listState.value == ListState.IDLE){
//                _listState.value = if (_page== 1) ListState.LOADING else ListState.PAGINATING}
                repository.getAllArticles(_page).onEach {result ->
                    when(result){
                        is Resource.Success -> {
                            if (_page==1) _articles.clear()
                            _articles.addAll(result.data ?: emptyList())
                            _state.value = _state.value.copy(
                                articles = _articles,
                                isLoading = false,
                                isRefreshing = false,
                                canPaginate = result.data!!.size == 20,
                            )
                            _listState.value = ListState.IDLE
                            if (_state.value.canPaginate) _page++
                        }
                        is Resource.Loading -> {
                            _state.value = _state.value.copy(
                                articles = result.data ?: emptyList(),
                                isLoading = true,
                                isRefreshing = false
                            )
                            _listState.value = ListState.LOADING
                        }
                        is Resource.Error ->{
                            _state.value = _state.value.copy(
                                articles = result.data ?: emptyList(),
                                isLoading = false,
                                isRefreshing = false
                            )
                            _eventFlow.emit(
                                UIEvent.ShowSnackBar(msg = result.msg ?: "Unknown error UI Event")
                            )
                            _listState.value = if (_page==1) ListState.ERROR else ListState.PAGINATION_EXHAUST
                        }
                    }

                }
            }
        }


    init {
        loadNewsWithPagination()
    }

    override fun onCleared() {
        _page = 1
        _listState.value = ListState.IDLE
        super.onCleared()
    }



    sealed class UIEvent{
        data class ShowSnackBar(val msg: String) :  UIEvent()
    }

    enum class ListState {
        IDLE,
        LOADING,
        PAGINATING,
        ERROR,
        PAGINATION_EXHAUST,
    }

}