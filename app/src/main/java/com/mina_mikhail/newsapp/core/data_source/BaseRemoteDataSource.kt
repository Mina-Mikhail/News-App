package com.mina_mikhail.newsapp.core.data_source

import com.mina_mikhail.newsapp.core.network.FailureStatus.API_FAIL
import com.mina_mikhail.newsapp.core.network.FailureStatus.NO_INTERNET
import com.mina_mikhail.newsapp.core.network.FailureStatus.OTHER
import com.mina_mikhail.newsapp.core.network.FailureStatus.SERVER_SIDE_EXCEPTION
import com.mina_mikhail.newsapp.core.network.FailureStatus.TOKEN_EXPIRED
import com.mina_mikhail.newsapp.core.network.Resource
import com.mina_mikhail.newsapp.core.network.ResponseStatus
import com.mina_mikhail.newsapp.features.news.domain.entity.response.NewsResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.net.UnknownHostException
import javax.inject.Inject

open class BaseRemoteDataSource @Inject constructor() {

  companion object {
    const val LIST_PAGE_SIZE = 10
  }

  suspend fun <T> safeApiCall(apiCall: suspend () -> T): Resource<T> {
    return withContext(Dispatchers.IO) {
      try {
        val apiResponse: T = apiCall.invoke()

        if ((apiResponse as NewsResponse).status == ResponseStatus.SUCCESS) {
          if ((apiResponse as NewsResponse).totalResults == 0) {
            Resource.Empty
          } else {
            Resource.Success(apiResponse)
          }
        } else {
          Resource.Failure(API_FAIL, 200, "Error from api")
        }
      } catch (throwable: Throwable) {
        when (throwable) {
          is HttpException -> {
            if (throwable.code() == 401) {
              Resource.Failure(TOKEN_EXPIRED, throwable.code(), throwable.response()?.errorBody().toString())
            } else {
              Resource.Failure(SERVER_SIDE_EXCEPTION, throwable.code(), throwable.response()?.errorBody().toString())
            }
          }

          is UnknownHostException -> {
            Resource.Failure(NO_INTERNET, null, null)
          }

          else -> {
            Resource.Failure(OTHER, null, null)
          }
        }
      }
    }
  }
}