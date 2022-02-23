package com.wind.billypos.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.wind.billypos.base.Error
import org.jetbrains.annotations.NotNull
import org.threeten.bp.LocalDateTime

@Entity(tableName = "category")
data class LocalCategory(
    @PrimaryKey(autoGenerate = true)
    @NotNull
    @ColumnInfo(name = "id") val id: Long? = null,
    @ColumnInfo(name = "idCompany") val idCompany: Long = 0,
    @ColumnInfo(name = "categoryName") val categoryName: String? = null,
    @ColumnInfo(name = "categoryColor") val categoryColor: String? = null,
    @ColumnInfo(name = "categoryOrder") val categoryOrder: Int? = null,
    @ColumnInfo(name = "subcategories") val subcategories: List<LocalSubCategory> = listOf(),
    @ColumnInfo(name = "status") val status: Boolean? = true,
    @ColumnInfo(name = "isActive") val isActive: Boolean? = true,
    @ColumnInfo(name = "createdAt") val createdAt: LocalDateTime? = LocalDateTime.now()
): Error()