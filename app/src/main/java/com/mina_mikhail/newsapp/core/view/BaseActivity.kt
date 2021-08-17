package com.mina_mikhail.newsapp.core.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.navigation.NavController
import androidx.viewbinding.ViewBinding

abstract class BaseActivity<VB : ViewBinding> : AppCompatActivity() {

  protected lateinit var binding: VB
  protected lateinit var navController: LiveData<NavController>

  override
  fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    initViewBinding()
    setContentView(binding.root)

    if (savedInstanceState == null) {
      setUpBottomNavigation()
    }

    setUpViews()
  }

  override
  fun onRestoreInstanceState(savedInstanceState: Bundle) {
    super.onRestoreInstanceState(savedInstanceState)

    setUpBottomNavigation()
  }

  private fun initViewBinding() {
    binding = getViewBinding()
  }

  abstract fun getViewBinding(): VB

  open fun setUpBottomNavigation() {}

  open fun setUpViews() {}

  override
  fun onSupportNavigateUp(): Boolean {
    return navController.value?.navigateUp()!! || super.onSupportNavigateUp()
  }
}