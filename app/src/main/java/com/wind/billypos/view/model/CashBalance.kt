package com.wind.billypos.view.model

import androidx.room.ColumnInfo
import com.wind.billypos.base.Error
import com.wind.billypos.utils.enums.FiscalisationState
import com.wind.billypos.utils.enums.CashBalanceTaxType
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime

data class CashBalance(
    val id: Long? = null,
    val uuid: String? = null,
    val deviceId: String? = null,
    val userId: Long? = null,
    val cashAmount: Double? = 0.0,
    val balanceState: String? = null,
    val notes: String? = null,
    val operation: String? = CashBalanceTaxType.INITIAL,
    val cashBalanceState: String = FiscalisationState.NOT_FISCALIZED,
    val responseUUID: String? = null,
    val nuuis: String? = null,
    val changeDateTime: LocalDateTime? = LocalDateTime.now(),
    val createdAt: LocalDate? = LocalDate.now(),
    val cashDepositDate: LocalDate? = LocalDate.now(),
    val isFiscalised: Boolean? = false,
    val isSync: Boolean? = false,
    val fcdc: String? = null
) : Error(){

    fun isFCDCCorrect() = !fcdc.isNullOrEmpty()
    fun areDataCorrect() = cashAmount!! != 0.0
}