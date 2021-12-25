package com.jin.android.indiestage.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jin.android.indiestage.data.ExhibitionRepo
import com.jin.android.indiestage.data.ExhibitionResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.collect

class HomeViewModel(
    private val exhibitionRepo: ExhibitionRepo
) : ViewModel() {
    val exhibitionStateFlow = MutableStateFlow<ExhibitionResponse?>(null)

    init {
        viewModelScope.launch {
            exhibitionRepo.getExhibitions().collect {
                exhibitionStateFlow.value = it
            }
        }
    }

    fun getExhibitionInfo() = exhibitionRepo.getExhibitions()
}