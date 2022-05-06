package com.jin.android.indiestage.data.firestore

import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot

data class Banner(
    val text: String = "",
    val image: String = ""
)

sealed class BannerResponse
data class BannerOnSuccess(val data: QuerySnapshot?) : BannerResponse()
data class BannerOnError(val exception: FirebaseFirestoreException?) : BannerResponse()