package com.jin.android.indiestage.ui.exhibitions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jin.android.indiestage.data.firestore.ExhibitionRepository
import com.jin.android.indiestage.data.firestore.ExhibitionResponse
import com.jin.android.indiestage.data.firestore.OnError
import com.jin.android.indiestage.ui.home.HomeViewState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class ExhibitionsViewModel(exhibitionRepository: ExhibitionRepository) : ViewModel() {

    lateinit var openedExhibitionFlow : Flow<ExhibitionResponse>
    lateinit var readyExhibitionFlow :  Flow<ExhibitionResponse>

    private val _state = MutableStateFlow(ExhibitionsScreenState())
    val state: StateFlow<ExhibitionsScreenState>
        get() = _state

    init {
        viewModelScope.launch {
            exhibitionRepository.run {
                openedExhibitionFlow = getOpenedExhibitions()
                readyExhibitionFlow = getClosedExhibitions()
            }

            combine(
                openedExhibitionFlow,
                readyExhibitionFlow
            ){ open, ready ->
                ExhibitionsScreenState(
                    openedExhibitionFlow = open,
                    readyExhibitionFlow = ready
                )
            }.catch { throwable ->
                throwable.printStackTrace()
                throw throwable
            }.collect {
                _state.value = it
            }
        }
    }
}

data class ExhibitionsScreenState(
    var readyExhibitionFlow: ExhibitionResponse = OnError(null),
    var openedExhibitionFlow: ExhibitionResponse = OnError(null),
)