package com.jin.android.indiestage.data.firestore

import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot

data class Artist(
    val id: String,
    val image:String,
    val name: String,
    val intro: String,
    val email:String,
    val homepage:String,
    val history:String
){
    constructor():this("","","","","","","")
}

sealed class ArtistResponse
data class ArtistOnSuccess(val querySnapshot: QuerySnapshot?): ArtistResponse()
data class ArtistOnError(val exception: FirebaseFirestoreException?): ArtistResponse()