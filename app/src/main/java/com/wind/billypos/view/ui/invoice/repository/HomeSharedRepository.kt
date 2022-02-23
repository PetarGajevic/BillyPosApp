package com.wind.billypos.view.ui.invoice.repository

import com.wind.billypos.data.dao.InvoiceDao

class HomeSharedRepository(private val invoiceDao: InvoiceDao) {

    fun getTransactionBodyList(trnHeadUUID: String?) = invoiceDao.getItemsByTransactionUUID(transactionUUID = trnHeadUUID)


}