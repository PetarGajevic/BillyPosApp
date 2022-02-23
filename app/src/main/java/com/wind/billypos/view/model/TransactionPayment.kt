package com.wind.billypos.view.model

import androidx.room.ColumnInfo
import com.wind.billypos.base.Error
import com.wind.billypos.utils.enums.PaymentMethodTypeSType
import org.threeten.bp.LocalDateTime

data class TransactionPayment(
     val id: Long? = null,
     val uuid: String? = null,
     val idTransactionHead: String? = null,
     val paymentMethod: PaymentMethodTypeSType? = null,
     val amount: Double? = null,
     val paymentDetails: String? = null,
     val createdAt: LocalDateTime? = LocalDateTime.now()
): Error()