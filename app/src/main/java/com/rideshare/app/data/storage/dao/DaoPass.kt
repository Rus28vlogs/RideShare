package com.rideshare.app.data.storage.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rideshare.app.data.storage.models.Pass
import com.rideshare.app.utils.Const
import kotlinx.coroutines.flow.Flow

@Dao
interface DaoPass {
    @Query("SELECT * FROM ${Const.PASS}")
    fun getAll(): Flow<List<Pass>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: Pass)

    @Delete()
    suspend fun delete(item: Pass)
}