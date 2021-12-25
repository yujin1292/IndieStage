package com.jin.android.indiestage.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jin.android.indiestage.data.ExhibitionRepo
import com.jin.android.indiestage.ui.home.HomeViewModel
import com.jin.android.indiestage.ui.stage.StageViewModel

class HomeViewModelFactory(private val exhibitionRepo: ExhibitionRepo) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(exhibitionRepo) as T
        }
        throw IllegalStateException()
    }
}

class StageViewModelFactory(
    private val exhibitionRepo: ExhibitionRepo,
    private val exhibitionId: String
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StageViewModel::class.java)) {
            return StageViewModel(exhibitionRepo, exhibitionId) as T
        }
        throw IllegalStateException()
    }
}