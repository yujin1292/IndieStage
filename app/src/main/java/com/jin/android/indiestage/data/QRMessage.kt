package com.jin.android.indiestage.data

data class QRMessage(
    val id: String,
    val enterCode: String
){
    constructor():this("","")
}