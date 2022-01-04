package com.jin.android.indiestage.data.checkedin

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [CheckedInEntity::class], version = 1, exportSchema = false)
abstract class CheckedInDatabase() : RoomDatabase() {

    abstract fun checkedInDao(): CheckedInDao

    companion object {
        @Volatile
        private var checkedInDatabase: CheckedInDatabase? = null

        fun getDataBase(context: Context): CheckedInDatabase {
            return (checkedInDatabase ?: synchronized(this) {
                if (checkedInDatabase == null) {
                    val instance = Room.databaseBuilder(
                        context.applicationContext,
                        CheckedInDatabase::class.java,
                        "checked_in_database"
                    ).build()
                    checkedInDatabase = instance
                }
                checkedInDatabase
            }) as CheckedInDatabase
        }
    }
}