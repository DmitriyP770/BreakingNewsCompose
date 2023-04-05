package com.example.breakingnewscompose.ui.home_screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.breakingnewscompose.domain.Article
import com.example.breakingnewscompose.domain.repository.ArticleRepository
import com.example.breakingnewscompose.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository : ArticleRepository
) : ViewModel() {
     val _breakingNewsArticles = mutableStateListOf<Article>()
    private val _favoriteArticles = mutableStateListOf<Article>()
    private var _favoriteArticlesOld = mutableListOf<Article>()
    private val _state = mutableStateOf(HomeScreenState())
    val state : State<HomeScreenState>
        get() = _state
    private val _eventFlow = MutableSharedFlow<UIEvent>()
    val eventFlow = _eventFlow.asSharedFlow()
    private val _listState = mutableStateOf(ListState.IDLE)
    private var _page by mutableStateOf<Int>(1)

    @RequiresApi(Build.VERSION_CODES.O)
    fun loadNewsWithPagination() {
        viewModelScope.launch {

            repository.getAllArticles(_page, LocalDateTime.now()).collect { result ->
                when (result) {
                    is Resource.Success -> {

                            _breakingNewsArticles.clear()
                            _breakingNewsArticles.addAll(result.data ?: emptyList())
                        _state.value = _state.value.copy(
                            articles = _breakingNewsArticles,
                            isLoading = false ,
                            isRefreshing = false ,
                            canPaginate = result.data!!.size % 20 == 0 ,
                        )
                        _listState.value = ListState.IDLE
                        checkBreakingNewsInFavorites(result.data)
                        if (_state.value.canPaginate) _page++
                    }
                    is Resource.Loading -> {
                        if (_page == 1) {
                            _breakingNewsArticles.clear()
                        }
                        _state.value = _state.value.copy(
                            articles = _breakingNewsArticles ,
                            isLoading = true ,
                            isRefreshing = false,
                            canPaginate = false
                        )
                        _listState.value = ListState.LOADING
                    }
                    is Resource.Error -> {
                        if (_page==1){
                            _breakingNewsArticles.clear()
                            _breakingNewsArticles.addAll(result.data ?: emptyList())
                        }
                        _state.value = _state.value.copy(
                            articles = _breakingNewsArticles ,
                            isLoading = false ,
                            isRefreshing = false ,
                            canPaginate = false
                        )
                        _eventFlow.emit(
                            UIEvent.ShowSnackBar(msg = result.msg ?: "Unknown error UI Event")
                        )
                        _listState.value =
                            if (_page == 1) ListState.ERROR else ListState.PAGINATION_EXHAUST
                    }
                }

            }
        }
    }
    private fun getFavoriteArticles() {
        viewModelScope.launch {
            repository.getFavoriteArticles(true).onEach{result ->
                when(result){
                    is Resource.Success ->{
                        _favoriteArticlesOld.addAll(result.data!!)
                        _favoriteArticles.addAll(result.data!!)
                        }
                    else ->{}
                }
            }.collect()
        }
    }

    private suspend fun checkBreakingNewsInFavorites(articles: List<Article>){
        articles.forEach { breakingNewsArticle ->
            _favoriteArticles.forEach {favArticle ->
                if (breakingNewsArticle.url == favArticle.url){
                    repository.updateArticle(breakingNewsArticle)
                }
            }
        }
    }





//        _breakingNewsArticles.forEach {breakingNewsArticle ->
//            _favoriteArticles.forEach {favoriteArticle ->
//                breakingNewsArticle.isFavorite =
//                    breakingNewsArticle.url == favoriteArticle.url
//            }
//           val article =  _breakingNewsArticles.find { it.url == article.url }
//            it.isFavorite = article?.isFavorite ?: false
//        }
        /**
         * subscribe on flow from favs
         * create local fav list in vm and add in it articles from fav flow
         * check if art is in fav. then update its info in local breaking news list
         */
//        _breakingNewsArticles.find { it.url == article.url }?.isFavorite = true
//    }

    init {
        loadNewsWithPagination()
        getFavoriteArticles()
//        checkIsArticleFavorite()

    }

    override fun onCleared() {
        viewModelScope.launch {
            _page = 1
            _listState.value = ListState.IDLE
            repository.onClose()
            super.onCleared()
        }

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