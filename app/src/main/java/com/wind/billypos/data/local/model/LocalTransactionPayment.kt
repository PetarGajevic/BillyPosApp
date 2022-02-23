package com.wind.billypos.data.local.model

import androidx.room.*
import com.wind.billypos.base.Error
import com.wind.billypos.utils.enums.PaymentMethodTypeSType
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import java.util.*

@Entity(tableName = "transactions_payment", indices = [Index(value = ["id"], unique = true)])
data class LocalTransactionPayment (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id: Long? = null,
    @ColumnInfo(name = "uuid") val uuid: String? = null,
    @ColumnInfo(name = "idTransactionHead") val idTransactionHead: String? = null,
    @ColumnInfo(name = "paymentMethod") val paymentMethod: PaymentMethodTypeSType? = null,
    @ColumnInfo(name = "amount") val amount: Double? = null,
    @ColumnInfo(name = "paymentDetails") val paymentDetails: String? = null,
    @ColumnInfo(name = "createdAt") val createdAt: LocalDateTime? = LocalDateTime.now(),
    @ColumnInfo(name = "dayCreated") val dayCreated: LocalDate? = LocalDate.now()
): Error()