package com.mina_mikhail.newsapp.core.utils

import android.text.Editable
import android.text.TextWatcher
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

internal class SearchEditTextListener(
  lifecycle: Lifecycle,
  private val onSearchQueryChange: (String?) -> Unit
) : TextWatcher, LifecycleObserver {

  companion object {
    private const val debouncePeriod: Long = 600
  }

  private val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Main)
  private var currentSearchJob: Job? = null

  init {
    lifecycle.addObserver(this)
  }

  override
  fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

  }

  override
  fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    currentSearchJob?.cancel()

    currentSearchJob = coroutineScope.launch {
      p0?.let {
        delay(debouncePeriod)
        onSearchQueryChange(p0.toString())
      }
    }
  }

  override
  fun afterTextChanged(p0: Editable?) {

  }

  @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
  private fun destroy() {
    currentSearchJob?.cancel()
  }
}