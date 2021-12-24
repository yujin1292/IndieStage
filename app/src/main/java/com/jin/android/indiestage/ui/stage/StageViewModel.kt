package com.jin.android.indiestage.ui.stage

import androidx.lifecycle.ViewModel
import com.jin.android.indiestage.data.Exhibition

class StageViewModel() : ViewModel() {

    var exhibition = Exhibition("stageuri", "전시명", "imageUri", "name", "description")

    var artistInformation = ArtistInfo("artist", "yujin", "안녕하세요")
    var artWorkList = mutableListOf<ArtWorkInfo>(
        ArtWorkInfo("1", "uri", "one"),
        ArtWorkInfo("1", "uri", "two"),
        ArtWorkInfo("1", "uri", "three"),
        ArtWorkInfo("1", "uri", "four")
    )

}


data class ArtistInfo(
    val artistUri: String,
    val artistName: String,
    val Describe: String
)

data class ArtWorkInfo(
    val artworkUri: String,
    val tumbnailUri: String,
    val title: String
)