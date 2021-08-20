package com.mina_mikhail.newsapp.features.splash.presentation

import android.os.Handler
import android.os.Looper
import com.mina_mikhail.newsapp.R
import com.mina_mikhail.newsapp.core.view.BaseActivity
import com.mina_mikhail.newsapp.core.view.extensions.openActivityAndClearStack
import com.mina_mikhail.newsapp.databinding.ActivitySplashBinding
import com.mina_mikhail.newsapp.features.news.presentation.NewsActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashActivity : BaseActivity<ActivitySplashBinding>() {

  override
  fun getLayoutId() = R.layout.activity_splash

  override
  fun setUpViews() {
    decideNavigationLogic()
  }

  private fun decideNavigationLogic() {
    Handler(Looper.getMainLooper()).postDelayed({
      openActivityAndClearStack(NewsActivity::class.java)
    }, 2000)
  }
}