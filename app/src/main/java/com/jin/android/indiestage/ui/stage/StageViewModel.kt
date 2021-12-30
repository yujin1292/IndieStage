package com.jin.android.indiestage.ui.stage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jin.android.indiestage.data.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class StageViewModel(
    private val exhibitionRepo: ExhibitionRepo,
    private val exhibitionId:String
) : ViewModel() {

    val exhibitionStateFlow = MutableStateFlow<ExhibitionResponse?>(null)
    init {
        viewModelScope.launch {
            exhibitionRepo.getExhibitionsById(exhibitionId).collect {
                exhibitionStateFlow.value = it
            }

        }
    }

    fun getArtistInfo() = exhibitionRepo.getArtist(exhibitionId)
    fun getArtWorkInfo() = exhibitionRepo.getArtWorks(exhibitionId)
}

