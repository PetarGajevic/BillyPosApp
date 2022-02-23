package com.wind.billypos.view.ui.order.repository

import com.wind.billypos.data.dao.EntityPointDao
import com.wind.billypos.data.local.model.LocalCashBalance
import com.wind.billypos.data.local.model.LocalEntityPoint
import com.wind.billypos.data.local.model.LocalTransactionHead
import com.wind.billypos.view.model.TransactionHead
import io.reactivex.Maybe

class EntityPointRepository(private val entityPointDao: EntityPointDao) {

    fun hasOpenedDay(): Maybe<LocalCashBalance> = entityPointDao.hasOpenedDay()

    fun getEntityTransactions(idEntity: String?): Maybe<List<LocalTransactionHead>> =
        entityPointDao.getEntityTransactions(idEntity = idEntity)

    fun getLastInvoiceNum(): Maybe<Long> = entityPointDao.getLastInvoiceNum()

}