package com.mina_mikhail.newsapp.core.network

sealed class Resource<out T> {

  data class Success<out T>(val value: T) : Resource<T>()

  data class Failure(val failureStatus: FailureStatus, val code: Int?, val message: String?) : Resource<Nothing>()

  object Empty : Resource<Nothing>()

  object Loading : Resource<Nothing>()

}