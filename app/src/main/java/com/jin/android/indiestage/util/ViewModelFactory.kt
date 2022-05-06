package com.jin.android.indiestage.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jin.android.indiestage.data.firestore.FireStoreRepository
import com.jin.android.indiestage.data.room.BookMarkDataSource
import com.jin.android.indiestage.data.room.CheckedInDataSource
import com.jin.android.indiestage.ui.artwork.ArtWorkViewModel
import com.jin.android.indiestage.ui.exhibitions.ExhibitionsViewModel
import com.jin.android.indiestage.ui.home.HomeViewModel
import com.jin.android.indiestage.ui.quickenter.QuickEnterViewModel
import com.jin.android.indiestage.ui.stage.StageViewModel
import com.jin.android.indiestage.ui.ticketbox.TicketBoxViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class ViewModelFactory(
    private val checkedInDataSource: CheckedInDataSource? = null,
    private val bookMarkDataSource: BookMarkDataSource? = null,
    private val fireStoreRepository: FireStoreRepository,
    private val exhibitionId: String = "",
    private val artWorkId: String = ""
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(fireStoreRepository, checkedInDataSource, bookMarkDataSource) as T
            }
            modelClass.isAssignableFrom(StageViewModel::class.java) -> {
                StageViewModel(bookMarkDataSource, fireStoreRepository, exhibitionId) as T
            }
            modelClass.isAssignableFrom(ArtWorkViewModel::class.java) -> {
                ArtWorkViewModel(fireStoreRepository, exhibitionId, artWorkId) as T
            }
            modelClass.isAssignableFrom(TicketBoxViewModel::class.java) -> {
                TicketBoxViewModel(checkedInDataSource, fireStoreRepository, exhibitionId) as T
            }
            modelClass.isAssignableFrom(QuickEnterViewModel::class.java) -> {
                QuickEnterViewModel(checkedInDataSource, fireStoreRepository) as T
            }
            modelClass.isAssignableFrom(ExhibitionsViewModel::class.java) -> {
                ExhibitionsViewModel(fireStoreRepository) as T
            }
            else -> throw IllegalStateException()
        }
    }
}