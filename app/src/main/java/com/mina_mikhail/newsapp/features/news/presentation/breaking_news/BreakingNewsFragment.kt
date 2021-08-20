package com.mina_mikhail.newsapp.features.news.presentation.breaking_news

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mina_mikhail.newsapp.R
import com.mina_mikhail.newsapp.core.data_source.BaseRemoteDataSource
import com.mina_mikhail.newsapp.core.enums.DataStatus
import com.mina_mikhail.newsapp.core.network.Resource.Empty
import com.mina_mikhail.newsapp.core.network.Resource.Failure
import com.mina_mikhail.newsapp.core.network.Resource.Loading
import com.mina_mikhail.newsapp.core.network.Resource.Success
import com.mina_mikhail.newsapp.core.utils.EndlessRecyclerViewScrollListener
import com.mina_mikhail.newsapp.core.view.BaseFragment
import com.mina_mikhail.newsapp.core.view.extensions.handleApiError
import com.mina_mikhail.newsapp.databinding.FragmentBreakingNewsBinding
import com.mina_mikhail.newsapp.features.news.domain.entity.model.Article
import com.mina_mikhail.newsapp.features.news.presentation.NewsAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BreakingNewsFragment : BaseFragment<FragmentBreakingNewsBinding>() {

  private val viewModel: BreakingNewsViewModel by viewModels()

  private lateinit var articlesAdapter: NewsAdapter
  private lateinit var scrollListener: EndlessRecyclerViewScrollListener

  override
  fun getLayoutId() = R.layout.fragment_breaking_news

  override
  fun setBindingVariables() {
    binding.includedList.baseViewModel = viewModel
  }

  override
  fun setUpViews() {
    setUpRecyclerView()

    initSwipeRefreshLayout()
  }

  override
  fun observeAPICall() {
    getBreakingNews()
  }

  private fun setUpRecyclerView() {
    articlesAdapter = NewsAdapter { onArticleClick(it) }
    binding.includedList.recyclerView.apply {
      setHasFixedSize(true)
      layoutManager = LinearLayoutManager(requireContext())
      adapter = articlesAdapter

      initPaging(layoutManager as LinearLayoutManager)
    }
  }

  private fun initPaging(layoutManager: LinearLayoutManager) {
    scrollListener = object : EndlessRecyclerViewScrollListener(3, layoutManager) {
      override
      fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView) {
        if (viewModel.shouldLoadMore) {
          viewModel.shouldLoadMore = false
          viewModel.isLoading = true

          getBreakingNews()
        }
      }
    }
    binding.includedList.recyclerView.addOnScrollListener(scrollListener)
  }

  private fun getBreakingNews() {
    viewModel.getBreakingNews("us").observe(this, {
      binding.swipeRefresh.isRefreshing = false

      when (it) {
        is Loading -> {
          if (articlesAdapter.currentList.isNullOrEmpty()) {
            viewModel.dataLoadingEvent.value = DataStatus.LOADING
          } else {
            viewModel.dataLoadingEvent.value = DataStatus.LOADING_NEXT_PAGE
          }
        }
        is Empty -> {
          if (articlesAdapter.currentList.isNullOrEmpty()) {
            viewModel.dataLoadingEvent.value = DataStatus.NO_DATA
          }
        }
        is Success -> {
          if (it.value.articles.size != BaseRemoteDataSource.LIST_PAGE_SIZE) {
            viewModel.shouldLoadMore = false
          } else {
            viewModel.shouldLoadMore = true
            viewModel.page += 1
          }

          if (articlesAdapter.currentList.isNullOrEmpty()) {
            articlesAdapter.submitList(it.value.articles)
          } else {
            articlesAdapter.submitList(articlesAdapter.currentList + it.value.articles)
          }

          viewModel.dataLoadingEvent.value = DataStatus.SHOW_DATA
        }
        is Failure -> {
          handleApiError(it)
        }
      }
    })
  }

  private fun initSwipeRefreshLayout() {
    binding.swipeRefresh.setOnRefreshListener { refreshData() }
    binding.swipeRefresh.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimaryDark, R.color.colorAccent)
  }

  private fun refreshData() {
    initPagingParameters()

    getBreakingNews()
  }

  private fun initPagingParameters() {
    viewModel.page = 1
    viewModel.isLoading = true
    viewModel.shouldLoadMore = false

    articlesAdapter.submitList(null)

    scrollListener.resetState()
  }

  private fun onArticleClick(article: Article) {
    val bundle = Bundle().apply {
      putSerializable("article", article)
    }
    findNavController().navigate(
      R.id.action_open_news_details_fragment,
      bundle
    )
  }
}