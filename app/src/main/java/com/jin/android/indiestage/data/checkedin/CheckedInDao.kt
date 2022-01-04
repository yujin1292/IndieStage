package com.jin.android.indiestage.data.checkedin

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CheckedInDao {
    @Query("SELECT * FROM checked_in")
    fun getAll(): LiveData<List<CheckedInEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: CheckedInEntity)
}