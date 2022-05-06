package com.jin.android.indiestage.data.firestore

import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot

data class Exhibition(
    val id: String,
    var isOpened: Boolean,
    val title: String,
    val image: String,
    val description: String,
    val artist: Artist,
    val artwork: ArtWork,
    val enterCode: String,
    val hashTags: List<String>
) {
    constructor() : this(
        "", false, "", "", "", Artist(), ArtWork(), "",
        listOf(
            "#hashtag",
            "#genre",
            " #IndieStage",
            "#hi",
            "#hashtag",
            "#genre",
            " #IndieStage"
        )
    )
}

sealed class ExhibitionResponse
data class OnSuccess(val querySnapshot: QuerySnapshot?) : ExhibitionResponse()
data class OnError(val exception: FirebaseFirestoreException?) : ExhibitionResponse()


sealed class EnterCodeResponse
data class EnterCodeOnSuccess(val enterCode: String) : EnterCodeResponse()
data class EnterCodeOnError(val exception: FirebaseFirestoreException?) : EnterCodeResponse()