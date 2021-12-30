package com.jin.android.indiestage.ui.stage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jin.android.indiestage.data.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class StageViewModel(
    private val exhibitionRepo: ExhibitionRepo,
    private val exhibitionId: String
) : ViewModel() {

    private val _state = MutableStateFlow(StageViewState())
    val state: StateFlow<StageViewState>
        get() = _state

    private lateinit var exhibitionFlow: Flow<ExhibitionResponse>
    private lateinit var artistInfoFlow: Flow<ArtistResponse>
    private lateinit var artWorkInfoFlow: Flow<ArtWorksResponse>

    init {
        viewModelScope.launch {
            exhibitionId.run {
                exhibitionFlow = exhibitionRepo.getExhibitionsById(this)
                artistInfoFlow = exhibitionRepo.getArtist(this)
                artWorkInfoFlow = exhibitionRepo.getArtWorks(this)
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
}

data class StageViewState(
    var exhibitionFlow: ExhibitionResponse = OnError(null),
    var artistInfoFlow: ArtistResponse = ArtistOnError(null),
    var artWorkInfoFlow: ArtWorksResponse = ArtWorksOnError(null)
)