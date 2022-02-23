package com.wind.billypos.data.remote.api

import com.wind.billypos.data.remote.model.entity.RemoteEntityPoint
import com.wind.billypos.data.remote.model.invoice.RemoteTransactionHead
import com.wind.billypos.data.remote.model.cashbalance.RemoteCashBalanceRequest
import com.wind.billypos.data.remote.model.cashbalance.RemoteCashBalanceResponse
import com.wind.billypos.data.remote.model.cashbalance.RemoteGetCashBalanceResponse
import com.wind.billypos.data.remote.model.category.RemoteCategoryResponse
import com.wind.billypos.data.remote.model.customer.RemoteCustomer
import com.wind.billypos.data.remote.model.customer.RemoteCustomerResponse
import com.wind.billypos.data.remote.model.customer.RemoteGetCustomersResponse
import com.wind.billypos.data.remote.model.entity.RemoteEntityPointResponse
import com.wind.billypos.data.remote.model.invoice.RemoteGetTransactionHeadResponse
import com.wind.billypos.data.remote.model.invoice.RemoteTransactionHeadResponse
import com.wind.billypos.data.remote.model.item.RemoteGetItemsResponse
import com.wind.billypos.data.remote.model.item.RemoteItem
import com.wind.billypos.data.remote.model.item.RemoteItemResponse
import com.wind.billypos.data.remote.model.operator.RemoteGetOperatorsResponse
import com.wind.billypos.data.remote.model.operator.RemoteOperatorResponse
import io.reactivex.Maybe
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface SyncApi {


    @POST("v1/item")
    fun sendItem(@Body item: RemoteItem): Maybe<RemoteItemResponse>

    @GET("v1/items")
    fun getItems(@Query("offset") offset: Int, @Query("batchSize") batchSize: Int): Maybe<RemoteGetItemsResponse>

    @GET("v1/customers")
    fun getCustomers(@Query("offset") offset: Int, @Query("batchSize") batchSize: Int): Maybe<RemoteGetCustomersResponse>

    @POST("v1/customers")
    fun sendCustomer(@Body customer: RemoteCustomer): Maybe<RemoteCustomerResponse>

    @GET("v1/categories")
    fun getCategories(): Maybe<RemoteCategoryResponse>

    @GET("v1/balance")
    fun getCashBalance(): Maybe<RemoteGetCashBalanceResponse>

    @POST("v1/balance")
    fun sendCashBalance(@Body cashBalance: RemoteCashBalanceRequest): Maybe<RemoteCashBalanceResponse>

    @GET("v1/transaction/weekly")
    fun getInvoices(@Query("offset") offset: Int, @Query("batchSize") batchSize: Int): Maybe<RemoteGetTransactionHeadResponse>

    @POST("v1/transaction")
    fun sendInvoice(@Body invoice: RemoteTransactionHead): Maybe<RemoteTransactionHeadResponse>

    @GET("v1/entity-points")
    fun getEntityPoints(): Maybe<RemoteEntityPointResponse>

    @GET("v1/user")
    fun getOperators(): Maybe<RemoteGetOperatorsResponse>


}