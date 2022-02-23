package com.wind.billypos.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.wind.billypos.data.local.model.LocalItem
import com.wind.billypos.data.local.model.LocalOperator
import io.reactivex.Maybe
import io.reactivex.Single

@Dao
interface OperatorDao {

    @Insert
    fun insert(operator: LocalOperator): Maybe<Long>

    @Update
    fun update(operator: LocalOperator): Maybe<Int>

    @Query("SELECT * FROM operators WHERE id = :id")
    fun findOperatorById(id: Long): Maybe<LocalOperator>

    @Query("SELECT * FROM operators WHERE id = :id AND pin = :oldPin")
    fun checkOldPin(id: Long?, oldPin: String?): Maybe<LocalOperator?>

    @Query("SELECT count(*) FROM operators WHERE pin = :pin")
    fun checkPin(pin: String?): Maybe<Int>

    @Query("SELECT count(*) FROM operators WHERE operatorCode = :operatorCode")
    fun checkOperatorCode(operatorCode: String?): Maybe<Int>

    @Query("SELECT count(*) FROM operators WHERE operatorCode = :operatorCode AND id != :id")
    fun checkOperatorCode(operatorCode: String?, id: Long?): Maybe<Int>

    @Query("SELECT * FROM operators")
    fun getOperators(): Maybe<List<LocalOperator>>

    @Query("SELECT * FROM operators WHERE firstName LIKE '%' || :input || '%' OR  lastName LIKE '%' || :input || '%'")
    fun searchOperators(input: String?): Maybe<List<LocalOperator>>
}