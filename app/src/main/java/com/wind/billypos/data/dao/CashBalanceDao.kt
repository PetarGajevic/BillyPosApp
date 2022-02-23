package com.wind.billypos.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.wind.billypos.data.local.model.*
import com.wind.billypos.utils.enums.CashBalanceType
import com.wind.billypos.utils.enums.FiscalisationState
import com.wind.billypos.utils.enums.PaymentMethodTypeSType
import io.reactivex.Maybe
import io.reactivex.Single
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import java.util.*

@Dao
interface CashBalanceDao {

    @Insert
    fun insertCashBalance(localCashBalance: LocalCashBalance): Single<Long>

    @Update
    fun updateCashBalance(localCashBalance: LocalCashBalance): Single<Int>

    @Query("SELECT * FROM cash_balance WHERE id = :id")
    fun findCashBalanceById(id: Long): Single<LocalCashBalance?>

    @Query("SELECT * FROM cash_balance WHERE createdAt = :date AND operation='INITIAL' ORDER BY id DESC LIMIT 1")
    fun getCashAmountBalance(date: LocalDate = LocalDate.now()): Maybe<LocalCashBalance>

    @Query("SELECT * FROM cash_balance WHERE createdAt = :date AND operation = :operation")
    fun getCashAmountIN(date: LocalDate = LocalDate.now(), operation: String = CashBalanceType.IN): Maybe<List<LocalCashBalance>>

    @Query("SELECT * FROM cash_balance WHERE createdAt = :date AND operation = :operation")
    fun getCashAmountOUT(date: LocalDate = LocalDate.now(), operation: String = CashBalanceType.OUT): Maybe<List<LocalCashBalance>>

    @Query("SELECT sum(cashAmount) FROM cash_balance WHERE createdAt = :date")
    fun getTotalCashAmount(date: LocalDate = LocalDate.now()): Maybe<Double>

    @Query("SELECT * FROM cash_balance where cashDepositDate = :date and  operation = :operation")
    fun hasOpenedDay(date: LocalDate = LocalDate.now(), operation: String = CashBalanceType.BALANCE): Maybe<LocalCashBalance>

    @Query("SELECT *  FROM company LIMIT 1")
    fun getCompany(): Maybe<LocalCompany>

    @Query("SELECT *  FROM fiscal_digital_key LIMIT 1")
    fun getFiscalDigitalKey(): Maybe<LocalFiscalDigitalKey>

    @Query("SELECT * FROM transactions_head WHERE isReversed = 0")
    fun getAllTransactions(): Maybe<List<LocalTransactionHead>>

    @Query("SELECT sum(totalWithVat) FROM transactions_head WHERE isReversed = 0 AND dayCreated = :date")
    fun getTodaySales(date: LocalDate = LocalDate.now()): Maybe<Double>

    @Query("SELECT sum(totalWithVat) FROM transactions_head WHERE isReversed = 0 AND dayCreated = :date")
    fun getTodaySalesForTotal(date: LocalDate = LocalDate.now()): Double

    @Query("SELECT sum(totalWithVat) FROM transactions_head WHERE isReversed = 1 AND dayCreated = :date")
    fun getTodayStornoSales(date: LocalDate = LocalDate.now()): Maybe<Double>

    @Query("SELECT sum(amount) FROM transactions_payment WHERE dayCreated = :date AND paymentMethod = :paymentMethod" +
            " AND idTransactionHead IN (SELECT uuid FROM transactions_head WHERE isReversed = 0)")
    fun getTodayCashSales(date: LocalDate = LocalDate.now(), paymentMethod: PaymentMethodTypeSType = PaymentMethodTypeSType.BANKNOTE): Maybe<Double>

    @Query("SELECT sum(amount) FROM transactions_payment WHERE dayCreated = :date AND paymentMethod = :paymentMethod" +
            " AND idTransactionHead IN (SELECT uuid FROM transactions_head WHERE isReversed = 0)")
    fun getTodayCardSales(date: LocalDate = LocalDate.now(), paymentMethod: PaymentMethodTypeSType = PaymentMethodTypeSType.CARD): Maybe<Double>

    @Query("SELECT * FROM cash_balance WHERE isFiscalised = 0")
    fun getUnfiscalizedCashBalance(): Maybe<List<LocalCashBalance>>

    @Query("SELECT * FROM cash_balance WHERE uuid = :UUID")
    fun findCashBalanceByUUID(UUID: String?): Single<LocalCashBalance>

    //    Cash Balance

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCashBalance(cashBalance: List<LocalCashBalance>): Maybe<List<Long>>

    @Update
    fun update(cashBalance: LocalCashBalance): Int

    @Query("SELECT * FROM cash_balance WHERE isSync = 0")
    fun getNotSyncedCashBalance(): Maybe<List<LocalCashBalance>>

    @Query("SELECT * FROM fiscal_digital_key")
    fun getCertificate(): Maybe<LocalFiscalDigitalKey>


}