package com.rideshare.app.data.storage.repository

import androidx.annotation.WorkerThread
import com.rideshare.app.data.storage.dao.DaoUsers
import com.rideshare.app.data.storage.models.Users
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class UserRepository(private val daoUsers: DaoUsers) {

    val getAll: Flow<List<Users>> = daoUsers.getAll()

    @WorkerThread
    fun get(login: String, password: String): Flow<Users?> {
        return daoUsers.get(login, password)
    }

    @WorkerThread
    fun getInfo(login: String): Flow<Users?> {
        return daoUsers.getInfo(login)
    }

    @WorkerThread
    fun insert(item: Users) {
        CoroutineScope(Dispatchers.IO).launch {
            daoUsers.insert(item)
        }
    }
}