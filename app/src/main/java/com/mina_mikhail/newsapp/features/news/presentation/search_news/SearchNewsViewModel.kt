package com.mina_mikhail.newsapp.features.news.presentation.search_news

import androidx.lifecycle.liveData
import com.mina_mikhail.newsapp.core.network.Resource
import com.mina_mikhail.newsapp.core.utils.SingleLiveEvent
import com.mina_mikhail.newsapp.core.view.BaseViewModel
import com.mina_mikhail.newsapp.features.news.data.repository.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class SearchNewsViewModel @Inject constructor(private val repository: NewsRepository) : BaseViewModel() {

  var searchQuery: String = ""
  var shouldLoadMore = false
  var isLoading = false
  var page: Int = 1

  val clearSearchArea: SingleLiveEvent<Void> = SingleLiveEvent()

  fun searchForNews() = liveData(Dispatchers.IO) {
    emit(Resource.Loading)
    emit(repository.searchForNews(searchQuery, page))
  }

  fun onDismissClicked() {
    clearSearchArea.call()
  }
}