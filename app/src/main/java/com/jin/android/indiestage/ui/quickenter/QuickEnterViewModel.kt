package com.jin.android.indiestage.ui.quickenter

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jin.android.indiestage.data.*
import com.jin.android.indiestage.data.firestore.Exhibition
import com.jin.android.indiestage.data.firestore.FireStoreRepository
import com.jin.android.indiestage.data.firestore.OnSuccess
import com.jin.android.indiestage.data.room.CheckedInDataSource
import com.jin.android.indiestage.data.room.ExhibitionEntity
import com.jin.android.indiestage.ui.quickenter.QuickEnterState.*
import com.jin.android.indiestage.util.EnterCode
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class QuickEnterViewModel(
    private val checkedInDataSource: CheckedInDataSource?,
    private val fireStoreRepository: FireStoreRepository
) : ViewModel() {

    private val _state = MutableStateFlow<QuickEnterState>(InitialState)
    val state: StateFlow<QuickEnterState>
        get() = _state
    var exhibitionId: String = ""

    private var _exhibitionEntity = MutableLiveData(ExhibitionEntity())
    val exhibitionEntity: LiveData<ExhibitionEntity>
        get() = _exhibitionEntity

    fun verifyEnterCode(msg: QRMessage) {
        viewModelScope.launch {


            fireStoreRepository.getExhibitionsById(msg.id).collect { response ->
                when (response) {
                    is OnSuccess -> {
                        val exhibition =
                            response.querySnapshot?.toObjects(Exhibition::class.java)?.get(0)

                        exhibition?.let {
                            val isMatched = EnterCode.verifyEnterCode(msg.enterCode, it.enterCode)
                            if (isMatched) {
                                checkedInDataSource?.checkIn(ExhibitionEntity(it))
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