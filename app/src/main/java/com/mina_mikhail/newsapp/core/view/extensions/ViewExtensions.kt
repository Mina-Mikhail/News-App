package com.mina_mikhail.newsapp.core.view.extensions

import android.view.View
import android.widget.ImageView
import androidx.constraintlayout.widget.Group
import androidx.databinding.BindingAdapter
import coil.load
import coil.transform.CircleCropTransformation
import coil.transform.RoundedCornersTransformation
import com.google.android.material.snackbar.Snackbar
import com.mina_mikhail.newsapp.R

fun View.show() {
  visibility = View.VISIBLE
  if (this is Group) {
    this.requestLayout()
  }
}

fun View.hide() {
  visibility = View.GONE
  if (this is Group) {
    this.requestLayout()
  }
}

fun View.invisible() {
  visibility = View.INVISIBLE
  if (this is Group) {
    this.requestLayout()
  }
}

@BindingAdapter("app:goneUnless")
fun View.goneUnless(visible: Boolean) {
  visibility = if (visible) View.VISIBLE else View.GONE
  if (this is Group) {
    this.requestLayout()
  }
}

fun View.enable() {
  isEnabled = true
  alpha = 1f
}

fun View.disable() {
  isEnabled = false
  alpha = 0.5f
}

fun View.showSnackBar(message: String, retryActionName: String? = null, action: (() -> Unit)? = null) {
  val snackBar = Snackbar.make(this, message, Snackbar.LENGTH_LONG)

  action?.let {
    snackBar.setAction(retryActionName) {
      it()
    }
  }

  snackBar.show()
}

@BindingAdapter("app:loadImage")
fun ImageView.loadImage(imageUrl: String?) {
  if (imageUrl != null && imageUrl.isNotEmpty()) {
    load(imageUrl) {
      crossfade(true)
      placeholder(R.color.backgroundGray)
      error(R.drawable.bg_no_image)
    }
  } else {
    setImageResource(R.drawable.bg_no_image)
  }
}

@BindingAdapter("app:loadCircleImage")
fun ImageView.loadCircleImage(imageUrl: String?) {
  if (imageUrl != null && imageUrl.isNotEmpty()) {
    load(imageUrl) {
      crossfade(true)
      placeholder(R.color.backgroundGray)
      error(R.drawable.bg_no_image)
      transformations(
        CircleCropTransformation()
      )
    }
  } else {
    load(R.drawable.bg_no_image) {
      crossfade(true)
      transformations(
        CircleCropTransformation()
      )
    }
  }
}

@BindingAdapter("app:loadRoundImage")
fun ImageView.loadRoundImage(imageUrl: String?) {
  if (imageUrl != null && imageUrl.isNotEmpty()) {
    load(imageUrl) {
      crossfade(true)
      placeholder(R.color.backgroundGray)
      error(R.drawable.bg_no_image)
      transformations(
        RoundedCornersTransformation(
          resources.getDimension(R.dimen.dimen7)
        )
      )
    }
  } else {
    load(R.drawable.bg_no_image) {
      crossfade(true)
      transformations(
        RoundedCornersTransformation(
          resources.getDimension(R.dimen.dimen7)
        )
      )
    }
  }
}