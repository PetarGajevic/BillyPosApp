package com.wind.billypos.view.ui.order.repository

import com.wind.billypos.data.dao.OrderInvoiceDao
import com.wind.billypos.data.local.model.LocalEntityPoint
import com.wind.billypos.data.local.model.LocalTransactionHead
import io.reactivex.Maybe
import timber.log.Timber

class OrderInvoiceRepository(private val orderInvoiceDao: OrderInvoiceDao) {

    fun getEntityPoints(): Maybe<List<LocalEntityPoint>> = orderInvoiceDao.getEntityPoints()

    fun checkFreeEntityPoints(entityPointId: String?): Maybe<LocalTransactionHead> =
        orderInvoiceDao.checkFreeEntityPoints(entityPointId = entityPointId)

}