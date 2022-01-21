package com.jin.android.indiestage.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jin.android.indiestage.data.firestore.ExhibitionRepository
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
    private val exhibitionRepository: ExhibitionRepository,
    private val exhibitionId: String = "",
    private val artWorkId: String = ""
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(exhibitionRepository, checkedInDataSource, bookMarkDataSource) as T
            }
            modelClass.isAssignableFrom(StageViewModel::class.java) -> {
                StageViewModel(bookMarkDataSource, exhibitionRepository, exhibitionId) as T
            }
            modelClass.isAssignableFrom(ArtWorkViewModel::class.java) -> {
                ArtWorkViewModel(exhibitionRepository, exhibitionId, artWorkId) as T
            }
            modelClass.isAssignableFrom(TicketBoxViewModel::class.java) -> {
                TicketBoxViewModel(checkedInDataSource, exhibitionRepository, exhibitionId) as T
            }
            modelClass.isAssignableFrom(QuickEnterViewModel::class.java) -> {
                QuickEnterViewModel(checkedInDataSource, exhibitionRepository) as T
            }
            modelClass.isAssignableFrom(ExhibitionsViewModel::class.java) -> {
                ExhibitionsViewModel(exhibitionRepository) as T
            }
            else -> throw IllegalStateException()
        }
    }
}