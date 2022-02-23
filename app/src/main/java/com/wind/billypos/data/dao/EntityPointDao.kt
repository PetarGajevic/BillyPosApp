package com.wind.billypos.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.wind.billypos.data.local.model.LocalCashBalance
import com.wind.billypos.data.local.model.LocalEntityPoint
import com.wind.billypos.data.local.model.LocalTransactionHead
import com.wind.billypos.utils.enums.CashBalanceType
import com.wind.billypos.view.model.TransactionHead
import io.reactivex.Maybe
import org.threeten.bp.LocalDate

@Dao
interface EntityPointDao {

    @Query("SELECT * FROM cash_balance where cashDepositDate = :date and  operation = :operation")
    fun hasOpenedDay(date: LocalDate = LocalDate.now(), operation: String = CashBalanceType.BALANCE): Maybe<LocalCashBalance>

//    TODO(Promjeniti isFiscalized u 1)
    @Query("SELECT * FROM transactions_head WHERE idEntity = :idEntity AND isPaid = 0 AND isFiscalized = 0")
    fun getEntityTransactions(idEntity: String?): Maybe<List<LocalTransactionHead>>

    @Query("SELECT invoiceNo FROM transactions_head ORDER BY id DESC LIMIT 1")
    fun getLastInvoiceNum(): Maybe<Long>

}