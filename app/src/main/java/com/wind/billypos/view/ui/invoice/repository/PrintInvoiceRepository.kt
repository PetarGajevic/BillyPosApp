package com.wind.billypos.view.ui.invoice.repository

import com.wind.billypos.data.dao.InvoiceDao
import com.wind.billypos.data.local.model.LocalTransactionBody
import io.reactivex.Maybe

class PrintInvoiceRepository(private val invoiceDao: InvoiceDao) {

    fun getItems(trnHeadUUID: String): Maybe<List<LocalTransactionBody>> = invoiceDao.getTransactions(transactionUUID = trnHeadUUID)
}