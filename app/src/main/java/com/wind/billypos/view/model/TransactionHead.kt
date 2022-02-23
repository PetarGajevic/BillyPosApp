package com.wind.billypos.view.model

import android.annotation.SuppressLint
import android.os.Parcelable
import com.wind.billypos.R
import com.wind.billypos.base.Error
import com.wind.billypos.data.local.model.LocalTransactionBody
import com.wind.billypos.data.local.model.LocalTransactionPayment
import com.wind.billypos.utils.enums.FiscalisationState
import com.wind.billypos.utils.enums.FiscalisationState.FISCALIZED
import com.wind.billypos.utils.enums.FiscalisationState.STORNO
import com.wind.billypos.utils.enums.PaymentMethod
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter
import java.util.*

@Parcelize
data class TransactionHead(
    val id: Long? = null,
    val uuid: String? = UUID.randomUUID().toString(),
    val idCompany: Long? = null,
    val idAddress: Long? = null,
    val idDevice: Long? = null,
    val idUser: Long? = null,
    val idTransactionParent: String = "",
    val total: Double = 0.0,
    val totalWithVat: Double = 0.0,
    val vatAmount: Double = 0.0,
    val discountValue: Double = 0.0,
    val invoiceNum: String? = null,
    val deviceTCR: String? = null,
    val uuidTransaction: String? = null,
    val fiscalUUID: String? = null,
    val fiscalFIC: String? = null,
    val reversedFIC: String? = null,
    val iic: String? = null,
    val invoiceNo: Long? = null,
    val transactionState: String = FiscalisationState.NOT_FISCALIZED,
    val paymentMethod: String = PaymentMethod.CASH,
    val receiptDateTime: String? = ZonedDateTime.now(ZoneId.of("Europe/Budapest")).format(
        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZZZZZ")),
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val dayCreated: LocalDate = LocalDate.now(),
    val fiscalizedAt: LocalDateTime? = null,
    val isFiscalized: Boolean? = false,
    val isSync: Boolean? = false,
    val reversedAt: LocalDateTime? = null,
    val isReversed: Boolean? = false,
    val idCustomer: String = "",
    val lastServerSync: LocalDateTime? = null,
    val isOrderInvoice: Boolean = false,
    val isOrderSelected: Boolean = false,
    val isPaid: Boolean = false,
    val idEntity: String = "",
    val isSummaryInvoice: Boolean = false,
    val items: @RawValue List<TransactionBody>? = arrayListOf(),
    val paymentMethods: @RawValue List<TransactionPayment>? = arrayListOf(),
    val orders: @RawValue List<TransactionHead>? = arrayListOf()
): Error(), Parcelable{


    @SuppressLint("DefaultLocale")
    fun nextInvoiceNo(): String? {
        return String.format(
            "%s/%s/%s", invoiceNo,
            Calendar.getInstance()[Calendar.YEAR], deviceTCR
        )
    }

    fun isTransactionFiscalized(): Boolean {
        return transactionState === FISCALIZED
    }

    fun getTransactionStateLabel(): Int {
        when (transactionState) {
            FISCALIZED -> return R.string.transaction_state_fiscalized
            STORNO -> return R.string.transaction_state_fiscalized
        }
        return R.string.transaction_state_not_fiscalized
    }

    fun canBeReversed(): Boolean {
        return id != null && transactionState === FISCALIZED && isReversed == true && idTransactionParent == null
    }

    @Parcelize
    data class SameTax(
        var numOfItems: Int? = 0,
        var priceBefVAT:  Double? = 0.0,
        var vatAmt: Double? = 0.0,
        var vatRate: Double? = 0.0
    ): Parcelable

}