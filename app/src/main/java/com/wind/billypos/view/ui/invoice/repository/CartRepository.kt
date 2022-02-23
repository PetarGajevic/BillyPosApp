package com.wind.billypos.view.ui.invoice.repository

import com.wind.billypos.data.dao.InvoiceDao
import com.wind.billypos.data.local.model.LocalTransactionHead
import com.wind.billypos.view.model.TransactionHead
import io.reactivex.Single

class CartRepository(private val invoiceDao: InvoiceDao) {


    fun getTransactions(transactionUUID: String?) = invoiceDao.getTransactions(transactionUUID = transactionUUID)


    fun insertTransactionHead(trnHead: LocalTransactionHead?): Single<Int> = invoiceDao.update(transactionHead = trnHead)

}