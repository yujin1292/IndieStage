package com.jin.android.indiestage.data

import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot

data class Exhibition (
    val id : String,
    val title: String,
    val image: String,
    val description:String,
    val artist:Artist,
    val artwork:ArtWork
){
    constructor():this("","","","",Artist(),ArtWork())
}

sealed class ExhibitionResponse
data class OnSuccess(val querySnapshot: QuerySnapshot?): ExhibitionResponse()
data class OnError(val exception: FirebaseFirestoreException?): ExhibitionResponse()