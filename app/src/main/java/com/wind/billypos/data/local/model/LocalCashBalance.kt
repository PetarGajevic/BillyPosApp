package com.wind.billypos.data.local.model


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.wind.billypos.base.Error
import com.wind.billypos.utils.enums.FiscalisationState
import com.wind.billypos.utils.enums.CashBalanceType
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime

@Entity(tableName = "cash_balance", indices = [Index(value = ["uuid"], unique = true)])
data class LocalCashBalance(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")  val id: Long? = null,
    @ColumnInfo(name = "uuid")  val uuid: String? = null,
    @ColumnInfo(name = "deviceId") val deviceId: String? = null,
    @ColumnInfo(name = "userId") val userId: Long? = null,
    @ColumnInfo(name = "cashAmount") val cashAmount: Double? = 0.0,
    @ColumnInfo(name = "balanceState") val balanceState: String? = null,
    @ColumnInfo(name = "notes") val notes: String? = null,
    @ColumnInfo(name = "operation") val operation: String? = CashBalanceType.BALANCE,
    @ColumnInfo(name = "cashBalanceState") val cashBalanceState: String = FiscalisationState.NOT_FISCALIZED,
    @ColumnInfo(name = "responseUUID") val responseUUID: String? = null,
    @ColumnInfo(name = "changeDateTime") val changeDateTime: LocalDateTime? = LocalDateTime.now(),
    @ColumnInfo(name = "createdAt") val createdAt: LocalDate? = LocalDate.now(),
    @ColumnInfo(name = "cashDepositDate") val cashDepositDate: LocalDate? = LocalDate.now(),
    @ColumnInfo(name = "isFiscalised") val isFiscalised: Boolean? = false,
    @ColumnInfo(name = "isSync") val isSync: Boolean? = false,
    @ColumnInfo(name = "fcdc") val fcdc: String? = null
) : Error()