package com.mina_mikhail.newsapp.features.news.presentation.news_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mina_mikhail.newsapp.features.news.data.repository.NewsRepository
import com.mina_mikhail.newsapp.features.news.domain.entity.model.Article
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsDetailsViewModel @Inject constructor(private val repository: NewsRepository) : ViewModel() {

  fun saveArticleToLocal(article: Article) = viewModelScope.launch {
    repository.saveArticleToLocal(article)
  }

}