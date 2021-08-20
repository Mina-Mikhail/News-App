package com.mina_mikhail.newsapp.core.di.module

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.mina_mikhail.newsapp.BuildConfig
import com.mocklets.pluto.PlutoInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {

  private const val REQUEST_TIME_OUT: Long = 60

  @Provides
  @Singleton
  fun provideHeadersInterceptor(): Interceptor =
    Interceptor { chain: Interceptor.Chain ->
      val original = chain.request()
      // Request customization: add request query params
      val originalHttpUrl: HttpUrl = original.url
      val url = originalHttpUrl.newBuilder()
        .addQueryParameter("apiKey", BuildConfig.API_KEY)
        .build()
      // Request customization: add request headers
      val requestBuilder = original.newBuilder()
        .url(url)
      val request = requestBuilder.build()
      chain.proceed(request)
    }

  @Provides
  @Singleton
  fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
    val logging = HttpLoggingInterceptor()
    logging.level = HttpLoggingInterceptor.Level.BODY
    return logging
  }

  @Provides
  @Singleton
  fun provideOkHttpClient(
    headersInterceptor: Interceptor,
    logging: HttpLoggingInterceptor
  ): OkHttpClient {
    return if (BuildConfig.DEBUG) {
      OkHttpClient.Builder()
        .readTimeout(REQUEST_TIME_OUT, TimeUnit.SECONDS)
        .connectTimeout(REQUEST_TIME_OUT, TimeUnit.SECONDS)
        .addInterceptor(headersInterceptor)
        .addNetworkInterceptor(logging)
        .addInterceptor(PlutoInterceptor())
        .build()
    } else {
      OkHttpClient.Builder()
        .readTimeout(REQUEST_TIME_OUT, TimeUnit.SECONDS)
        .connectTimeout(REQUEST_TIME_OUT, TimeUnit.SECONDS)
        .addInterceptor(headersInterceptor)
        .build()
    }
  }

  @Provides
  @Singleton
  fun provideGson(): Gson {
    return GsonBuilder()
      .setLenient()
      .serializeNulls() // To allow sending null values
      .create()
  }

  @Provides
  @Singleton
  fun provideRetrofit(gson: Gson, okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
    .client(okHttpClient)
    .addConverterFactory(GsonConverterFactory.create(gson))
    .baseUrl(BuildConfig.API_BASE_URL)
    .build()

}