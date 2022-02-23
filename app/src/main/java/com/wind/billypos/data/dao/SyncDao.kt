package com.wind.billypos.data.dao

import androidx.room.*
import com.wind.billypos.data.local.model.*
import com.wind.billypos.view.model.Operator
import io.reactivex.Maybe
import io.reactivex.Single

@Dao
interface SyncDao {

    //    Items

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertItems(items: List<LocalItem>): Maybe<List<Long>>

    @Update
    fun updateItem(item: LocalItem): Int

    @Query("SELECT * FROM item WHERE id = :id")
    fun findItemById(id: Long): Single<LocalItem>

    @Query("SELECT * FROM item WHERE isSync = 0")
    fun getNotSyncedItems(): Maybe<List<LocalItem>>

    //    Customers

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCustomers(customers: List<LocalCustomer>): Maybe<List<Long>>

    @Update
    fun updateCustomer(customer: LocalCustomer): Int

    @Query("SELECT * FROM customer WHERE isSync = 0")
    fun getNotSyncedCustomers(): Maybe<List<LocalCustomer>>

    @Query("SELECT * FROM customer WHERE uuid = :uuid")
    fun findCustomerByUUID(uuid: String?): Single<LocalCustomer>


//    Categories

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCategories(categories: List<LocalCategory>): Maybe<List<Long>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSubCategories(subCategories: List<LocalSubCategory>): Maybe<List<Long>>

    //    Cash Balance

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCashBalance(cashBalance: List<LocalCashBalance>): Maybe<List<Long>>

    @Update
    fun updateCashBalance(cashBalance: LocalCashBalance): Int

    @Query("SELECT * FROM cash_balance WHERE isSync = 0")
    fun getNotSyncedCashBalance(): Maybe<List<LocalCashBalance>>

    @Query("SELECT * FROM cash_balance WHERE uuid = :uuid")
    fun findCashBalanceByUUID(uuid: String?): Single<LocalCashBalance>

    //    Invoice

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertInvoices(invoices: List<LocalTransactionHead>): Maybe<List<Long>>

    @Update
    fun updateInvoice(invoice: LocalTransactionHead): Int

    @Query("SELECT * FROM transactions_head WHERE isSync = 0")
    fun getNotSyncedInvoices(): Maybe<List<LocalTransactionHead>>

    @Query("SELECT * FROM transactions_head WHERE uuid = :uuid")
    fun findInvoiceByUUID(uuid: String?): Single<LocalTransactionHead>

//    Entity Points

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertEntityPoints(entityPoints: List<LocalEntityPoint>): Maybe<List<Long>>

//  Operators

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOperators(operators: List<LocalOperator>): Maybe<List<Long>>

}