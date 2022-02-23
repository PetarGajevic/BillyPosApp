package com.wind.billypos.data.local.model

import androidx.room.*
import com.wind.billypos.base.Error
import com.wind.billypos.data.local.model.typeconverter.ListConverter
import com.wind.billypos.utils.enums.FiscalisationState
import com.wind.billypos.utils.enums.PaymentMethod
import com.wind.billypos.view.model.TransactionBody
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter
import java.util.*

@Entity(tableName = "transactions_head", indices = [Index(value = ["uuid"], unique = true)])
data class LocalTransactionHead(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id: Long? = null,
    @ColumnInfo(name = "uuid") val uuid: String? = "",
    @ColumnInfo(name = "idCompany") val idCompany: Long? = null,
    @ColumnInfo(name = "idAddress") val idAddress: Long? = null,
    @ColumnInfo(name = "idDevice") val idDevice: Long? = null,
    @ColumnInfo(name = "idUser") val idUser: Long? = null,
    @ColumnInfo(name = "idTransactionParent") val idTransactionParent: String = "",
    @ColumnInfo(name = "total") val total: Double = 0.0,
    @ColumnInfo(name = "totalWithVat") val totalWithVat: Double = 0.0,
    @ColumnInfo(name = "vatAmount") val vatAmount: Double = 0.0,
    @ColumnInfo(name = "discountValue") val discountValue: Double = 0.0,
    @ColumnInfo(name = "invoiceNum") val invoiceNum: String? = null,
    @ColumnInfo(name = "deviceTCR") val deviceTCR: String? = null,
    @ColumnInfo(name = "uuidTransaction") val uuidTransaction: String? = null,
    @ColumnInfo(name = "fiscalUUID") val fiscalUUID: String? = null,
    @ColumnInfo(name = "fiscalFIC") val fiscalFIC: String? = null,
    @ColumnInfo(name = "reversedFIC") val reversedFIC: String? = null,
    @ColumnInfo(name = "iic") val iic: String? = null,
    @ColumnInfo(name = "invoiceNo") val invoiceNo: Long? = null,
    @ColumnInfo(name = "transactionState") val transactionState: String = FiscalisationState.NOT_FISCALIZED,
    @ColumnInfo(name = "paymentMethod") val paymentMethod: String = PaymentMethod.CASH,
    @ColumnInfo(name = "receiptDateTime") val receiptDateTime: String? = ZonedDateTime.now(ZoneId.of("Europe/Budapest")).format(
        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZZZZZ")),
    @ColumnInfo(name = "createdAt") val createdAt: LocalDateTime = LocalDateTime.now(),
    @ColumnInfo(name = "dayCreated") val dayCreated: LocalDate = LocalDate.now(),
    @ColumnInfo(name = "fiscalizedAt") val fiscalizedAt: LocalDateTime? = null,
    @ColumnInfo(name = "isFiscalized") val isFiscalized: Boolean? = false,
    @ColumnInfo(name = "isSync") val isSync: Boolean? = false,
    @ColumnInfo(name = "reversedAt") val reversedAt: LocalDateTime? = null,
    @ColumnInfo(name = "isReversed") val isReversed: Boolean? = false,
    @ColumnInfo(name = "idCustomer") val idCustomer: String = "",
    @ColumnInfo(name = "lastServerSync") val lastServerSync: LocalDateTime? = null,
    @ColumnInfo(name = "isOrderInvoice") val isOrderInvoice: Boolean = false,
    @ColumnInfo(name = "isPaid") val isPaid: Boolean = false,
    @ColumnInfo(name = "idEntity") val idEntity: String = "",
    @ColumnInfo(name = "isSummaryInvoice") val isSummaryInvoice: Boolean = false,
    @ColumnInfo(name = "items") val items: List<LocalTransactionBody>? = listOf(),
    @ColumnInfo(name = "paymentMethods") val paymentMethods: List<LocalTransactionPayment>? = listOf()
): Error()