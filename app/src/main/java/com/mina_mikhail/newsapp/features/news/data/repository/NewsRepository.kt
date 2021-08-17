package com.mina_mikhail.newsapp.features.news.data.repository

import com.mina_mikhail.newsapp.features.news.data.data_source.local.NewsLocalDataSource
import com.mina_mikhail.newsapp.features.news.data.data_source.remote.NewsRemoteDataSource
import com.mina_mikhail.newsapp.features.news.domain.entity.model.Article
import javax.inject.Inject

class NewsRepository @Inject constructor(
  private val remoteDataSource: NewsRemoteDataSource,
  private val newsLocalDataSource: NewsLocalDataSource
) {

  suspend fun getBreakingNews(country: String, page: Int) = remoteDataSource.getBreakingNews(country, page)

  suspend fun searchForNews(searchQuery: String, page: Int) = remoteDataSource.searchForNews(searchQuery, page)

  fun getArticlesFromLocal() = newsLocalDataSource.getArticlesFromLocal()

  suspend fun saveArticleToLocal(article: Article) = newsLocalDataSource.saveArticleToLocal(article)

  suspend fun deleteArticleFromLocal(article: Article) = newsLocalDataSource.deleteArticleFromLocal(article)
}