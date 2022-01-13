package com.jin.android.indiestage.data.room

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface MyPageDao {
    @Query("SELECT * FROM exhibition")
    fun getAll(): LiveData<List<ExhibitionEntity>>

    @Query("SELECT * FROM exhibition WHERE isCheckedIn = 1")
    fun getAllCheckedInData(): LiveData<List<ExhibitionEntity>>

    @Query("SELECT * FROM exhibition WHERE isBookMarked = 1")
    fun getAllBookMarkedData(): LiveData<List<ExhibitionEntity>>

    @Query("SELECT * FROM exhibition WHERE id =:id")
    suspend fun getEntity(id:String):Array<ExhibitionEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: ExhibitionEntity)

    @Delete
    suspend fun delete(entity: ExhibitionEntity)

    @Query("SELECT COUNT(*) FROM exhibition WHERE id=:id")
    suspend fun getCount(id: String): Int

    @Update
    suspend fun update(entity: ExhibitionEntity)

}