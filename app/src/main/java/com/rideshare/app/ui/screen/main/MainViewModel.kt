package com.rideshare.app.ui.screen.main

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import com.rideshare.app.R
import com.rideshare.app.data.storage.MainDatabase
import com.rideshare.app.data.storage.PrefManager
import com.rideshare.app.data.storage.models.Pass
import com.rideshare.app.data.storage.models.Users
import com.rideshare.app.data.storage.repository.PassRepository
import com.rideshare.app.data.storage.repository.UserRepository
import com.rideshare.app.utils.Const
import com.rideshare.app.utils.ToastHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@HiltViewModel
class MainViewModel: ViewModel() {

    companion object {
        val user = MutableStateFlow(Users())
        val userList = MutableStateFlow(listOf<Users>())
        val passList = MutableStateFlow(listOf<Pass>())
    }

    fun isAuth(context: Context, callback: (Boolean) -> Unit) {
        val login = PrefManager().getString(Const.LOGIN)
        if (!login.isNullOrEmpty()) {
            val rep = UserRepository(MainDatabase.getDatabase(context).daoUsers())
            CoroutineScope(Dispatchers.IO).launch {
                rep.getInfo(login)
                    .flowOn(Dispatchers.IO)
                    .catch { e -> Log.e(Const.LOG_TAG, e.toString()) }
                    .collect {
                        withContext(Dispatchers.Main) {
                            if (it != null) {
                                user.value = it
                            }
                            callback(PrefManager().getBoolean(Const.IS_AUTH) && it != null)
                            this.coroutineContext.job.cancel()
                        }
                    }
            }
        } else {
            callback(false)
        }
    }

    fun login(context: Context, login: String, password: String, callback: (Boolean) -> Unit) {
        if (login.isEmpty() || password.isEmpty()){
            ToastHelper().show(context, context.getString(R.string.toastEmptyToken))
            callback(false)
            return
        }
        if (password.length < 6) {
            ToastHelper().show(context, context.getString(R.string.toastErrorPass))
            callback(false)
            return
        }

        val rep = UserRepository(MainDatabase.getDatabase(context).daoUsers())

        CoroutineScope(Dispatchers.IO).launch {
            rep.get(login, password)
                .flowOn(Dispatchers.IO)
                .catch { e -> Log.e(Const.LOG_TAG, e.toString()) }
                .collect {
                    withContext(Dispatchers.Main) {
                        if (it != null) {
                            user.value = it
                            PrefManager().setBoolean(Const.IS_AUTH, true)
                            PrefManager().setString(Const.LOGIN, it.login)
                            callback(true)
                        } else {
                            ToastHelper().show(context, context.getString(R.string.toastLoginError))
                            callback(false)
                        }
                        this.coroutineContext.job.cancel()
                    }
                }
        }
    }

    fun reg(context: Context, users: Users, callback: (Boolean) -> Unit) {
        if (users.login.isEmpty() || users.password.isEmpty()){
            ToastHelper().show(context, context.getString(R.string.toastEmptyToken))
            callback(false)
            return
        }
        if (users.password.length < 6) {
            ToastHelper().show(context, context.getString(R.string.toastErrorPass))
            callback(false)
            return
        }

        val rep = UserRepository(MainDatabase.getDatabase(context).daoUsers())

        rep.insert(users)
        user.value = users
        PrefManager().setBoolean(Const.IS_AUTH, true)
        PrefManager().setString(Const.LOGIN, users.login)
        callback(true)
    }

    fun logout() {
        PrefManager().clear()
        PrefManager().setBoolean(Const.IS_AUTH, false)
    }

    fun getAllUsers(context: Context) {
        try {
            val repository = UserRepository(MainDatabase.getDatabase(context).daoUsers())
            CoroutineScope(Dispatchers.IO).launch {
                repository.getAll
                    .flowOn(Dispatchers.IO)
                    .catch { e -> Log.e(Const.LOG_TAG, e.toString()) }
                    .collect {
                        userList.value = it
                        this.coroutineContext.job.cancel()
                    }
            }
        } catch (e: Exception) {
            Log.e(Const.LOG_TAG, e.toString())
        }
    }

    fun insertPass(context: Context, pass: Pass) {
        val rep = PassRepository(MainDatabase.getDatabase(context).daoPass())
        rep.insert(pass)

        user.value.isSit = true
        val uRep = UserRepository(MainDatabase.getDatabase(context).daoUsers())
        uRep.insert(user.value)
    }

    fun getAllPass(context: Context) {
        try {
            val repository = PassRepository(MainDatabase.getDatabase(context).daoPass())
            CoroutineScope(Dispatchers.IO).launch {
                repository.all
                    .flowOn(Dispatchers.IO)
                    .catch { e -> Log.e(Const.LOG_TAG, e.toString()) }
                    .collect {
                        passList.value = it
                        this.coroutineContext.job.cancel()
                    }
            }
        } catch (e: Exception) {
            Log.e(Const.LOG_TAG, e.toString())
        }
    }

    fun start(context: Context) {
        user.value.isStart = true
        val uRep = UserRepository(MainDatabase.getDatabase(context).daoUsers())
        uRep.insert(user.value)
    }

    fun help(context: Context) {
        callPhone(context, "112")
    }

    fun cancelClick(context: Context) {
        user.value.isSit = false
        val uRep = UserRepository(MainDatabase.getDatabase(context).daoUsers())
        uRep.insert(user.value)

        val pass = passList.value.firstOrNull{ it.passId == user.value.id }
        if (pass != null) {
            val rep = PassRepository(MainDatabase.getDatabase(context).daoPass())
            rep.delete(pass)
        }
    }

    private fun callPhone(context: Context, phone: String) {
        if (ContextCompat.checkSelfPermission(context,
                android.Manifest.permission.CALL_PHONE)
            != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(context as Activity,
                    android.Manifest.permission.CALL_PHONE)) {
            } else {
                ActivityCompat.requestPermissions(context,
                    arrayOf(android.Manifest.permission.CALL_PHONE),
                    42)
            }
        } else {
            val intent = Intent(Intent.ACTION_CALL)
            intent.data = Uri.parse("tel:${phone}")
            context.startActivity(intent)
        }
    }
}