package com.mina_mikhail.newsapp.features.news.presentation.news_details

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.webkit.WebViewClient
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
  fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
    FragmentNewsDetailsBinding.inflate(inflater, container, false)

  override
  fun setUpViews() {
    getArticleFromArguments()

    setUpWebView()
  }

  private fun getArticleFromArguments() {
    article = args.article
  }

  @SuppressLint("SetJavaScriptEnabled")
  private fun setUpWebView() {
    binding.webView.apply {
      webViewClient = WebViewClient()
      settings.javaScriptEnabled = true

      loadUrl(article.url)
    }
  }

  override
  fun handleClickListeners() {
    binding.btnSaveArticle.setOnClickListener {
      viewModel.saveArticleToLocal(article)
      showMessage(resources.getString(R.string.article_saved_to_local))
    }
  }
}