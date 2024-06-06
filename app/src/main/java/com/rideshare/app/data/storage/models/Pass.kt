package com.rideshare.app.data.storage.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.rideshare.app.utils.Const

@Entity(tableName = Const.PASS, indices = [Index(value = [Const.PASS], unique = true)])
class Pass(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = Const.ID) val id: Int = 0,
    @ColumnInfo(name = Const.DRIVER) val driverId: Int = 0,
    @ColumnInfo(name = Const.PASS) var passId: Int = 0
)