package com.rideshare.app.data.storage

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.rideshare.app.utils.Const
import com.rideshare.app.data.storage.models.Users
import com.rideshare.app.data.storage.dao.DaoUsers
import com.rideshare.app.data.storage.dao.DaoPass
import com.rideshare.app.data.storage.models.Pass

@Database(entities = [Users::class, Pass::class], version = 8, exportSchema = true)
abstract class MainDatabase : RoomDatabase() {

    abstract fun daoUsers(): DaoUsers
    abstract fun daoPass(): DaoPass

    companion object {

        @Volatile
        private var INSTANCE: MainDatabase? = null

        fun getDatabase(context: Context): MainDatabase {

            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MainDatabase::class.java,
                    Const.DB_NAME
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}