package com.mina_mikhail.newsapp.core.view

import androidx.lifecycle.ViewModel
import com.mina_mikhail.newsapp.core.utils.SingleLiveEvent

open class BaseViewModel : ViewModel() {

  var dataLoadingEvent: SingleLiveEvent<Int> = SingleLiveEvent()

}