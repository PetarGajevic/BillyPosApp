package com.wind.billypos.data.local.model

import androidx.room.*
import androidx.room.ForeignKey.CASCADE
import com.wind.billypos.base.Error
import org.threeten.bp.LocalDateTime
import java.util.*

@Entity(tableName = "tbl_transactions_body",
    foreignKeys = [ForeignKey(entity = LocalTransactionHead::class,
    parentColumns = arrayOf("uuid"),
    childColumns = arrayOf("idTransactionHead"),
    onDelete = CASCADE)]
    ,indices = [Index(value = ["id"], unique = true)])
data class LocalTransactionBody (
    @PrimaryKey
    @ColumnInfo(name = "id") val id: Long? = null,
    @ColumnInfo(name = "uuid") val uuid: String? = UUID.randomUUID().toString(),
    @ColumnInfo(name = "idTransactionHead") val idTransactionHead: String? = null,
    @ColumnInfo(name = "idCompany") val idCompany: Long? = null,
    @ColumnInfo(name = "idAddress") val idAddress: Long? = null,
    @ColumnInfo(name = "idDevice") val idDevice: Long? = null,
    @ColumnInfo(name = "idUser") val idUser: Long? = null,
    @ColumnInfo(name = "idItem") val idItem: String? = null,
    @ColumnInfo(name = "itemName") val itemName: String? = null,
    @ColumnInfo(name = "itemUnit") val itemUnit: String? = null,
    @ColumnInfo(name = "idCategory") val idCategory: Long? = null,
    @ColumnInfo(name = "unitPrice") val unitPrice: Double? = 0.0,
    @ColumnInfo(name = "unitPriceWithVat") val unitPriceWithVat: Double? = 0.0,
    @ColumnInfo(name = "quantity") val quantity: Double? = 0.0,
    @ColumnInfo(name = "rabat") val rabat: Double? = 0.0,
    @ColumnInfo(name = "vatRate") val vatRate: Double? = 0.0,
    @ColumnInfo(name = "vatAmount") val vatAmount: Double? = 0.0,
    @ColumnInfo(name = "itemPrice") val itemPrice: Double? = 0.0,
    @ColumnInfo(name = "itemPriceWithDiscount") val itemPriceWithDiscount: Double = 0.0,
    @ColumnInfo(name = "total") val total: Double? = 0.0,
    @ColumnInfo(name = "totalWithVat") val totalWithVat: Double? = 0.0,
    @ColumnInfo(name = "costPrice") val costPrice: Double? = 0.0,
    @ColumnInfo(name = "lastServerSync") val lastServerSync: LocalDateTime? = null,
    @Embedded(prefix = "item_") val item: LocalItem? = null
): Error()