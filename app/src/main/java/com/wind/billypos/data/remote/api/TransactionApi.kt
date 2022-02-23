package com.wind.billypos.data.remote.api

import com.wind.billypos.data.remote.model.invoice.RemoteTransactionHead
import com.wind.billypos.data.remote.model.invoice.RemoteTransactionHeadResponse
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.POST

interface TransactionApi {

    @POST("v1/transaction")
    fun sendTransactionHead(@Body remoteTransactionHead: RemoteTransactionHead): Observable<RemoteTransactionHeadResponse>

}