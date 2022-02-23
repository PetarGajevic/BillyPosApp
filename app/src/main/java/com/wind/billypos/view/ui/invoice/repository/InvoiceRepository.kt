package com.wind.billypos.view.ui.invoice.repository

import com.wind.billypos.data.dao.InvoiceDao
import com.wind.billypos.data.local.model.LocalItem
import com.wind.billypos.data.local.model.LocalTransactionBody
import com.wind.billypos.data.local.model.LocalTransactionHead
import com.wind.billypos.view.model.Item
import io.reactivex.Maybe
import io.reactivex.Single
import timber.log.Timber
import java.util.*

class InvoiceRepository(private val invoiceDao: InvoiceDao) {

    fun getAllCategories() = invoiceDao.getAllCategories()

    fun getAllSubCategories() = invoiceDao.getAllSubCategories()

    fun getSubCategories(id: Long) = invoiceDao.getSubCategories(id = id)

    fun getItemsByCategory(id: Long): Maybe<List<LocalItem>> = invoiceDao.getItemsByCategory(id = id)

    fun getAllItems() = invoiceDao.getAllItems()

    fun getLastInvoiceNum(): Maybe<Long> = invoiceDao.getLastInvoiceNum()

    fun getItemsByTransactionHead(transactionHeadUUID: String): Maybe<List<LocalTransactionBody>> = invoiceDao.getItemsByTransactionUUID(transactionUUID = transactionHeadUUID)


    fun nextNum(): Single<Long> =
        invoiceDao.nextNum()

    fun insert(trnHead: LocalTransactionHead): Maybe<LocalTransactionHead?> =
        invoiceDao.insert(trnHead)
            .flatMap {
                invoiceDao.getTransactionHeadById(it)
            }.doOnSuccess {
                it
            }

    fun update(trnHead: LocalTransactionHead?): Single<Int?> =
        invoiceDao.update(trnHead)
           .doOnSuccess {

            }

    fun getTransactionHeadById(id: Long) = invoiceDao.getTransactionHeadById(id = id)

    fun getTransactionHead(trnHeadUUID: String?): Maybe<LocalTransactionHead?> = invoiceDao.getTransactionHead(trnHeadUUID = trnHeadUUID)

    fun getItemsBySubCategory(subId: Long): Maybe<Long> = invoiceDao.getItemsBySubCategory(subId = subId)

    fun getItemsByCategoryAndSubcategory(subId: Long, categoryId: Long): Maybe<List<LocalItem>> = invoiceDao.getItemsByCategoryAndSubcategory(subId = subId, categoryId = categoryId)

    fun findForItem(trnHead: LocalTransactionHead, item: Item) =
        invoiceDao.findForItem(trnHead.uuid, item.uuid)

    fun insert(trnBody: LocalTransactionBody) {
        val body = trnBody.copy(
            total = trnBody.itemPrice?.let { trnBody.quantity?.times(it) },
            totalWithVat = trnBody.itemPrice?.let { trnBody.quantity?.times(it) }
        )
        Timber.e("Insert body %s " , trnBody)
        if (trnBody.id != null) {
            invoiceDao.update(transactionBody = body)
        } else {
            invoiceDao.insert(transactionBody = body)
        }
    }

    fun deleteForHead(trnHead: LocalTransactionHead) = invoiceDao.deleteForHead(trnHead.uuid)

    fun delete(trnHead: LocalTransactionHead) = invoiceDao.delete(trnHead.uuid)

}