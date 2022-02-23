package com.wind.billypos.data.remote.api

import com.wind.billypos.data.remote.model.cashbalance.RemoteCashBalanceRequest
import com.wind.billypos.data.remote.model.cashbalance.RemoteCashBalanceResponse
import com.wind.billypos.data.remote.model.cashbalance.RemoteGetCashBalanceResponse
import io.reactivex.Maybe
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface CashBalanceApi {

    @POST("v1/balance")
    fun sendCashBalance(@Body remoteCashBalance: RemoteCashBalanceRequest): Observable<RemoteCashBalanceResponse>

    @GET("v1/balance")
    fun getCashBalance(): Maybe<RemoteGetCashBalanceResponse>


}