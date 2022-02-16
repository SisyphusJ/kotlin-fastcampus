package com.sysiphusj.aop.part02.book_review.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

// 클래스 직렬화를 해서 인텐트에 클래스 자체를 보낸다....
@Parcelize
data class Book(
    @SerializedName("isbn") var isbn: String = "", // 유통과정 상의 책 고유의 넘버
    @SerializedName("title") var title: String = "", // 제목
    @SerializedName("link") var link: String = "", // 검색 결과 문서의 하이퍼텍스트 link
    @SerializedName("image") var image: String = "", // 썸네일 이미지의 URL
    @SerializedName("author") var author: String = "", // 저자
    @SerializedName("price") var price: Int = 0, // 정가
    @SerializedName("discount") var discount: Int = 0, // 할인가
    @SerializedName("publisher") var publisher: String = "", // 출판사
    @SerializedName("pubdate") var pubdate: String = "", // 출간일
    @SerializedName("description") var description: String = "", // 요약
): Parcelable