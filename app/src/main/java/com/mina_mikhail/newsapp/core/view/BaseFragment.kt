package com.mina_mikhail.newsapp.core.view

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.mina_mikhail.newsapp.core.utils.hideLoadingDialog
import com.mina_mikhail.newsapp.core.utils.showLoadingDialog

abstract class BaseFragment<VB : ViewBinding> : Fragment() {

  private var _binding: VB? = null
  open val binding get() = _binding!!
  private var mRootView: View? = null
  private var hasInitializedRootView = false
  private var progressDialog: Dialog? = null

  override
  fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    if (mRootView == null) {
      initViewBinding(inflater, container)
    }

    return mRootView
  }

  private fun initViewBinding(inflater: LayoutInflater, container: ViewGroup?) {
    _binding = getViewBinding(inflater, container)
    mRootView = binding.root
  }

  override
  fun onResume() {
    super.onResume()

    registerListeners()
  }

  override
  fun onPause() {
    unRegisterListeners()

    super.onPause()
  }

  override
  fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    if (!hasInitializedRootView) {
      setUpViews()
      handleClickListeners()
      subscribeToObservables();

      hasInitializedRootView = true
    }
  }

  abstract fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?): VB

  open fun registerListeners() {}

  open fun unRegisterListeners() {}

  open fun setUpViews() {}

  open fun handleClickListeners() {}

  open fun subscribeToObservables() {}

  fun showLoading() {
    hideLoading()
    progressDialog = showLoadingDialog(requireActivity(), null)
  }

  fun showLoading(hint: String?) {
    hideLoading()
    progressDialog = showLoadingDialog(requireActivity(), hint)
  }

  fun hideLoading() = hideLoadingDialog(progressDialog, requireActivity())

  override
  fun onDestroyView() {
    super.onDestroyView()

    _binding = null
  }
}