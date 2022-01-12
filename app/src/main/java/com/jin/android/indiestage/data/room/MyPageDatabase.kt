package com.jin.android.indiestage.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ExhibitionEntity::class], version = 1, exportSchema = false)
abstract class MyPageDatabase : RoomDatabase() {

    abstract fun checkedInDao(): MyPageDao

    companion object {
        @Volatile
        private var myPageDatabase: MyPageDatabase? = null

        fun getDataBase(context: Context): MyPageDatabase {
            return (myPageDatabase ?: synchronized(this) {
                if (myPageDatabase == null) {
                    val instance = Room.databaseBuilder(
                        context.applicationContext,
                        MyPageDatabase::class.java,
                        "checked_in_database"
                    ).build()
                    myPageDatabase = instance
                }
                myPageDatabase
            }) as MyPageDatabase
        }
    }
}