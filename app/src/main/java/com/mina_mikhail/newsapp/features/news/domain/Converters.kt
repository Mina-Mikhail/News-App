package com.mina_mikhail.newsapp.features.news.domain

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mina_mikhail.newsapp.features.news.domain.entity.model.Source
import java.lang.reflect.Type

class Converters {

  @TypeConverter
  fun fromSourceString(value: String): Source {
    val modelType: Type = object : TypeToken<Source>() {}.type
    return Gson().fromJson(value, modelType)
  }

  @TypeConverter
  fun fromSourceModel(model: Source): String {
    val gson = Gson()
    return gson.toJson(model)
  }
}