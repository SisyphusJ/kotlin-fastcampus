package com.sysiphusj.aop.part02.book_review

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.MotionEvent
import androidx.constraintlayout.widget.ConstraintSet
import androidx.recyclerview.widget.LinearLayoutManager
import com.sysiphusj.aop.part02.book_review.adpater.BookAdapter
import com.sysiphusj.aop.part02.book_review.api.BookAPI
import com.sysiphusj.aop.part02.book_review.databinding.ActivityMainBinding
import com.sysiphusj.aop.part02.book_review.model.Book
import com.sysiphusj.aop.part02.book_review.model.SearchBookDto
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.KeyException


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adpater: BookAdapter
    private lateinit var bookService: BookAPI

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initBookRecyclerView()

        binding.searchEditText.setOnKeyListener { v, KeyCode, event ->
            // 실제로 enter를 눌렀을 때는 줄 내리기와 입력 이벤트 2개가 존재하기 때문에 입력으로 설정한다.
            if (KeyCode == KeyEvent.KEYCODE_ENTER && event.action == MotionEvent.ACTION_DOWN) {
                search(binding.searchEditText.text.toString())
                return@setOnKeyListener true
            }

            return@setOnKeyListener false
        }

    }

    private fun search(keyword: String) {

        val query: String = keyword

        val retrofit = Retrofit.Builder()
            .baseUrl("https://openapi.naver.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        bookService = retrofit.create(BookAPI::class.java)

        bookService.getSearchBook(CLIENT_ID, CLIENT_SECRET, query)
            .enqueue(object : Callback<SearchBookDto> {
                override fun onResponse(
                    call: Call<SearchBookDto>,
                    response: Response<SearchBookDto>,
                ) {

                    if (response.isSuccessful.not()) {
                        Log.e(TAG, "NOT!! SUCCESS")
                        return
                    }

                    response.body()?.let {
                        Log.d(TAG, it.toString())

                        it.items.forEach { books ->
                            Log.d(TAG, books.toString())
                        }

                        adpater.submitList(it.items)
                    }


                }

                override fun onFailure(call: Call<SearchBookDto>, t: Throwable) {
                    Log.d("Error", t.toString())
                }

            })

    }

    private fun initBookRecyclerView() {
        adpater = BookAdapter()

        binding.bookRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.bookRecyclerView.adapter = adpater
    }

    companion object {
        private const val CLIENT_ID = "YKZsdNzQhwvnfaLYPeu0"
        private const val CLIENT_SECRET = "rB0O9VyY3i"
        private const val TAG = "MainActivity"
    }
}