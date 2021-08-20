package com.mina_mikhail.newsapp.features.news.presentation.news_details

import androidx.lifecycle.viewModelScope
import com.mina_mikhail.newsapp.core.utils.SingleLiveEvent
import com.mina_mikhail.newsapp.core.view.BaseViewModel
import com.mina_mikhail.newsapp.features.news.data.repository.NewsRepository
import com.mina_mikhail.newsapp.features.news.domain.entity.model.Article
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsDetailsViewModel @Inject constructor(private val repository: NewsRepository) : BaseViewModel() {

  val onArticleSavedToLocal: SingleLiveEvent<Void> = SingleLiveEvent()

  private fun saveArticleToLocal(article: Article) = viewModelScope.launch {
    repository.saveArticleToLocal(article)
  }

  fun saveArticle(article: Article) {
    saveArticleToLocal(article)
    onArticleSavedToLocal.call()
  }
}