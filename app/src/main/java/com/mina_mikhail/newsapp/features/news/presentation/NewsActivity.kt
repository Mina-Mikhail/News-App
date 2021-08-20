package com.mina_mikhail.newsapp.features.news.presentation

import com.mina_mikhail.newsapp.R
import com.mina_mikhail.newsapp.core.view.BaseActivity
import com.mina_mikhail.newsapp.core.view.extensions.setupWithNavController
import com.mina_mikhail.newsapp.databinding.ActivityNewsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewsActivity : BaseActivity<ActivityNewsBinding>() {

  override
  fun getLayoutId() = R.layout.activity_news

  override
  fun setUpBottomNavigation() {
    val graphIds = listOf(
      R.navigation.nav_breaking_news,
      R.navigation.nav_saved_news,
      R.navigation.nav_search_news
    )

    val controller = binding.bottomNavigationView.setupWithNavController(
      graphIds,
      supportFragmentManager,
      R.id.fragment_host_container,
      intent
    )

    navController = controller
  }
}