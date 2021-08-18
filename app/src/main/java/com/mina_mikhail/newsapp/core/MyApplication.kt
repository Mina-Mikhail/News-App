package com.mina_mikhail.newsapp.core

import android.app.Application
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.common.GooglePlayServicesRepairableException
import com.google.android.gms.security.ProviderInstaller
import com.mina_mikhail.newsapp.BuildConfig
import com.mocklets.pluto.Pluto
import com.mocklets.pluto.PlutoLog
import com.mocklets.pluto.modules.exceptions.ANRException
import com.mocklets.pluto.modules.exceptions.ANRListener
import dagger.hilt.android.HiltAndroidApp
import java.security.KeyManagementException
import java.security.NoSuchAlgorithmException
import javax.net.ssl.SSLContext

@HiltAndroidApp
class MyApplication : Application() {

  override
  fun onCreate() {
    super.onCreate()

    updateAndroidSecurityProvider()

    initPlutoNetworkInspection()

    initPlutoANRInspection()
  }

  private fun updateAndroidSecurityProvider() {
    // To fix the following issue, when run app in cellular data, Apis not working
    // javax.net.ssl.SSLHandshakeException: SSL handshake aborted: ssl=0x7edfc49e08: I/O error during system call, Connection reset by peer
    try {
      ProviderInstaller.installIfNeeded(applicationContext)
      val sslContext: SSLContext = SSLContext.getInstance("TLSv1.2")
      sslContext.init(null, null, null)
      sslContext.createSSLEngine()
    } catch (e: GooglePlayServicesRepairableException) {
      e.printStackTrace()
    } catch (e: GooglePlayServicesNotAvailableException) {
      e.printStackTrace()
    } catch (e: NoSuchAlgorithmException) {
      e.printStackTrace()
    } catch (e: KeyManagementException) {
      e.printStackTrace()
    }
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