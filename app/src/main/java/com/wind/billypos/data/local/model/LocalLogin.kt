package com.wind.billypos.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "login")
data class LocalLogin(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id: Long? = null,
    @ColumnInfo(name = "authorization") val authorization: String? = null,
    @ColumnInfo(name = "operators") val operators: List<LocalOperator>? = null,
    @ColumnInfo(name = "businUnits") val businUnits: List<LocalBusinUnit>? = null
)