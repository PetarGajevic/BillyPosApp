package com.wind.billypos.data.dao

import androidx.room.*
import com.wind.billypos.data.local.model.LocalFiscalDigitalKey
import com.wind.billypos.data.local.model.LocalTransactionBody
import com.wind.billypos.data.local.model.LocalTransactionHead
import com.wind.billypos.data.local.model.LocalTransactionPayment
import com.wind.billypos.view.model.TransactionBody
import com.wind.billypos.view.model.report.TransactionsTodayOverview
import io.reactivex.Maybe
import io.reactivex.Single
import org.threeten.bp.LocalDate

@Dao
interface TransactionDao {

    @Update
    fun update(trnBody: LocalTransactionBody?): Single<Int>

    @Insert
    fun insert(trnHead: LocalTransactionHead): Single<Long>

    @Update
    fun update(trnHead: LocalTransactionHead): Single<Int>

    @Delete
    fun delete(trnBody: LocalTransactionBody?): Single<Int>

    @Insert
    fun insertTransactionList(trnList: List<LocalTransactionBody>): Maybe<List<Long>>

    @Query("SELECT sum(totalWithVat) total, count(*) transactions FROM transactions_head WHERE dayCreated = :date AND isReversed = 0")
    fun getTodayTotalSales(date: LocalDate = LocalDate.now()): Maybe<TransactionsTodayOverview>

    @Query("SELECT * FROM transactions_head ORDER BY createdAt DESC")
   fun getAllTransactions(): Maybe<List<LocalTransactionHead>>

   @Query("SELECT * FROM tbl_transactions_body WHERE idTransactionHead = :trnHeadUUID")
   fun getTransactionsBody(trnHeadUUID: String): Maybe<List<LocalTransactionBody>>

    @Query("SELECT *  FROM fiscal_digital_key LIMIT 1")
    fun getFiscalDigitalKey(): Maybe<LocalFiscalDigitalKey>

    @Query("SELECT * FROM transactions_payment WHERE idTransactionHead = :trnHeadUUID")
   fun getPaymentMethods(trnHeadUUID: String): Maybe<List<LocalTransactionPayment>>

    @Query("SELECT * FROM transactions_head WHERE isFiscalized = 0")
    fun getAllUnfiscalizedTransactions(): Maybe<List<LocalTransactionHead>>

    @Query("SELECT * FROM tbl_transactions_body WHERE idTransactionHead = :transactionUUID")
    fun getTransactions(transactionUUID: String?): Maybe<List<LocalTransactionBody>>

    @Query("SELECT invoiceNo FROM transactions_head ORDER BY id DESC LIMIT 1")
    fun getLastInvoiceNum(): Maybe<Long>

}