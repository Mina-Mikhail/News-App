package com.mina_mikhail.newsapp.features.news.data.data_source.local

import androidx.lifecycle.LiveData
import com.mina_mikhail.newsapp.core.local.MyDatabase
import com.mina_mikhail.newsapp.features.news.domain.entity.model.Article
import javax.inject.Inject

class NewsLocalDataSource @Inject constructor(private val myDatabase: MyDatabase) {

  fun getArticlesFromLocal(): LiveData<List<Article>> = myDatabase.getArticlesDao().getAll()

  suspend fun saveArticleToLocal(article: Article) = myDatabase.getArticlesDao().insert(article)

  suspend fun deleteArticleFromLocal(article: Article) = myDatabase.getArticlesDao().delete(article)
}