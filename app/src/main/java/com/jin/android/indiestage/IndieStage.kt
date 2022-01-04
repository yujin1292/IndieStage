package com.jin.android.indiestage

import android.app.Application
import com.jin.android.indiestage.data.checkedin.CheckedInDataSource
import com.jin.android.indiestage.data.checkedin.CheckedInDatabase
import com.jin.android.indiestage.data.checkedin.CheckedInRepository

class IndieStage :Application(){

    private lateinit var _checkedInRepository: CheckedInRepository
    val checkedInDataSource: CheckedInDataSource
        get() = _checkedInRepository

    override fun onCreate() {
        super.onCreate()

        val checkedInDatabase = CheckedInDatabase.getDataBase(this)
        _checkedInRepository = CheckedInRepository(checkedInDatabase.checkedInDao())

    }
}