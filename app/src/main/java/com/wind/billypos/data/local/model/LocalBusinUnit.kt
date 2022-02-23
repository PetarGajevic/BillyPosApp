package com.wind.billypos.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "businUnit")
data class LocalBusinUnit (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id: Long? = null,
    @ColumnInfo(name = "name") val name: String? = null,
    @ColumnInfo(name = "businUnitCode") val businUnitCode: String? = null,
    @ColumnInfo(name = "address") val address: String? = null,
    @ColumnInfo(name = "city") val city: String? = null,
    @ColumnInfo(name = "country") val country: String? = null,
    @ColumnInfo(name = "status") val status: String? = null,
    @ColumnInfo(name = "desc") val desc: String? = null,
        )