package com.jin.android.indiestage.ui.home

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class HomeViewModel(
) : ViewModel() {
    val cover =
        "https://ia800703.us.archive.org/19/items/mbid-8328a210-879d-4c50-8c62-389d409eb843/mbid-8328a210-879d-4c50-8c62-389d409eb843-21449738392_thumb500.jpg"
    val cover2 =
        "https://ia801703.us.archive.org/31/items/mbid-f3c1f76d-d436-4402-8888-1fdcf9e6cba8/mbid-f3c1f76d-d436-4402-8888-1fdcf9e6cba8-27301517082_thumb500.jpg"
    val cover3 =
        "https://ia902607.us.archive.org/32/items/mbid-76df3287-6cda-33eb-8e9a-044b5e15ffdd/mbid-76df3287-6cda-33eb-8e9a-044b5e15ffdd-829521842_thumb500.jpg"



    // Holds our view state which the UI collects via [state]
    private val _state = MutableStateFlow(HomeViewState())

    private val refreshing = MutableStateFlow(false)

    val state: StateFlow<HomeViewState>
        get() = _state


    val imageList = mutableListOf<String>(cover, cover2, cover3, cover, cover2, cover3)

}
data class HomeViewState(
    val refreshing: Boolean = false,
    val errorMessage: String? = null
)
