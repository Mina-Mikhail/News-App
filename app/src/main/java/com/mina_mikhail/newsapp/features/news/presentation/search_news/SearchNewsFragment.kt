package com.mina_mikhail.newsapp.features.news.presentation.search_news

import android.os.Bundle
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.TextView
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
import com.mina_mikhail.newsapp.core.utils.SearchEditTextListener
import com.mina_mikhail.newsapp.core.view.BaseFragment
import com.mina_mikhail.newsapp.core.view.extensions.handleApiError
import com.mina_mikhail.newsapp.core.view.extensions.hide
import com.mina_mikhail.newsapp.core.view.extensions.hideKeyboard
import com.mina_mikhail.newsapp.core.view.extensions.show
import com.mina_mikhail.newsapp.databinding.FragmentSearchNewsBinding
import com.mina_mikhail.newsapp.features.news.domain.entity.model.Article
import com.mina_mikhail.newsapp.features.news.presentation.NewsAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchNewsFragment : BaseFragment<FragmentSearchNewsBinding>() {

  private val viewModel: SearchNewsViewModel by viewModels()

  private lateinit var articlesAdapter: NewsAdapter
  private lateinit var scrollListener: EndlessRecyclerViewScrollListener
  private lateinit var searchTextListener: SearchEditTextListener

  override
  fun getLayoutId() = R.layout.fragment_search_news

  override
  fun registerListeners() {
    startSearchListener()
  }

  override
  fun unRegisterListeners() {
    stopSearchListener()
  }

  override
  fun setBindingVariables() {
    binding.viewModel = viewModel
    binding.includedList.baseViewModel = viewModel
  }

  override
  fun setUpViews() {
    initSearchListener()

    setUpRecyclerView()

    handleSearchArea()
  }

  private fun initSearchListener() {
    searchTextListener = SearchEditTextListener(lifecycle) { searchQuery ->
      searchQuery?.let {
        if (it.isNotEmpty()) {
          hideSearchHintView()

          initPagingParameters()

          viewModel.searchQuery = it
          searchForNews()
        } else {
          showSearchHintView()
        }
      }
    }
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

  private fun startSearchListener() {
    binding.etSearch.addTextChangedListener(searchTextListener)
  }

  private fun stopSearchListener() {
    binding.etSearch.removeTextChangedListener(searchTextListener)
  }

  private fun handleSearchArea() {
    binding.etSearch.setOnEditorActionListener(object : TextView.OnEditorActionListener {
      override
      fun onEditorAction(p0: TextView?, actionId: Int, p2: KeyEvent?): Boolean {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
          hideKeyboard()
          binding.etSearch.clearFocus()
          return true
        }

        return false
      }
    })
  }

  private fun initPaging(layoutManager: LinearLayoutManager) {
    scrollListener = object : EndlessRecyclerViewScrollListener(3, layoutManager) {
      override
      fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView) {
        if (viewModel.shouldLoadMore) {
          viewModel.shouldLoadMore = false
          viewModel.isLoading = true

          searchForNews()
        }
      }
    }
    binding.includedList.recyclerView.addOnScrollListener(scrollListener)
  }

  private fun searchForNews() {
    viewModel.searchForNews().observe(this, {

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
            hideKeyboard()
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

  private fun initPagingParameters() {
    viewModel.searchQuery = ""
    viewModel.page = 1
    viewModel.isLoading = true
    viewModel.shouldLoadMore = false

    articlesAdapter.submitList(null)

    scrollListener.resetState()
  }

  override
  fun setupObservers() {
    viewModel.clearSearchArea.observe(this, {
      clearSearchArea()
    })
  }

  private fun clearSearchArea() {
    initPagingParameters()
    hideKeyboard()
    binding.etSearch.setText("")
    binding.etSearch.clearFocus()
  }

  private fun onArticleClick(article: Article) {
    hideKeyboard()
    binding.etSearch.clearFocus()

    val bundle = Bundle().apply {
      putSerializable("article", article)
    }
    findNavController().navigate(
      R.id.action_open_news_details_fragment,
      bundle
    )
  }

  private fun showSearchHintView() {
    binding.searchHint.show()
    binding.listContainer.hide()
    binding.btnDismissSearch.hide()
  }

  private fun hideSearchHintView() {
    binding.listContainer.show()
    binding.btnDismissSearch.show()
    binding.searchHint.hide()
  }
}