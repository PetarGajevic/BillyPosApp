package com.wind.billypos.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.wind.billypos.base.Error
import org.threeten.bp.LocalDateTime
import java.util.*


@Entity(tableName = "operators", indices = [Index(value = ["operatorCode", "pin"], unique = true)])
data class LocalOperator(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id: Long? = null,
    @ColumnInfo(name = "businUnit") val businUnit: Long? = null,
    @ColumnInfo(name = "cashRegister") val cashRegister: Long? = null,
    @ColumnInfo(name = "firstName") val firstName: String? = null,
    @ColumnInfo(name = "lastName") val lastName: String? = null,
    @ColumnInfo(name = "operatorCode") val operatorCode: String? = null,
    @ColumnInfo(name = "pin") val pin: String? = null,
    @ColumnInfo(name = "nationalIdNo") val nationalIdNo: String? = null,
    @ColumnInfo(name = "userRole") val userRole: String? = "OPERATOR_MOBILE",
    @ColumnInfo(name = "permissions") val permissions: List<String>? = listOf(),
    @ColumnInfo(name = "status") val status: Boolean? = true,
    @ColumnInfo(name = "isActive") val isActive: Boolean? = true,
    @ColumnInfo(name = "createdAt") val createdAt: LocalDateTime? = LocalDateTime.now(),
    @ColumnInfo(name = "lastServerSync") val lastServerSync: LocalDateTime? = null,
    @ColumnInfo(name = "isSync") val isSync: Boolean? = false
): Error()

