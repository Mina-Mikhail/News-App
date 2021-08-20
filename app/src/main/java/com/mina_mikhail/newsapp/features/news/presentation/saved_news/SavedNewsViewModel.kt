package com.mina_mikhail.newsapp.features.news.presentation.saved_news

import androidx.lifecycle.viewModelScope
import com.mina_mikhail.newsapp.core.view.BaseViewModel
import com.mina_mikhail.newsapp.features.news.data.repository.NewsRepository
import com.mina_mikhail.newsapp.features.news.domain.entity.model.Article
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SavedNewsViewModel @Inject constructor(private val repository: NewsRepository) : BaseViewModel() {

  fun getArticlesFromLocal() = repository.getArticlesFromLocal()

  fun deleteArticleFromLocal(article: Article) = viewModelScope.launch {
    repository.deleteArticleFromLocal(article)
  }

  fun saveArticleToLocal(article: Article) = viewModelScope.launch {
    repository.saveArticleToLocal(article)
  }

}