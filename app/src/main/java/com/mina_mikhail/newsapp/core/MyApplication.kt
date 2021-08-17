package com.mina_mikhail.newsapp.core

import android.app.Application
import com.mina_mikhail.newsapp.BuildConfig
import com.mocklets.pluto.Pluto
import com.mocklets.pluto.PlutoLog
import com.mocklets.pluto.modules.exceptions.ANRException
import com.mocklets.pluto.modules.exceptions.ANRListener
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApplication : Application() {

  override
  fun onCreate() {
    super.onCreate()

    initPlutoNetworkInspection()

    initPlutoANRInspection()
  }

  private fun initPlutoNetworkInspection() {
    if (BuildConfig.DEBUG) {
      Pluto.initialize(this)
    }
  }

  private fun initPlutoANRInspection() {
    if (BuildConfig.DEBUG) {
      Pluto.setANRListener(object : ANRListener {
        override
        fun onAppNotResponding(exception: ANRException) {
          exception.printStackTrace()
          PlutoLog.e("ANR", exception.threadStateMap)
        }
      })
    }
  }
}