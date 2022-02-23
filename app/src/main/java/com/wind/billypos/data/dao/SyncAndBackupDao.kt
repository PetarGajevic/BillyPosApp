package com.wind.billypos.data.dao

import androidx.room.Dao
import androidx.room.Query
import io.reactivex.Single

@Dao
interface SyncAndBackupDao {

    //  Transactions
    @Query("SELECT count(*) FROM transactions_head WHERE isSync = 1")
    fun getSyncedTransactions(): Single<Int>

    @Query("SELECT count(*) FROM transactions_head WHERE isFiscalized = 1")
    fun getFiscalizedTransactions(): Single<Int>

    @Query("SELECT count(*) FROM transactions_head")
    fun getAllTransactions(): Single<Int>


    //    Cash Balance
    @Query("SELECT count(*) FROM cash_balance WHERE isSync = 1")
    fun getSyncedCashBalance(): Single<Int>

    @Query("SELECT count(*) FROM cash_balance WHERE isFiscalised = 1")
    fun getFiscalizedCashBalance(): Single<Int>

    @Query("SELECT count(*) FROM cash_balance")
    fun getAllCashBalance(): Single<Int>


    //    Items
    @Query("SELECT count(*) FROM item")
    fun getAllItems(): Single<Int>

    @Query("SELECT count(*) FROM item WHERE isSync = 1")
    fun getSyncedItems(): Single<Int>

    //    Customers
    @Query("SELECT count(*) FROM customer")
    fun getAllCustomers(): Single<Int>

    @Query("SELECT count(*) FROM customer WHERE isSync = 1")
    fun getSyncedCustomers(): Single<Int>


}