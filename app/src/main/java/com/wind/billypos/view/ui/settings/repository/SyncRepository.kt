package com.wind.billypos.view.ui.settings.repository

import com.wind.billypos.data.dao.SyncDao
import com.wind.billypos.data.local.model.*
import com.wind.billypos.data.remote.api.SyncApi
import com.wind.billypos.data.remote.mapper.BillyPosMapper
import com.wind.billypos.data.remote.model.entity.RemoteEntityPoint
import com.wind.billypos.data.remote.model.cashbalance.RemoteCashBalanceResponse
import com.wind.billypos.data.remote.model.cashbalance.RemoteGetCashBalanceResponse
import com.wind.billypos.data.remote.model.category.RemoteCategoryResponse
import com.wind.billypos.data.remote.model.customer.RemoteCustomerResponse
import com.wind.billypos.data.remote.model.customer.RemoteGetCustomersResponse
import com.wind.billypos.data.remote.model.entity.RemoteEntityPointResponse
import com.wind.billypos.data.remote.model.invoice.RemoteGetTransactionHeadResponse
import com.wind.billypos.data.remote.model.invoice.RemoteTransactionHeadResponse
import com.wind.billypos.data.remote.model.item.RemoteGetItemsResponse
import com.wind.billypos.data.remote.model.item.RemoteItemResponse
import com.wind.billypos.data.remote.model.operator.RemoteGetOperatorsResponse
import com.wind.billypos.data.remote.model.operator.RemoteOperatorResponse
import com.wind.billypos.view.model.Operator
import io.reactivex.Maybe
import io.reactivex.Single
import org.threeten.bp.LocalDateTime

class SyncRepository(private val syncApi: SyncApi, private val syncDao: SyncDao) {


    private val billyPosMapper = BillyPosMapper()

//    Items

    fun sendItem(item: LocalItem): Maybe<RemoteItemResponse> {
        return syncApi.sendItem(item = billyPosMapper.itemMapper.toRemoteModel(item))
    }


    fun getNotSyncedItems(): Maybe<List<LocalItem>> = syncDao.getNotSyncedItems()

    fun updateItem(item: LocalItem): Single<Int> =
        Single.create {
            val int = syncDao.updateItem(item = item)
            it.onSuccess(int)
        }


    fun findItemById(id: Long): Single<Int> =
        syncDao.findItemById(id = id).map {
            syncDao.updateItem(it.copy(isSync = true, lastServerSync = LocalDateTime.now()))
        }

    fun getItems(): Maybe<RemoteGetItemsResponse> = syncApi.getItems(offset = 0, batchSize = 1000)

    fun insertItems(items: List<LocalItem>): Maybe<List<Long>> = syncDao.insertItems(items = items)

//    Customers

    fun sendCustomer(customer: LocalCustomer): Maybe<RemoteCustomerResponse> =
        syncApi.sendCustomer(customer = billyPosMapper.customerMapper.toRemoteModel(customer))


    fun getNotSyncedCustomers(): Maybe<List<LocalCustomer>> = syncDao.getNotSyncedCustomers()

    fun getCustomers(): Maybe<RemoteGetCustomersResponse> {
        return syncApi.getCustomers(offset = 0, batchSize = 1000)
    }

    fun insertCustomers(customers: List<LocalCustomer>) =
        syncDao.insertCustomers(customers = customers)

    fun findCustomerByUUID(uuid: String?): Single<Int> =
        syncDao.findCustomerByUUID(uuid = uuid).map {
            syncDao.updateCustomer(it.copy(isSync = true, lastServerSync = LocalDateTime.now()))
        }

    // Categories

    fun getCategories(): Maybe<RemoteCategoryResponse> = syncApi.getCategories()

    fun insertCategories(categories: List<LocalCategory>): Maybe<List<Long>> =
        syncDao.insertCategories(categories = categories)

    fun insertSubCategories(subCategories: List<LocalSubCategory>): Maybe<List<Long>> =
        syncDao.insertSubCategories(subCategories = subCategories)


//    Cash Balance

    fun sendCashBalance(cashBalance: LocalCashBalance): Maybe<RemoteCashBalanceResponse> =
        syncApi.sendCashBalance(
            cashBalance = billyPosMapper.cashBalanceMapper.toRemoteModel(
                cashBalance
            )
        )

    fun getNotSyncedCashBalance(): Maybe<List<LocalCashBalance>> = syncDao.getNotSyncedCashBalance()

    fun getCashBalance(): Maybe<RemoteGetCashBalanceResponse> = syncApi.getCashBalance()


    fun insertCashBalance(cashBalance: List<LocalCashBalance>) =
        syncDao.insertCashBalance(cashBalance = cashBalance)

    fun findCashBalanceByUUID(uuid: String?): Single<Int> =
        syncDao.findCashBalanceByUUID(uuid = uuid).map {
            syncDao.updateCashBalance(it.copy(isSync = true))
        }


    //    Invoices

    fun sendInvoice(invoice: LocalTransactionHead): Maybe<RemoteTransactionHeadResponse> =
        syncApi.sendInvoice(
            invoice = billyPosMapper.transactionHeadMapper.toRemoteModel(
                invoice
            )
        )

    fun getNotSyncedInvoices(): Maybe<List<LocalTransactionHead>> = syncDao.getNotSyncedInvoices()

    fun getInvoices(): Maybe<RemoteGetTransactionHeadResponse> =
        syncApi.getInvoices(offset = 0, batchSize = 1000)


    fun insertInvoices(invoices: List<LocalTransactionHead>) =
        syncDao.insertInvoices(invoices = invoices)

    fun findInvoiceByUUID(uuid: String?): Single<Int> =
        syncDao.findInvoiceByUUID(uuid = uuid).map {
            syncDao.updateInvoice(it.copy(isSync = true))
        }


//  Entity Points

    fun getEntityPoints(): Maybe<RemoteEntityPointResponse> = syncApi.getEntityPoints()

    fun insertEntityPoints(entityPoints: List<LocalEntityPoint>): Maybe<List<Long>> =
        syncDao.insertEntityPoints(entityPoints = entityPoints)


//    Operators

    fun getOperators(): Maybe<RemoteGetOperatorsResponse> = syncApi.getOperators()

    fun insertOperators(operators: List<LocalOperator>): Maybe<List<Long>> =
        syncDao.insertOperators(operators = operators)

}





