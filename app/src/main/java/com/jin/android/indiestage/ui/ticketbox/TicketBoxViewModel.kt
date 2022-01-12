package com.jin.android.indiestage.ui.ticketbox

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jin.android.indiestage.data.*
import com.jin.android.indiestage.data.firestore.EnterCodeOnSuccess
import com.jin.android.indiestage.data.firestore.Exhibition
import com.jin.android.indiestage.data.firestore.ExhibitionRepository
import com.jin.android.indiestage.data.firestore.OnSuccess
import com.jin.android.indiestage.data.room.CheckedInDataSource
import com.jin.android.indiestage.data.room.ExhibitionEntity
import com.jin.android.indiestage.ui.ticketbox.TicketBoxState.*
import com.jin.android.indiestage.util.EnterCode
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class TicketBoxViewModel(
    private val checkedInDataSource: CheckedInDataSource?,
    private val exhibitionRepository: ExhibitionRepository,
    private val exhibitionId: String,
) : ViewModel() {
    val state: MutableStateFlow<TicketBoxState> = MutableStateFlow(InitialState)

    private var enterCode: String = ""
    private var exhibition: Exhibition = Exhibition()

    init {
        viewModelScope.launch {
            launch {
                exhibitionRepository.getExhibitionEnterCode(exhibitionId).collect {
                    when (it) {
                        is EnterCodeOnSuccess -> {
                            enterCode = it.enterCode
                            Log.d("tag", enterCode)
                        }
                        else -> {
                            Log.d("tag", "failed")
                        }
                    }
                }
            }


            launch {
                exhibitionRepository.getExhibitionsById(exhibitionId).collect {
                    when (it) {
                        is OnSuccess -> {
                            exhibition =
                                it.querySnapshot?.toObjects(Exhibition::class.java)?.get(0)!!
                            Log.d("tag", enterCode)
                        }
                        else -> {
                            Log.d("tag", "failed")
                        }
                    }
                }
            }

        }
    }

    fun verifyEnterCode(msg:QRMessage) {
        if(msg.id != exhibitionId){
            state.value = WrongId
            return
        }
        viewModelScope.launch {
            val isMatched = EnterCode.verifyEnterCode(msg.enterCode, enterCode)
            if (isMatched) {
                checkedInDataSource?.checkIn(ExhibitionEntity(exhibition))
                state.value = StartNavigation
            }else{
                state.value = WrongEnterCode
            }
        }
    }

}

sealed class TicketBoxState{
    object InitialState:TicketBoxState()
    object StartNavigation:TicketBoxState()
    object WrongEnterCode: TicketBoxState()
    object WrongId:TicketBoxState()
}