package com.mina_mikhail.newsapp.features.news.presentation.saved_news

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mina_mikhail.newsapp.R
import com.mina_mikhail.newsapp.core.utils.SwipeToDeleteCallback
import com.mina_mikhail.newsapp.core.view.BaseFragment
import com.mina_mikhail.newsapp.core.view.extensions.showError
import com.mina_mikhail.newsapp.databinding.FragmentSavedNewsBinding
import com.mina_mikhail.newsapp.features.news.domain.entity.model.Article
import com.mina_mikhail.newsapp.features.news.presentation.NewsAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SavedNewsFragment : BaseFragment<FragmentSavedNewsBinding>() {

  private val viewModel: SavedNewsViewModel by viewModels()

  private lateinit var articlesAdapter: NewsAdapter

  override
  fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
    FragmentSavedNewsBinding.inflate(inflater, container, false)

  override
  fun setUpViews() {
    setUpRecyclerView()

    getSavedNews()
  }

  private fun setUpRecyclerView() {
    articlesAdapter = NewsAdapter { onArticleClick(it) }
    binding.recyclerView.apply {
      setHasFixedSize(true)
      layoutManager = LinearLayoutManager(requireContext())
      adapter = articlesAdapter
    }

    val itemTouchHelper = ItemTouchHelper(object : SwipeToDeleteCallback(requireContext()) {
      override
      fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val article = articlesAdapter.getItemByPosition(viewHolder.adapterPosition)
        viewModel.deleteArticleFromLocal(article)

        showError(
          resources.getString(R.string.article_removed),
          resources.getString(R.string.undo)
        ) { viewModel.saveArticleToLocal(article) }
      }
    })
    itemTouchHelper.attachToRecyclerView(binding.recyclerView)
  }

  private fun getSavedNews() {
    viewModel.getArticlesFromLocal().observe(this, { articlesAdapter.submitList(it) })
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