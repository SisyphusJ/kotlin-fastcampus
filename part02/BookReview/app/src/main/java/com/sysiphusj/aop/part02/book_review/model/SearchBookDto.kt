package com.sysiphusj.aop.part02.book_review.model

import com.google.gson.annotations.SerializedName

data class SearchBookDto(
    @SerializedName("total") var total: Int = 0, // 검색 결과 문서의 총 개수
    @SerializedName("start") var start: Int = 0, // 검색 결과 문서 중, 문서의 시작점
    @SerializedName("display") var display: Int = 0, // 검색된 결과의 개수
    @SerializedName("items") var items: List<Book>, // 개별 검색 결과
)



