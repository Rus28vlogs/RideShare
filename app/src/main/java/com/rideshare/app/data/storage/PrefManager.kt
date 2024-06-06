package com.rideshare.app.data.storage

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences

class PrefManager {

    companion object {
        var sharedPref: SharedPreferences? = null
    }

    fun connect(activity: Activity) {
        sharedPref = activity.getPreferences(Context.MODE_PRIVATE)
    }

    fun setString(id: String, data: String) {
        if (sharedPref == null) return
        with (sharedPref!!.edit()) {
            putString(id, data)
            apply()
        }
    }

    fun getString(id: String): String? {
        return sharedPref?.getString(id, null)
    }

    fun setInt(id: String, data: Int) {
        if (sharedPref == null) return
        with (sharedPref!!.edit()) {
            putInt(id, data)
            apply()
        }
    }

    fun getInt(id: String): Int? {
        return sharedPref?.getInt(id, -1)
    }

    fun setBoolean(id: String, data: Boolean) {
        if (sharedPref == null) return
        with (sharedPref!!.edit()) {
            putBoolean(id, data)
            apply()
        }
    }

    fun getBoolean(id: String): Boolean {
        if (sharedPref == null) return false
        return sharedPref!!.getBoolean(id, false)
    }

    fun clear() {
        if (sharedPref == null) return
        with (sharedPref!!.edit()) {
            clear()
            apply()
        }
    }
}