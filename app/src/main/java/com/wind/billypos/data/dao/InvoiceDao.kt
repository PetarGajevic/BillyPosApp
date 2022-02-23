package com.wind.billypos.data.dao

import androidx.room.*
import com.wind.billypos.data.local.model.*
import com.wind.billypos.view.model.Category
import com.wind.billypos.view.model.Item
import com.wind.billypos.view.model.SubCategory
import com.wind.billypos.view.model.TransactionBody
import io.reactivex.Maybe
import io.reactivex.Single

@Dao
interface InvoiceDao {


    @Insert
    fun insert(transactionHead: LocalTransactionHead): Maybe<Long>

    @Update
    fun update(transactionHead: LocalTransactionHead?): Single<Int>

    @Insert
    fun insert(transactionBody: LocalTransactionBody)

    @Update
    fun update(transactionBody: LocalTransactionBody)

    @Query("SELECT * FROM transactions_head WHERE id = :id")
    fun getTransactionHeadById(id: Long): Maybe<LocalTransactionHead?>

    @Query("SELECT * FROM CATEGORY WHERE isActive = 1")
    fun getAllCategories(): Maybe<List<LocalCategory>>

    @Query("SELECT * FROM SUBCATEGORY")
    fun getAllSubCategories(): Maybe<List<SubCategory>>

    @Query("SELECT * FROM SUBCATEGORY WHERE id_category = :id AND isActive = 1")
    fun getSubCategories(id: Long): Maybe<List<SubCategory>>

    @Query("SELECT * FROM item WHERE isActive = 1")
    fun getAllItems(): Maybe<List<Item>>

    @Query("SELECT invoiceNo FROM transactions_head ORDER BY id DESC LIMIT 1")
    fun nextNum(): Single<Long>

    @Query("SELECT * FROM tbl_transactions_body where idTransactionHead = :idHead and idItem = :idItem")
    fun findForItem(idHead: String?, idItem: String?): LocalTransactionBody?

    @Query("DELETE FROM tbl_transactions_body where idTransactionHead = :id")
    fun deleteForHead(id: String?)

    @Query("DELETE FROM transactions_head where uuid = :uuid")
    fun delete(uuid: String?)

    @Query("SELECT * FROM tbl_transactions_body WHERE idTransactionHead = :transactionUUID")
    fun getTransactions(transactionUUID: String?): Maybe<List<LocalTransactionBody>>

    @Query("SELECT * FROM transactions_head WHERE uuid = :trnHeadUUID")
    fun getTransactionHead(trnHeadUUID: String?): Maybe<LocalTransactionHead?>

    @Query("SELECT id_category FROM subcategory WHERE id = :subId AND isActive = 1")
    fun getItemsBySubCategory(subId: Long): Maybe<Long>

    @Query("SELECT * FROM item WHERE idSubcategory = :subId OR idCategory = :categoryId")
    fun getItemsByCategoryAndSubcategory(subId: Long, categoryId: Long): Maybe<List<LocalItem>>

    @Query("SELECT * FROM item WHERE idCategory = :id AND isActive = 1")
    fun getItemsByCategory(id: Long): Maybe<List<LocalItem>>

    @Query("SELECT invoiceNo FROM transactions_head ORDER BY id DESC LIMIT 1")
    fun getLastInvoiceNum(): Maybe<Long>

    @Query("SELECT * FROM tbl_transactions_body tb WHERE idTransactionHead = :transactionUUID")
    fun getItemsByTransactionUUID(transactionUUID: String?): Maybe<List<LocalTransactionBody>>


}