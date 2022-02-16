package com.sysiphusj.aop.part02.book_review.adpater

import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sysiphusj.aop.part02.book_review.databinding.ItemBookBinding
import com.sysiphusj.aop.part02.book_review.model.Book

class BookAdapter(private val itemClickedListener: (Book) -> Unit): ListAdapter<Book, BookAdapter.BookItemViewHolder>(diffUtil) {

    inner class BookItemViewHolder(private val binding: ItemBookBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(bookModel: Book) {
            binding.titleTextView.text = stripHtml(bookModel.title)
            binding.descriptionTextView.text = stripHtml(bookModel.description)

            binding.root.setOnClickListener {
                itemClickedListener(bookModel)
            }

            Glide
                .with(binding.coverImageView.context)
                .load(bookModel.image)
                .into(binding.coverImageView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookItemViewHolder {
        return BookItemViewHolder(ItemBookBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: BookItemViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    fun stripHtml(html : String): String {
        return Html.fromHtml(html).toString()
    }


    // diffUtil = 리사이클러뷰가 실제로 뷰의 포지션이 변경되었을 때 새로운 값을 할당할지 말지 정할 기준이 있는데
    //            같은 아이템이 올라오면 유지할지 변경할지 판단해준다.
    companion object {
        val diffUtil = object  : DiffUtil.ItemCallback<Book>() {
            override fun areItemsTheSame(oldItem: Book, newItem: Book): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Book, newItem: Book): Boolean {
                return oldItem.isbn == newItem.isbn
            }

        }
    }
}