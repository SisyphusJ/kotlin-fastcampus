package com.sysiphusj.aop.part02.book_review

import android.os.Bundle
import android.text.Html
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.bumptech.glide.Glide
import com.sysiphusj.aop.part02.book_review.databinding.ActivityDetailBinding
import com.sysiphusj.aop.part02.book_review.model.Book

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "BookSearchDB"
        ).build()

        // 클래스 직렬화와 부가데이터에 클래스를 보내는 방법 설명
        val model = intent.getParcelableExtra<Book>("bookModel")

        binding.titleTextView.text = stripHtml(model?.title.orEmpty())
        binding.descriptionTextView.text = stripHtml(model?.description.orEmpty())

        Glide.with(binding.coverImageView.context)
            .load(model?.image.orEmpty())
            .into(binding.coverImageView)


    }

    private fun stripHtml(html: String): String {
        return Html.fromHtml(html).toString()
    }
}