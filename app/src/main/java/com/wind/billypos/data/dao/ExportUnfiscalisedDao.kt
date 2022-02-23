package com.wind.billypos.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.wind.billypos.data.local.model.LocalCashBalance
import com.wind.billypos.data.local.model.LocalTransactionHead
import io.reactivex.Maybe

@Dao
interface ExportUnfiscalisedDao {

    @Query("SELECT * FROM transactions_head WHERE isFiscalized = 0")
    fun findAllNotFiscalisedInvoices(): Maybe<List<LocalTransactionHead>>

    @Query("SELECT * FROM cash_balance WHERE isFiscalised = 0")
    fun findAllNotFiscalisedCashBalance(): Maybe<List<LocalCashBalance>>

}