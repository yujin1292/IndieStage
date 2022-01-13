package com.jin.android.indiestage

import android.app.Application
import com.jin.android.indiestage.data.room.BookMarkDataSource
import com.jin.android.indiestage.data.room.CheckedInDataSource
import com.jin.android.indiestage.data.room.MyPageDatabase
import com.jin.android.indiestage.data.room.MyPageRepository

class IndieStage : Application() {

    private lateinit var _myPageRepository: MyPageRepository
    val bookMarkDataSource: BookMarkDataSource
        get() = _myPageRepository
    val checkedInDataSource: CheckedInDataSource
        get() = _myPageRepository

    override fun onCreate() {
        super.onCreate()

        val checkedInDatabase = MyPageDatabase.getDataBase(this)
        _myPageRepository = MyPageRepository(checkedInDatabase.checkedInDao())

    }
}