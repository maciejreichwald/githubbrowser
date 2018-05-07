package com.rudearts.githubbrowser.ui.main

import android.content.Context
import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.rudearts.githubbrowser.R
import com.rudearts.githubbrowser.databinding.RepoItemBinding
import com.rudearts.githubbrowser.domain.model.RepoItem
import com.rudearts.githubbrowser.domain.model.RepoItemType
import com.rudearts.githubbrowser.extentions.loadUrlThumb
import javax.inject.Inject


class RepoItemAdapter @Inject constructor(
        private val context: Context?) : RecyclerView.Adapter<RepoItemAdapter.RepoItemHolder>() {

    companion object {
        private const val DEFAULT_IMAGE_SIZE = 150
        private const val DEFAULT_PLACEHOLDER_ID = R.drawable.user
    }

    private val inflater by lazy { LayoutInflater.from(context) }

    private var items: List<RepoItem> = ArrayList()

    internal var listener: ((RepoItem?) ->Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            RepoItemHolder(getBinding(parent))

    override fun onBindViewHolder(holder: RepoItemHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    private fun getBinding(convertView: View?) = when (convertView) {
        null -> createViewBinding()
        else -> updateViewBinding(convertView) ?: createViewBinding()
    }

    fun updateItems(items: List<RepoItem>) {
        this.items = items
        notifyDataSetChanged()
    }

    override fun getItemCount() = items.size

    private fun getItem(position: Int) = items[position]

    private fun createViewBinding(): RepoItemBinding =
            DataBindingUtil.inflate(inflater, R.layout.repo_item, null, false)

    private fun updateViewBinding(convertView: View) =
            DataBindingUtil.getBinding<RepoItemBinding>(convertView)

    inner class RepoItemHolder(private val binding: RepoItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(repoItem: RepoItem) {
            with(binding) {
                item = repoItem
                setupType(type, item?.type)
                setupListener(root, item)
                loadImageResource(avatar, item?.avatar)
                executePendingBindings()
            }
        }

        private fun setupListener(root: View, item: RepoItem?) {
            root.setOnClickListener {
                listener?.let {
                    it(item)
                }
            }
        }

        private fun setupType(type: TextView, repoItemType: RepoItemType?) {
            context?.let {
                type.text = context.getString(getTextByType(repoItemType))
            }
        }

        private fun getTextByType(repoItemType: RepoItemType?) = when(repoItemType) {
            RepoItemType.USER -> R.string.user
            RepoItemType.REPOSITORY -> R.string.repository
            else -> R.string.unknown
        }

        private fun loadImageResource(avatar: ImageView, avatarUrl: String?) {
            avatar.setImageResource(DEFAULT_PLACEHOLDER_ID)
            avatarUrl?.let { url ->
                avatar.loadUrlThumb(DEFAULT_IMAGE_SIZE, DEFAULT_PLACEHOLDER_ID, url)
            }
        }
    }
}