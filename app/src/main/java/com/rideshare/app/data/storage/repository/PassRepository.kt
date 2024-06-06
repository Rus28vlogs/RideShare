package com.rideshare.app.data.storage.repository

import androidx.annotation.WorkerThread
import com.rideshare.app.data.storage.dao.DaoPass
import com.rideshare.app.data.storage.models.Pass
import com.rideshare.app.data.storage.models.Users
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class PassRepository(private val dao: DaoPass) {

    val all: Flow<List<Pass>> = dao.getAll()

    @WorkerThread
    fun insert(item: Pass) {
        CoroutineScope(Dispatchers.IO).launch {
            dao.insert(item)
        }
    }

    @WorkerThread
    fun delete(item: Pass) {
        CoroutineScope(Dispatchers.IO).launch {
            dao.delete(item)
        }
    }
}