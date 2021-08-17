package com.mina_mikhail.newsapp.core.view.extensions

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat

fun <A : Activity> Activity.openActivityAndClearStack(activity: Class<A>) {
  Intent(this, activity).apply {
    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    startActivity(this)
    finish()
  }
}

fun <A : Activity> Activity.openActivity(activity: Class<A>) {
  Intent(this, activity).apply {
    startActivity(this)
  }
}

fun Context.hasPermission(permission: String): Boolean {
  // Background permissions didn't exit prior to Q, so it's approved by default.
  if (permission == Manifest.permission.ACCESS_BACKGROUND_LOCATION &&
    android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.Q
  ) {
    return true
  }

  return ActivityCompat.checkSelfPermission(this, permission) ==
      PackageManager.PERMISSION_GRANTED
}