package com.jin.android.indiestage.data.room

import androidx.lifecycle.LiveData

interface CheckedInDataSource {
    fun getAllCheckedInData(): LiveData<List<ExhibitionEntity>>
    suspend fun checkIn(exhibitionEntity: ExhibitionEntity)
    suspend fun isCheckedIn(exhibitionId: String): Boolean
}