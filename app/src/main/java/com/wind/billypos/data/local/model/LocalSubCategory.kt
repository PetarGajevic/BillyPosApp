package com.wind.billypos.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.wind.billypos.base.Error
import org.jetbrains.annotations.NotNull
import org.threeten.bp.LocalDateTime

@Entity(tableName = "subcategory")
data class LocalSubCategory(
    @PrimaryKey
    @NotNull
    @ColumnInfo(name = "id") val id: Long? = null,
    @ColumnInfo(name = "idCompany") val idCompany: Long? = 0,
    @ColumnInfo(name = "id_category") val id_category: Long? = 0,
    @ColumnInfo(name = "subcategoryName") val subcategoryName: String? = null,
    @ColumnInfo(name = "subcategoryOrder") val subcategoryOrder: Int? = null,
    @ColumnInfo(name = "status") val status: Boolean? = true,
    @ColumnInfo(name = "isActive") val isActive: Boolean? = true,
    @ColumnInfo(name = "createdAt") val createdAt: LocalDateTime? = null,
): Error()