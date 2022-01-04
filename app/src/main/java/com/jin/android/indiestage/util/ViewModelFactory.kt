package com.jin.android.indiestage.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jin.android.indiestage.data.ExhibitionRepo
import com.jin.android.indiestage.data.checkedin.CheckedInDataSource
import com.jin.android.indiestage.data.checkedin.CheckedInRepository
import com.jin.android.indiestage.ui.artwork.ArtWorkViewModel
import com.jin.android.indiestage.ui.home.HomeViewModel
import com.jin.android.indiestage.ui.quickenter.QuickEnterViewModel
import com.jin.android.indiestage.ui.stage.StageViewModel
import com.jin.android.indiestage.ui.ticketbox.TicketBoxViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class ViewModelFactory(
    private val checkedInDataSource: CheckedInDataSource? = null,
    private val exhibitionRepo: ExhibitionRepo,
    private val exhibitionId: String = "",
    private val artWorkId: String = ""
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(exhibitionRepo, checkedInDataSource) as T
            }
            modelClass.isAssignableFrom(StageViewModel::class.java) -> {
                StageViewModel(exhibitionRepo, exhibitionId) as T
            }
            modelClass.isAssignableFrom(ArtWorkViewModel::class.java) -> {
                ArtWorkViewModel(exhibitionRepo, exhibitionId, artWorkId) as T
            }
            modelClass.isAssignableFrom(TicketBoxViewModel::class.java) -> {
                TicketBoxViewModel(checkedInDataSource,exhibitionRepo, exhibitionId) as T
            }
            modelClass.isAssignableFrom(QuickEnterViewModel::class.java) -> {
                QuickEnterViewModel(checkedInDataSource,exhibitionRepo) as T
            }
            else -> throw IllegalStateException()
        }
    }
}