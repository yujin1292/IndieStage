package com.jin.android.indiestage.ui.artwork

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jin.android.indiestage.data.firestore.ArtWorkResponse
import com.jin.android.indiestage.data.firestore.FireStoreRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class ArtWorkViewModel(
    private val fireStoreRepository: FireStoreRepository,
    private val exhibitionId: String,
    private val artWorkId: String
) : ViewModel() {

    val artWorkStateFlow = MutableStateFlow<ArtWorkResponse?>(null)
    init {
        viewModelScope.launch {
            fireStoreRepository.getArtWork(exhibitionId, artWorkId).collect {
                artWorkStateFlow.value = it
            }
        }
    }
}