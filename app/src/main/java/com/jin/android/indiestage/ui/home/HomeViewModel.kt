package com.jin.android.indiestage.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jin.android.indiestage.data.*
import com.jin.android.indiestage.data.checkedin.CheckedInDataSource
import com.jin.android.indiestage.data.checkedin.CheckedInEntity
import com.jin.android.indiestage.data.checkedin.CheckedInRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class HomeViewModel(
    private val exhibitionRepo: ExhibitionRepo,
    private val checkedInDataSource: CheckedInDataSource?
) : ViewModel() {

    private lateinit var openedExhibitionFlow: Flow<ExhibitionResponse>
    private lateinit var closedExhibitionFlow: Flow<ExhibitionResponse>

    private val categories = MutableStateFlow(HomeCategory.values().asList())
    private val selectedCategory = MutableStateFlow(HomeCategory.Exhibition)

    private val _state = MutableStateFlow(HomeViewState())
    val state: StateFlow<HomeViewState>
        get() = _state

    val checkedInList = checkedInDataSource!!.getAllData()

    init {
        viewModelScope.launch {
            openedExhibitionFlow = exhibitionRepo.getOpenedExhibitions()
            closedExhibitionFlow = exhibitionRepo.getClosedExhibitions()

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
                // TODO: emit a UI error here. For now we'll just rethrow
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
    Exhibition, CheckedIn
}

data class HomeViewState(
    val refreshing: Boolean = false,
    val selectedHomeCategory: HomeCategory = HomeCategory.Exhibition,
    val homeCategories: List<HomeCategory> = emptyList(),
    var closedExhibitionFlow: ExhibitionResponse = OnError(null),
    var openedExhibitionFlow: ExhibitionResponse = OnError(null),
    val errorMessage: String? = null
)
