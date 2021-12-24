package com.jin.android.indiestage.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect

class HomeViewModel(
) : ViewModel() {
    val cover =
        "https://ia800703.us.archive.org/19/items/mbid-8328a210-879d-4c50-8c62-389d409eb843/mbid-8328a210-879d-4c50-8c62-389d409eb843-21449738392_thumb500.jpg"
    val cover2 =
        "https://ia801703.us.archive.org/31/items/mbid-f3c1f76d-d436-4402-8888-1fdcf9e6cba8/mbid-f3c1f76d-d436-4402-8888-1fdcf9e6cba8-27301517082_thumb500.jpg"
    val cover3 =
        "https://ia902607.us.archive.org/32/items/mbid-76df3287-6cda-33eb-8e9a-044b5e15ffdd/mbid-76df3287-6cda-33eb-8e9a-044b5e15ffdd-829521842_thumb500.jpg"
    val imageList = mutableListOf<String>(cover, cover2, cover3, cover, cover2, cover3)


    // Holds our view state which the UI collects via [state]
    private val _state = MutableStateFlow(HomeViewState())
    val state: StateFlow<HomeViewState>
        get() = _state


    private val selectedStage = MutableStateFlow(0)

    private val refreshing = MutableStateFlow(false)


    init {
        viewModelScope.launch {
            // Combines the latest value from each of the flows, allowing us to generate a
            // view state instance which only contains the latest values.
            combine(
                selectedStage,
                refreshing
            ) { selectedStage, refreshing ->
                HomeViewState(
                    selectedStage = selectedStage,
                    posters = imageList,
                    refreshing = refreshing,
                    errorMessage = null /* TODO */
                )
            }.catch { throwable ->
                // TODO: emit a UI error here. For now we'll just rethrow
                throw throwable
            }.collect{
                _state.value = it
            }
        }

        refresh(force = false)
    }


    private fun refresh(force: Boolean) {
        viewModelScope.launch {
            runCatching {
                refreshing.value = true
                //updatePodcasts
            }
            // TODO: look at result of runCatching and show any errors

            refreshing.value = false
        }
    }

    fun onHomeCategorySelected(artistID: Int) {
        selectedStage.value = artistID
    }

}
data class HomeViewState(
    val selectedStage : Int = 0,
    val posters: List<String> = emptyList(),
    val refreshing: Boolean = false,
    val errorMessage: String? = null
)
