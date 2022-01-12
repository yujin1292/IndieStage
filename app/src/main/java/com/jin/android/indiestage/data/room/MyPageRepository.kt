package com.jin.android.indiestage.data.room

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class MyPageRepository(private val myPageDao: MyPageDao) : CheckedInDataSource, BookMarkDataSource {

    override fun getEntity(id: String): LiveData<ExhibitionEntity> {
        myPageDao.getEntity(id).let {
            return if (it.value == null) it
            else MutableLiveData(ExhibitionEntity())
        }
    }

    override fun getAllCheckedInData(): LiveData<List<ExhibitionEntity>> {
        return myPageDao.getAllCheckedInData()
    }

    override fun getAllBookMarked(): LiveData<List<ExhibitionEntity>> {
        return myPageDao.getAllBookMarkedData()
    }

    override suspend fun setBookmark(exhibitionEntity: ExhibitionEntity) {
        if (myPageDao.getCount(exhibitionEntity.id) > 0) {
            exhibitionEntity.apply { isBookMarked = !isBookMarked }
            myPageDao.update(exhibitionEntity)
        } else {
            exhibitionEntity.isBookMarked = true
            myPageDao.insert(exhibitionEntity)
        }
    }

    override suspend fun checkIn(exhibitionEntity: ExhibitionEntity) {
        if (myPageDao.getCount(exhibitionEntity.id) > 0) {
            getEntity(exhibitionEntity.id).value?.apply{
                isCheckedIn = true
            }?.let { myPageDao.insert(it) }
        } else {

            myPageDao.insert(exhibitionEntity)
        }


    }
}