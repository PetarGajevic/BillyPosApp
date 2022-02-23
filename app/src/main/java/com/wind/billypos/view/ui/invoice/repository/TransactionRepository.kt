package com.wind.billypos.view.ui.invoice.repository

import com.wind.billypos.data.dao.TransactionDao
import com.wind.billypos.data.local.model.LocalTransactionBody
import com.wind.billypos.view.model.TransactionBody
import io.reactivex.Single

class TransactionRepository (private val transactionDao: TransactionDao){

    fun updateTransactionItem(trnBody: LocalTransactionBody?): Single<Int> = transactionDao.update(trnBody = trnBody)

    fun deleteTransactionItem(trnBody: LocalTransactionBody?): Single<Int> = transactionDao.delete(trnBody = trnBody)

}