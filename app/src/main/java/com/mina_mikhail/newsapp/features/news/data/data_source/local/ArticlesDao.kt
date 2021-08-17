package com.mina_mikhail.newsapp.features.news.data.data_source.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mina_mikhail.newsapp.features.news.domain.entity.model.Article

@Dao
interface ArticlesDao {
  /**
   * Select all articles from the articles table.
   *
   * @return all articles.
   */
  @Query("SELECT * FROM articles")
  fun getAll(): LiveData<List<Article>>

  /**
   * Insert articles in the database. If the article already exists, replace it.
   *
   * @param article the city to be inserted.
   */
  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insert(article: Article)

  /**
   * Delete article from the articles table.
   *
   * @param article the city to be inserted.
   */
  @Delete
  suspend fun delete(article: Article)
}