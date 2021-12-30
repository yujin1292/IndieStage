package com.jin.android.indiestage.ui.ticketbox

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jin.android.indiestage.data.EnterCodeOnSuccess
import com.jin.android.indiestage.data.ExhibitionRepo
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import kotlin.experimental.and

@ExperimentalCoroutinesApi
class TicketBoxViewModel(
    private val exhibitionRepo: ExhibitionRepo,
    private val exhibitionId: String,
) : ViewModel() {
    val startNavigateToStageWithAuth: MutableStateFlow<Boolean> = MutableStateFlow(false)

    private var enterCode: String = ""

    init {
        viewModelScope.launch {
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
    }

    fun checkEnterCode(inputCode: String) {
        val sha = try {
            val sh: MessageDigest = MessageDigest.getInstance("SHA-256")
            sh.update(inputCode.encodeToByteArray())
            val byteData: ByteArray = sh.digest()
            val sb = StringBuffer()
            for (i in byteData.indices) sb.append(
                ((byteData[i] and 0xff.toByte()) + 0x100).toString(16).substring(1)
            )
            sb.toString()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
            null
        }
        startNavigateToStageWithAuth.value = (sha == enterCode)
    }

}