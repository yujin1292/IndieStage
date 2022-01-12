package com.jin.android.indiestage.ui.stage

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jin.android.indiestage.data.firestore.*
import com.jin.android.indiestage.data.room.BookMarkDataSource
import com.jin.android.indiestage.data.room.ExhibitionEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class StageViewModel(
    private val bookMarkDataSource: BookMarkDataSource?,
    private val exhibitionRepository: ExhibitionRepository,
    private val exhibitionId: String
) : ViewModel() {

    private val _state = MutableStateFlow(StageViewState())
    val state: StateFlow<StageViewState>
        get() = _state

    private lateinit var exhibitionFlow: Flow<ExhibitionResponse>
    private lateinit var artistInfoFlow: Flow<ArtistResponse>
    private lateinit var artWorkInfoFlow: Flow<ArtWorksResponse>

    lateinit var exhibitionEntity: LiveData<ExhibitionEntity>

    init {
        viewModelScope.launch {
            exhibitionId.run {
                exhibitionFlow = exhibitionRepository.getExhibitionsById(this)
                artistInfoFlow = exhibitionRepository.getArtist(this)
                artWorkInfoFlow = exhibitionRepository.getArtWorks(this)
                exhibitionEntity = bookMarkDataSource?.getEntity(this)!!
            }

            combine(
                exhibitionFlow,
                artistInfoFlow,
                artWorkInfoFlow
            ) { exhibition, artist, artWorkInfo ->
                StageViewState(exhibition, artist, artWorkInfo)
            }.catch { t -> throw t }.collect {
                _state.value = it
            }


        }
    }

    fun clickBookMark() {
        viewModelScope.launch {
            exhibitionEntity.value?.let {
                bookMarkDataSource?.setBookmark(it)
                it.isBookMarked = it.isBookMarked.not()
            }
        }
    }
}

data class StageViewState(
    var exhibitionFlow: ExhibitionResponse = OnError(null),
    var artistInfoFlow: ArtistResponse = ArtistOnError(null),
    var artWorkInfoFlow: ArtWorksResponse = ArtWorksOnError(null)
)