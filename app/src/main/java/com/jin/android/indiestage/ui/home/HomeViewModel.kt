package com.jin.android.indiestage.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jin.android.indiestage.data.firestore.ExhibitionRepository
import com.jin.android.indiestage.data.firestore.ExhibitionResponse
import com.jin.android.indiestage.data.firestore.OnError
import com.jin.android.indiestage.data.room.BookMarkDataSource
import com.jin.android.indiestage.data.room.CheckedInDataSource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class HomeViewModel(
    private val exhibitionRepository: ExhibitionRepository,
    checkedInDataSource: CheckedInDataSource?,
    bookMarkDataSource: BookMarkDataSource?
) : ViewModel() {

    private lateinit var openedExhibitionFlow: Flow<ExhibitionResponse>
    private lateinit var closedExhibitionFlow: Flow<ExhibitionResponse>

    private val categories = MutableStateFlow(HomeCategory.values().asList())
    private val selectedCategory = MutableStateFlow(HomeCategory.Exhibition)

    private val _state = MutableStateFlow(HomeViewState())
    val state: StateFlow<HomeViewState>
        get() = _state

    val checkedInList = checkedInDataSource!!.getAllCheckedInData()
    val bookmarkedList = bookMarkDataSource!!.getAllBookMarked()

    init {
        viewModelScope.launch {
            openedExhibitionFlow = exhibitionRepository.getOpenedExhibitions()
            closedExhibitionFlow = exhibitionRepository.getClosedExhibitions()

            combine(
                categories,
                selectedCategory,
                openedExhibitionFlow,
                closedExhibitionFlow
            ) { categories, selectedCategory, openedExhibitionFlow, closedExhibitionFlow ->
                HomeViewState(
                    homeCategories = categories,
                    selectedHomeCategory = selectedCategory,
                    openedExhibitionFlow = openedExhibitionFlow,
                    closedExhibitionFlow = closedExhibitionFlow,
                    errorMessage = null /* TODO */
                )
            }.catch { throwable ->
                throw throwable
            }.collect {
                _state.value = it
            }
        }

    }

    fun onHomeCategorySelected(category: HomeCategory) {
        selectedCategory.value = category
    }

}
enum class HomeCategory {
    Exhibition, My
}

data class HomeViewState(
    val refreshing: Boolean = false,
    val selectedHomeCategory: HomeCategory = HomeCategory.Exhibition,
    val homeCategories: List<HomeCategory> = emptyList(),
    var closedExhibitionFlow: ExhibitionResponse = OnError(null),
    var openedExhibitionFlow: ExhibitionResponse = OnError(null),
    val errorMessage: String? = null
)
