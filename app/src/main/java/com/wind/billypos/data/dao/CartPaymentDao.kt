package com.wind.billypos.data.dao

import androidx.room.*
import com.wind.billypos.data.local.model.LocalFiscalDigitalKey
import com.wind.billypos.data.local.model.LocalTransactionBody
import com.wind.billypos.data.local.model.LocalTransactionHead
import com.wind.billypos.data.local.model.LocalTransactionPayment
import com.wind.billypos.utils.enums.FiscalisationState
import com.wind.billypos.view.model.TransactionBody
import com.wind.billypos.view.model.TransactionHead
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single

@Dao
interface CartPaymentDao {

    @Insert
    fun insert(trnPayment: LocalTransactionPayment): Single<Long>

    @Update
    fun update(trnHead: LocalTransactionHead): Single<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(trnHead: LocalTransactionHead): Single<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(trnBodyList: List<LocalTransactionBody>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateOrders(transactionHeadList: List<LocalTransactionHead>): Maybe<Int>

    @Query("SELECT * FROM transactions_head WHERE id = :idHead")
    fun findTransactionHeadById(idHead: Long): Single<LocalTransactionHead>

    @Query("SELECT * FROM tbl_transactions_body WHERE idTransactionHead = :idTrnHead")
    fun getItems(idTrnHead: String?): Maybe<List<LocalTransactionBody>>

    @Query("SELECT *  FROM fiscal_digital_key LIMIT 1")
    fun getFiscalDigitalKey(): Maybe<LocalFiscalDigitalKey>

    @Query("DELETE FROM transactions_payment WHERE idTransactionHead = :transactionUUID")
    fun deletePaymentMethods(transactionUUID: String?)

    // TODO("Vratiti parametre za fiskalizovane transakcije")
    @Query("SELECT * FROM transactions_head WHERE isOrderInvoice = 1 AND idEntity = :idEntity AND isFiscalized = 0 AND transactionState = :fiscalisationState")
    fun getInvoiceIIC(
        idEntity: String?,
        fiscalisationState: String = FiscalisationState.NOT_FISCALIZED
    ): Maybe<List<LocalTransactionHead>>

    @Query("SELECT * FROM tbl_transactions_body WHERE idTransactionHead IN (:id)")
    fun getSummaryTransactionBodies(id: List<String?>): Maybe<List<LocalTransactionBody>>



}