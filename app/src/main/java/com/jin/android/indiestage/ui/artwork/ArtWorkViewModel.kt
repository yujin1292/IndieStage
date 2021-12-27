package com.jin.android.indiestage.ui.artwork

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jin.android.indiestage.data.ArtWorkResponse
import com.jin.android.indiestage.data.ExhibitionRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ArtWorkViewModel(
    private val exhibitionRepo: ExhibitionRepo,
    private val exhibitionId: String,
    private val artWorkId: String
) : ViewModel() {

    val artWorkStateFlow = MutableStateFlow<ArtWorkResponse?>(null)
    init {
        viewModelScope.launch {
            exhibitionRepo.getArtWork(exhibitionId, artWorkId).collect {
                artWorkStateFlow.value = it
            }
        }
    }

    fun getArtWork() = exhibitionRepo.getArtWork(exhibitionId, artWorkId)
}