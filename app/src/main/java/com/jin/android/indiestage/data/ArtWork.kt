package com.jin.android.indiestage.data

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot

// 전시에 속해있는 작품  여러 페이지를 가진다.
data class ArtWork(
    val id: String,
    val title: String,
    val pageNum: Int,
    val contents: List<Content>
) {
    constructor() : this(
        "", "The First one", 1,
        mutableListOf(Content()))
}

data class Content(
    val image: String,
    val text: String
) {
    constructor() : this(
        image = "https://image.winudf.com/v2/image1/Y29tLnRpYW5iYXkuYXJpYW5hd2FsbHBhcGVyc19zY3JlZW5fMF8xNjAxNTQ1MTQzXzA5MA/screen-0.jpg?fakeurl=1&type=.jpg",
        text = "Ariana Albums\\nHello\\nGreat\\nWhat?"
    )
}

sealed class ArtWorksResponse
data class ArtWorksOnSuccess(val querySnapshot: QuerySnapshot?) : ArtWorksResponse()
data class ArtWorksOnError(val exception: FirebaseFirestoreException?) : ArtWorksResponse()


sealed class ArtWorkResponse
data class ArtWorkOnSuccess(val querySnapshot: DocumentSnapshot?) : ArtWorkResponse()
data class ArtWorkOnError(val exception: FirebaseFirestoreException?) : ArtWorkResponse()