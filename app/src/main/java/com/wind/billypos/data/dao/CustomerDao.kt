package com.wind.billypos.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.wind.billypos.data.local.model.LocalCustomer
import io.reactivex.Maybe

@Dao
interface CustomerDao {

    @Insert
    fun insert(customer: LocalCustomer): Maybe<Long>

    @Update
    fun update(customer: LocalCustomer): Maybe<Int>

    @Query("SELECT * FROM customer WHERE id = :id")
    fun findCustomerById(id: Long): Maybe<LocalCustomer>

    @Query("SELECT * FROM customer")
    fun getCustomers(): Maybe<List<LocalCustomer>>

    @Query("SELECT * FROM customer WHERE name LIKE '%' || :input || '%'")
    fun searchCustomers(input: String?): Maybe<List<LocalCustomer>>

}