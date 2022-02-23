package com.wind.billypos.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull
import org.threeten.bp.LocalDateTime
import java.util.*

@Entity(tableName = "entity_point", indices = [Index(value = ["uuid"], unique = true)])
data class LocalEntityPoint(
    @PrimaryKey(autoGenerate = true)
    @NotNull
    @ColumnInfo(name = "id") val id: Long? = null,
    @ColumnInfo(name = "uuid") val uuid: String? = UUID.randomUUID().toString(),
    @ColumnInfo(name = "entityName") val entityName: String? = "",
    @ColumnInfo(name = "entityColor") val entityColor: String? = "",
    @ColumnInfo(name = "idBusinessUnit") val idBusinessUnit: Int? = 0,
    @ColumnInfo(name = "createdAt") val createdAt: LocalDateTime? = LocalDateTime.now(),
    @ColumnInfo(name = "updatedAt") val updatedAt: LocalDateTime? = LocalDateTime.now(),
//    @ColumnInfo(name = "isOpen") val isOpen: Boolean? = true
)