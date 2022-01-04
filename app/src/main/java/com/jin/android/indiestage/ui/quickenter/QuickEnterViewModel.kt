package com.jin.android.indiestage.ui.quickenter

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jin.android.indiestage.data.*
import com.jin.android.indiestage.data.checkedin.CheckedInDataSource
import com.jin.android.indiestage.data.checkedin.CheckedInEntity
import com.jin.android.indiestage.ui.quickenter.QuickEnterState.*
import com.jin.android.indiestage.util.EnterCode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class QuickEnterViewModel(
    private val checkedInDataSource: CheckedInDataSource?,
    private val exhibitionRepo: ExhibitionRepo
) : ViewModel() {

    val _state = MutableStateFlow<QuickEnterState>(InitialState)
    val state: StateFlow<QuickEnterState>
        get() = _state
    var exhibitionId: String = ""
    fun verifyEnterCode(msg: QRMessage) {
        viewModelScope.launch {
            exhibitionRepo.getExhibitionsById(msg.id).collect { response ->
                when (response) {
                    is OnSuccess -> {
                        val exhibition =
                            response.querySnapshot?.toObjects(Exhibition::class.java)?.get(0)

                        exhibition?.let {
                            val isMatched = EnterCode.verifyEnterCode(msg.enterCode, it.enterCode)
                            if (isMatched) {
                                checkedInDataSource?.checkIn(CheckedInEntity(it))
                                exhibitionId = msg.id
                                _state.value = CertifiedTicket
                            } else {
                                _state.value = WrongTicket
                            }
                        }
                    }
                    else -> {
                        Log.d("tag", "failed")
                    }
                }
            }
        }


    }

    fun setWrongTicket() {
        _state.value = WrongTicket
    }
}

sealed class QuickEnterState {
    object InitialState : QuickEnterState()
    object CertifiedTicket : QuickEnterState()
    object WrongTicket : QuickEnterState()
}