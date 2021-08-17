package com.mina_mikhail.newsapp.features.news.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mina_mikhail.newsapp.R
import com.mina_mikhail.newsapp.core.utils.DateUtils
import com.mina_mikhail.newsapp.core.utils.convertDateTimeToTimesAgo
import com.mina_mikhail.newsapp.core.view.extensions.loadRoundImage
import com.mina_mikhail.newsapp.databinding.ItemNewsBinding
import com.mina_mikhail.newsapp.features.news.domain.entity.model.Article
import com.mina_mikhail.newsapp.features.news.presentation.NewsAdapter.ArticlesViewHolder

class NewsAdapter(private var itemClick: (Article) -> Unit) : ListAdapter<Article, ArticlesViewHolder>(DIFF_CALLBACK) {

  companion object {
    private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Article>() {
      override
      fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean =
        oldItem.url == newItem.url

      override
      fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean =
        oldItem == newItem
    }
  }

  override
  fun onCreateViewHolder(
    parent: ViewGroup,
    viewType: Int
  ): ArticlesViewHolder {
    val root = LayoutInflater.from(parent.context).inflate(R.layout.item_news, parent, false)
    return ArticlesViewHolder(root)
  }

  override
  fun onBindViewHolder(holder: ArticlesViewHolder, position: Int) {
    val item = getItem(position)
    holder.bind(item)
  }

  fun getItemByPosition(position: Int): Article = getItem(position)

  inner class ArticlesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val binding = ItemNewsBinding.bind(itemView)
    private var currentItem: Article? = null

    init {
      binding.item.setOnClickListener {
        currentItem?.let {
          itemClick(it)
        }
      }
    }

    fun bind(item: Article) {
      currentItem = item

      binding.ivArticleImage.loadRoundImage(item.urlToImage)
      binding.tvArticleDate.text = item.publishedAt.convertDateTimeToTimesAgo(DateUtils.FULL_DATE_TIME_FORMAT)
      binding.tvArticleTitle.text = item.title
      binding.tvArticleDescription.text = item.description
    }
  }
}