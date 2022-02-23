package com.wind.billypos.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.wind.billypos.base.Error
import org.jetbrains.annotations.NotNull
import org.threeten.bp.LocalDateTime
import java.util.*

@Entity(tableName = "item", indices = [Index(value = ["uuid"], unique = true)])
data class LocalItem(
    @PrimaryKey(autoGenerate = true)
    @NotNull
    @ColumnInfo(name = "id") val id: Long = 0,
    @ColumnInfo(name = "uuid") val uuid: String = UUID.randomUUID().toString(),
    @ColumnInfo(name = "idCompany") val idCompany: Long? = 0,
    @ColumnInfo(name = "idAddress") val idAddress: Long? = 0,
    @ColumnInfo(name = "idCategory") val idCategory: Long? = 0,
    @ColumnInfo(name = "idSubcategory") val idSubcategory: Long? = 0,
    @ColumnInfo(name = "itemName") val itemName: String? = null,
    @ColumnInfo(name = "itemDesc") val itemDesc: String? = null,
    @ColumnInfo(name = "barcode") val barcode: String? = null,
    @ColumnInfo(name = "itemImage") val itemImage: String? = null,
    @ColumnInfo(name = "itemUnit") val itemUnit: String? = null,
    @ColumnInfo(name = "itemType") val itemType: String? = null,
    @ColumnInfo(name = "itemPrice") val itemPrice: Double = 0.0,
    @ColumnInfo(name = "itemPriceWithDiscount") val itemPriceWithDiscount: Double = 0.0,
    @ColumnInfo(name = "unitPrice") val unitPrice: Double = 0.0,
    @ColumnInfo(name = "discount") val discount: Double = 0.0,
    @ColumnInfo(name = "vat") val vat: Double? = 0.0,
    @ColumnInfo(name = "status") val status: Int? = 0,
    @ColumnInfo(name = "isActive") val isActive: Boolean? = true,
    @ColumnInfo(name = "createdAt") val createdAt: LocalDateTime? = LocalDateTime.now(),
    @ColumnInfo(name = "categoryColor") val categoryColor: String? = "#3B4453",
    @ColumnInfo(name = "lastServerSync") val lastServerSync: LocalDateTime? = null,
    @ColumnInfo(name = "isSync") val isSync: Boolean? = false
): Error()