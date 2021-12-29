package com.jin.android.indiestage.ui.ticketbox

import androidx.lifecycle.ViewModel
import com.jin.android.indiestage.data.ExhibitionRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class TicketBoxViewModel(
    private val exhibitionRepo: ExhibitionRepo,
    private val exhibitionId:String
) : ViewModel() {  // QR코드 일치 처리

    private val _requestCameraPermission: MutableStateFlow<Boolean> =  MutableStateFlow(true)
    val requestCameraPermission = _requestCameraPermission.asStateFlow()

    fun setRequestCameraPermission(request: Boolean) {
        _requestCameraPermission.value = request
    }

}