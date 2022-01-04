package com.jin.android.indiestage.data.checkedin

import androidx.lifecycle.LiveData

interface CheckedInDataSource {
    fun getAllData(): LiveData<List<CheckedInEntity>>
    suspend fun checkIn(exhibition: CheckedInEntity)
}