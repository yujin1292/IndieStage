package com.jin.android.indiestage.data.room

import androidx.lifecycle.LiveData

interface BookMarkDataSource {
    suspend fun getEntity(id: String): Array<ExhibitionEntity>
    fun getAllBookMarked(): LiveData<List<ExhibitionEntity>>
    suspend fun setBookmark(exhibitionEntity: ExhibitionEntity)
}