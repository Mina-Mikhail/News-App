package com.mina_mikhail.newsapp.features.news.presentation.news_details

import android.annotation.SuppressLint
import android.os.Build.VERSION_CODES
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.mina_mikhail.newsapp.R
import com.mina_mikhail.newsapp.core.view.BaseFragment
import com.mina_mikhail.newsapp.core.view.extensions.showMessage
import com.mina_mikhail.newsapp.databinding.FragmentNewsDetailsBinding
import com.mina_mikhail.newsapp.features.news.domain.entity.model.Article
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewsDetailsFragment : BaseFragment<FragmentNewsDetailsBinding>() {

  private val viewModel: NewsDetailsViewModel by viewModels()

  private val args: NewsDetailsFragmentArgs by navArgs()
  private lateinit var article: Article

  override
  fun getLayoutId() = R.layout.fragment_news_details

  override
  fun getFragmentArguments() {
    getArticleFromArguments()
  }

  override
  fun setBindingVariables() {
    binding.viewModel = viewModel
    binding.article = article
  }

  override
  fun setUpViews() {
    showLoading()

    setUpWebView()
  }

  private fun getArticleFromArguments() {
    article = args.article
  }

  @SuppressLint("SetJavaScriptEnabled")
  private fun setUpWebView() {
    binding.webView.apply {
      webChromeClient = WebChromeClient()
      webViewClient = getLollipopWebViewClient()
      settings.javaScriptEnabled = true

      loadUrl(article.url)
    }
  }

  override
  fun setupObservers() {
    viewModel.onArticleSavedToLocal.observe(this, {
      showMessage(resources.getString(R.string.article_saved_to_local))
    })
  }

  @RequiresApi(api = VERSION_CODES.LOLLIPOP) private fun getLollipopWebViewClient(): WebViewClient {
    return object : WebViewClient() {
      override
      fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
        showLoading()
        val url = request.url.toString()
        binding.webView.loadUrl(url)
        return true
      }

      override
      fun onPageFinished(view: WebView, url: String) {
        hideLoading()
      }
    }
  }
}