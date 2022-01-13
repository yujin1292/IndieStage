package com.jin.android.indiestage.data.room

import androidx.lifecycle.LiveData

class MyPageRepository(private val myPageDao: MyPageDao) : CheckedInDataSource, BookMarkDataSource {

    override suspend fun getEntity(id: String): Array<ExhibitionEntity> {
        return myPageDao.getEntity(id)
    }

    override fun getAllCheckedInData(): LiveData<List<ExhibitionEntity>> {
        return myPageDao.getAllCheckedInData()
    }

    override fun getAllBookMarked(): LiveData<List<ExhibitionEntity>> {
        return myPageDao.getAllBookMarkedData()
    }

    override suspend fun setBookmark(exhibitionEntity: ExhibitionEntity) {

        val data = myPageDao.getEntity(exhibitionEntity.id)
        data.let { array ->
            if(array.isEmpty()) {
                exhibitionEntity.isBookMarked = true
                myPageDao.insert(exhibitionEntity)
            } else {
                exhibitionEntity.isBookMarked = exhibitionEntity.isBookMarked.not()
                myPageDao.update(exhibitionEntity)

            }
        }
    }

    override suspend fun checkIn(exhibitionEntity: ExhibitionEntity) {
        val data = myPageDao.getEntity(exhibitionEntity.id)
        data.let { array ->
            if(array.isEmpty()) {
                exhibitionEntity.isCheckedIn = true
                myPageDao.insert(exhibitionEntity)
            } else {
                array[0].let {
                    it.isCheckedIn = true
                    myPageDao.update(it)
                }
            }
        }
    }
}