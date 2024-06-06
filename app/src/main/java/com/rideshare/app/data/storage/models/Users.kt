package com.rideshare.app.data.storage.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.rideshare.app.utils.Const

@Entity(tableName = Const.USERS)
class Users(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = Const.ID) val id: Int = 0,
    @ColumnInfo(name = Const.LOGIN) val login: String = "",
    @ColumnInfo(name = Const.PASSWORD) val password: String = "",
    @ColumnInfo(name = Const.TYPE) val type: String = "",
    @ColumnInfo(name = Const.FIO) val fio: String = "",
    @ColumnInfo(name = Const.NUMBER) val number: String = "",
    @ColumnInfo(name = Const.DATE) val date: String = "",
    @ColumnInfo(name = Const.CAR_MARK) val carMark: String = "",
    @ColumnInfo(name = Const.CAR_NUMBER) val carNumber: String = "",
    @ColumnInfo(name = Const.START_ADDRESS) val startAddress: String = "",
    @ColumnInfo(name = Const.FINISH_ADDRESS) val finishAddress: String = "",
    @ColumnInfo(name = Const.START_LATITUDE) val startLatitude: Double = 0.0,
    @ColumnInfo(name = Const.START_LONGITUDE) val startLongitude: Double = 0.0,
    @ColumnInfo(name = Const.FINISH_LATITUDE) val finishLatitude: Double = 0.0,
    @ColumnInfo(name = Const.FINISH_LONGITUDE) val finishLongitude: Double = 0.0,
    @ColumnInfo(name = Const.IS_SIT) var isSit: Boolean = false,
    @ColumnInfo(name = Const.IS_START) var isStart: Boolean = false,
)