package com.wind.billypos.view.ui.settings.repository

import com.wind.billypos.data.dao.SyncAndBackupDao
import io.reactivex.Single

class SyncAndBackupRepository(private val syncAndBackupDao: SyncAndBackupDao) {

    //    Transactions
    fun getSyncedTransactions(): Single<Int> = syncAndBackupDao.getSyncedTransactions()

    fun getFiscalizedTransactions(): Single<Int> =
        syncAndBackupDao.getFiscalizedTransactions()

    fun getAllTransactions(): Single<Int> = syncAndBackupDao.getAllTransactions()

    //   Cash Balance
    fun getSyncedCashBalance(): Single<Int> = syncAndBackupDao.getSyncedCashBalance()

    fun getFiscalizedCashBalance(): Single<Int> = syncAndBackupDao.getFiscalizedCashBalance()

    fun getAllCashBalance(): Single<Int> = syncAndBackupDao.getAllCashBalance()

    //    Items
    fun getAllItems(): Single<Int> = syncAndBackupDao.getAllItems()

    fun getSyncedItems(): Single<Int> = syncAndBackupDao.getSyncedItems()

    //    Cash Balance
    fun getAllCustomers(): Single<Int> = syncAndBackupDao.getAllCustomers()

    fun getSyncedCustomers(): Single<Int> = syncAndBackupDao.getSyncedCustomers()
}