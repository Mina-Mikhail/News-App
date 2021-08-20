package com.mina_mikhail.newsapp.features.news.presentation.breaking_news

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mina_mikhail.newsapp.R
import com.mina_mikhail.newsapp.core.data_source.BaseRemoteDataSource
import com.mina_mikhail.newsapp.core.network.Resource.Empty
import com.mina_mikhail.newsapp.core.network.Resource.Failure
import com.mina_mikhail.newsapp.core.network.Resource.Loading
import com.mina_mikhail.newsapp.core.network.Resource.Success
import com.mina_mikhail.newsapp.core.utils.EndlessRecyclerViewScrollListener
import com.mina_mikhail.newsapp.core.view.BaseFragment
import com.mina_mikhail.newsapp.core.view.extensions.handleApiError
import com.mina_mikhail.newsapp.core.view.extensions.hide
import com.mina_mikhail.newsapp.core.view.extensions.show
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
  fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
    FragmentBreakingNewsBinding.inflate(inflater, container, false)

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
            showDataLoading()
          } else {
            showPaginationLoading()
          }
        }
        is Empty -> {
          if (articlesAdapter.currentList.isNullOrEmpty()) {
            showNoData()
          } else {
            hidePaginationLoading()
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

          showData()
        }
        is Failure -> {
          if (articlesAdapter.currentList.isNullOrEmpty()) {
            handleApiError(it, noDataAction = { showNoData() }, noInternetAction = { showNoInternet() })
          } else {
            handleApiError(it)
            hidePaginationLoading()
          }
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

  private fun showDataLoading() {
    binding.includedList.container.show()
    binding.includedList.progressBar.show()
    binding.includedList.emptyViewContainer.hide()
    binding.includedList.internetErrorViewContainer.hide()
    binding.includedList.recyclerView.hide()
    binding.includedList.paginationProgressBar.hide()
  }

  private fun showPaginationLoading() {
    binding.includedList.paginationProgressBar.show()
  }

  private fun hidePaginationLoading() {
    binding.includedList.paginationProgressBar.hide()
  }

  private fun showData() {
    binding.includedList.recyclerView.show()
    binding.includedList.container.hide()
    binding.includedList.paginationProgressBar.hide()
  }

  private fun showNoData() {
    binding.includedList.container.show()
    binding.includedList.emptyViewContainer.show()
    binding.includedList.internetErrorViewContainer.hide()
    binding.includedList.progressBar.hide()
    binding.includedList.paginationProgressBar.hide()
    binding.includedList.recyclerView.hide()
  }

  private fun showNoInternet() {
    binding.includedList.container.show()
    binding.includedList.internetErrorViewContainer.show()
    binding.includedList.emptyViewContainer.hide()
    binding.includedList.progressBar.hide()
    binding.includedList.paginationProgressBar.hide()
    binding.includedList.recyclerView.hide()
  }
}