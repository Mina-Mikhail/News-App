package com.mina_mikhail.newsapp.core.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mina_mikhail.newsapp.features.news.data.data_source.local.ArticlesDao
import com.mina_mikhail.newsapp.features.news.domain.Converters
import com.mina_mikhail.newsapp.features.news.domain.entity.model.Article

@Database(entities = [Article::class], version = MyDatabase.DATABASE_VERSION)
@TypeConverters(Converters::class)
abstract class MyDatabase : RoomDatabase() {

  companion object {
    const val DATABASE_VERSION = 1
    const val DATABASE_NAME = "NewsDatabase"
  }

  abstract fun getArticlesDao(): ArticlesDao
}