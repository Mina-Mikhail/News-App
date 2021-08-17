package com.mina_mikhail.newsapp.features.news.domain.entity.response

import com.mina_mikhail.newsapp.features.news.domain.entity.model.Article

data class NewsResponse(
  val articles: List<Article>,
  val status: String,
  val totalResults: Int
)