package com.sysiphusj.aop.part02.book_review.adpater

import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sysiphusj.aop.part02.book_review.databinding.ItemHistoryBinding
import com.sysiphusj.aop.part02.book_review.model.History

class HistoryAdapter(val historyDeleteClickedListener: (String) -> (Unit)) : ListAdapter<History, HistoryAdapter.HistoryItemViewHolder>(diffUtil) {

    inner class HistoryItemViewHolder(private val binding: ItemHistoryBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(historyModel: History) {
            binding.historyKeywordTextView.text = historyModel.keyword

            binding.historyKeywordDeleteButton.setOnClickListener {
                historyDeleteClickedListener(historyModel.keyword.orEmpty())
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryItemViewHolder {
        return HistoryItemViewHolder(ItemHistoryBinding.inflate(LayoutInflater.from(parent.context),
            parent,
            false))
    }

    override fun onBindViewHolder(holder: HistoryAdapter.HistoryItemViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    // diffUtil = 리사이클러뷰가 실제로 뷰의 포지션이 변경되었을 때 새로운 값을 할당할지 말지 정할 기준이 있는데
    //            같은 아이템이 올라오면 유지할지 변경할지 판단해준다.
    companion object {
        val diffUtil = object  : DiffUtil.ItemCallback<History>() {
            override fun areItemsTheSame(oldItem: History, newItem: History): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: History, newItem: History): Boolean {
                return oldItem.keyword == newItem.keyword
            }

        }
    }
}