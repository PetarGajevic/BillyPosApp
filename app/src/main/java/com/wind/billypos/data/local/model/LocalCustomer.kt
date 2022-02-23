package com.wind.billypos.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.wind.billypos.base.Error
import org.jetbrains.annotations.NotNull
import org.threeten.bp.LocalDateTime
import java.util.*

@Entity(tableName = "customer", indices = [Index(value = ["uuid"], unique = true)])
data class LocalCustomer(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id: Long? = null,
    @ColumnInfo(name = "uuid") val uuid: String = UUID.randomUUID().toString(),
    @ColumnInfo(name = "name") val name: String? = null,
    @ColumnInfo(name = "email") val email: String? = null,
    @ColumnInfo(name = "address") val address: String? = null,
    @ColumnInfo(name = "phone") val phone: String? = null,
    @ColumnInfo(name = "city") val city: String? = null,
    @ColumnInfo(name = "country") val country: String? = null,
    @ColumnInfo(name = "idType") val idType: String? = null,
    @ColumnInfo(name = "idNum") val idNum: String? = null,
    @ColumnInfo(name = "idCompany") val idCompany: Long? = null,
    @ColumnInfo(name = "deviceId") val deviceId: Long? = null,
    @ColumnInfo(name = "userID") val userID: Long? = null,
    @ColumnInfo(name = "createdAt") val createdAt: LocalDateTime = LocalDateTime.now(),
    @ColumnInfo(name = "lastServerSync") val lastServerSync: LocalDateTime? = null,
    @ColumnInfo(name = "isSync") val isSync: Boolean? = false
): Error()