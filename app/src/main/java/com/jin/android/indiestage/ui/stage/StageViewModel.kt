package com.jin.android.indiestage.ui.stage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jin.android.indiestage.data.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class StageViewModel(
    private val exhibitionRepo: ExhibitionRepo,
    private val exhibitionId:String
) : ViewModel() {

    val exhibitionStateFlow = MutableStateFlow<ExhibitionResponse?>(null)
    val artistStateFlow = MutableStateFlow<ArtistResponse?>(null)
    val artWorkStateFlow = MutableStateFlow<ArtWorkResponse?>(null)
    init {
        viewModelScope.launch {
            exhibitionRepo.getExhibitionsById(exhibitionId).collect {
                exhibitionStateFlow.value = it
            }

            /*exhibitionRepo.getArtist(exhibitionId).collect{
                artistStateFlow.value = it
            }*/
        }
    }

    fun getExhibitionInfo() = exhibitionRepo.getExhibitions()
    fun getArtistInfo() = exhibitionRepo.getArtist(exhibitionId)
    fun getArtWorkInfo() = exhibitionRepo.getArtWorks(exhibitionId)

    var exhibition = Exhibition()
    var artistInformation = Artist()
    var artWorkList = mutableListOf<ArtWork>()

}

