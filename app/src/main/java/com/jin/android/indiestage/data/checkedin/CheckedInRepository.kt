package com.jin.android.indiestage.data.checkedin

import androidx.lifecycle.LiveData

class CheckedInRepository(private val checkedInDao:CheckedInDao ) :CheckedInDataSource{
    override  fun getAllData(): LiveData<List<CheckedInEntity>> {
        return checkedInDao.getAll()
    }

    override suspend fun checkIn(entity: CheckedInEntity) {
        checkedInDao.insert(entity)
    }
}