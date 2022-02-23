package com.wind.billypos.view.ui.settings.repository

import com.wind.billypos.data.dao.ExportUnfiscalisedDao
import com.wind.billypos.data.local.model.LocalCashBalance
import com.wind.billypos.data.local.model.LocalTransactionHead
import io.reactivex.Maybe

class ExportUnfiscalisedRepository(private val exportUnfiscalisedDao: ExportUnfiscalisedDao) {

    fun findAllNotFiscalisedInvoices(): Maybe<List<LocalTransactionHead>> =
        exportUnfiscalisedDao.findAllNotFiscalisedInvoices()

    fun findAllNotFiscalisedCashBalance(): Maybe<List<LocalCashBalance>> =
        exportUnfiscalisedDao.findAllNotFiscalisedCashBalance()

}