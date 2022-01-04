package com.jin.android.indiestage.ui.ticketbox

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jin.android.indiestage.data.EnterCodeOnSuccess
import com.jin.android.indiestage.data.Exhibition
import com.jin.android.indiestage.data.ExhibitionRepo
import com.jin.android.indiestage.data.OnSuccess
import com.jin.android.indiestage.data.checkedin.CheckedInDataSource
import com.jin.android.indiestage.data.checkedin.CheckedInEntity
import com.jin.android.indiestage.util.EnterCode
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class TicketBoxViewModel(
    private val checkedInDataSource: CheckedInDataSource?,
    private val exhibitionRepo: ExhibitionRepo,
    private val exhibitionId: String,
) : ViewModel() {
    val startNavigateToStageWithAuth: MutableStateFlow<Boolean> = MutableStateFlow(false)

    private var enterCode: String = ""
    private var exhibition: Exhibition = Exhibition()

    init {
        viewModelScope.launch {
            launch {
                exhibitionRepo.getExhibitionEnterCode(exhibitionId).collect {
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
                exhibitionRepo.getExhibitionsById(exhibitionId).collect {
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

    fun verifyEnterCode(inputCode: String) {
        viewModelScope.launch {
            val startNavigate = EnterCode.verifyEnterCode(inputCode, enterCode)

            startNavigateToStageWithAuth.value = startNavigate
            if (startNavigate) {
                checkedInDataSource?.checkIn(CheckedInEntity(exhibition))
            }
        }
    }

}