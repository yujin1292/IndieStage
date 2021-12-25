package com.jin.android.indiestage.data

import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot

data class ArtWork(
    val id: String,
    val title: String,
    val pageNum:Int,
    val text:List<String>,
    val image:List<String>
){
    constructor():this("","",0, mutableListOf(), mutableListOf())
}


sealed class ArtWorkResponse
data class ArtWorkOnSuccess(val querySnapshot: QuerySnapshot?): ArtWorkResponse()
data class ArtWorkOnError(val exception: FirebaseFirestoreException?): ArtWorkResponse()