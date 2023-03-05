package com.example.breakingnewscompose.ui.home_screen

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
    private val _state = mutableStateOf(HomeScreenState())
    val state : State<HomeScreenState>
        get() = _state
    private val _eventFlow = MutableSharedFlow<UIEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private fun loadNews() {
        viewModelScope.launch {
            repository.getAllArticles().onEach {result ->
                when(result){
                    is Resource.Success -> {
                        _state.value = _state.value.copy(
                            articles = result.data ?: emptyList(),
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

    init {
        loadNews()
    }

    sealed class UIEvent{
        data class ShowSnackBar(val msg: String) :  UIEvent()
    }

}