package com.jin.android.indiestage.data.room

import androidx.lifecycle.LiveData

interface BookMarkDataSource {
    fun getEntity(id: String): LiveData<ExhibitionEntity>
    fun getAllBookMarked(): LiveData<List<ExhibitionEntity>>
    suspend fun setBookmark(exhibitionEntity: ExhibitionEntity)
}