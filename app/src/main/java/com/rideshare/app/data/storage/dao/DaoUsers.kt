package com.rideshare.app.data.storage.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rideshare.app.data.storage.models.Users
import com.rideshare.app.utils.Const
import kotlinx.coroutines.flow.Flow

@Dao
interface DaoUsers {
    @Query("SELECT * FROM ${Const.USERS} WHERE login = :login AND password = :password")
    fun get(login: String, password: String): Flow<Users?>

    @Query("SELECT * FROM ${Const.USERS} WHERE login = :login")
    fun getInfo(login: String): Flow<Users?>

    @Query("SELECT * FROM ${Const.USERS}")
    fun getAll(): Flow<List<Users>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(recipe: Users)
}