package com.sysiphusj.aop.part02.book_review.api

import com.sysiphusj.aop.part02.book_review.model.SearchBookDto
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface BookAPI {

    @GET("v1/search/book.json")

    fun getSearchBook(
        @Header("X-Naver-Client-Id") clientId: String,
        @Header("X-Naver-Client-Secret") clientSecret: String,
        @Query("query") query: String,
        @Query("display") display: Int? = null,
        @Query("start") start: Int? = null,
    ): Call<SearchBookDto>

}