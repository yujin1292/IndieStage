package com.jin.android.indiestage.ui.stage

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jin.android.indiestage.data.firestore.*
import com.jin.android.indiestage.data.room.BookMarkDataSource
import com.jin.android.indiestage.data.room.ExhibitionEntity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class StageViewModel(
    private val bookMarkDataSource: BookMarkDataSource?,
    private val fireStoreRepository: FireStoreRepository,
    private val exhibitionId: String
) : ViewModel() {

    private val _state = MutableStateFlow(StageViewState())
    val state: StateFlow<StageViewState>
        get() = _state

    private lateinit var exhibitionFlow: Flow<ExhibitionResponse>
    private lateinit var artistInfoFlow: Flow<ArtistResponse>
    private lateinit var artWorkInfoFlow: Flow<ArtWorksResponse>

    var exhibitionEntity = MutableLiveData(ExhibitionEntity())

    init {
        viewModelScope.launch {
            exhibitionId.run {
                exhibitionFlow = fireStoreRepository.getExhibitionsById(this)
                artistInfoFlow = fireStoreRepository.getArtist(this)
                artWorkInfoFlow = fireStoreRepository.getArtWorks(this)
            }
            combine(
                exhibitionFlow,
                artistInfoFlow,
                artWorkInfoFlow
            ) { exhibition, artist, artWorkInfo ->
                StageViewState(exhibition, artist, artWorkInfo)
            }.catch { t -> throw t }.collect {
                _state.value = it

                it.exhibitionFlow.let { exhibitionResponse ->
                    if (exhibitionResponse is OnSuccess) {
                        val exhibition =
                            exhibitionResponse.querySnapshot?.toObjects(Exhibition::class.java)
                                ?.get(0)

                        if (exhibition != null) {
                            bookMarkDataSource?.getEntity(exhibitionId)?.let { arr ->
                                exhibitionEntity.value =
                                    if (arr.isEmpty()) ExhibitionEntity(exhibition)
                                    else arr[0]
                            }
                        }

                    }

                }
            }
        }
    }

    fun clickBookMark() {
        viewModelScope.launch {
            exhibitionEntity.value?.let {
                bookMarkDataSource?.setBookmark(it)
            }
        }
    }
}

data class StageViewState(
    var exhibitionFlow: ExhibitionResponse = OnError(null),
    var artistInfoFlow: ArtistResponse = ArtistOnError(null),
    var artWorkInfoFlow: ArtWorksResponse = ArtWorksOnError(null)
)