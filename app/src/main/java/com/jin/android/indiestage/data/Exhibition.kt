package com.jin.android.indiestage.data

import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot

data class Exhibition (
    val id : String,
    val isOpened:Boolean,
    val title: String,
    val image: String,
    val description:String,
    val artist:Artist,
    val artwork:ArtWork,
    val enterCode: String
){
    constructor():this("", true,"","","",Artist(),ArtWork(),"")
}

sealed class ExhibitionResponse
data class OnSuccess(val querySnapshot: QuerySnapshot?): ExhibitionResponse()
data class OnError(val exception: FirebaseFirestoreException?): ExhibitionResponse()


sealed class EnterCodeResponse
data class EnterCodeOnSuccess(val enterCode:String): EnterCodeResponse()
data class EnterCodeOnError(val exception: FirebaseFirestoreException?): EnterCodeResponse()