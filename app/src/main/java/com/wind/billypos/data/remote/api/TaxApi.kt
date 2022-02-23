package com.wind.billypos.data.remote.api

import com.wind.billypos.data.remote.model.cashbalance.RemoteCashDepositResponse
import com.wind.billypos.data.remote.model.RemoteFiscalizeReceiptResponse
import io.reactivex.Observable
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface TaxApi{

    @Retention(AnnotationRetention.RUNTIME) annotation class XML
    @Retention(AnnotationRetention.RUNTIME) annotation class JSON

    @XML
    @POST("RegisterCashDepositRequest/FiscalizationService.wsdl")
    @Headers("Accept: text/xml", "No-Authentication: true")
    fun fiscalCashDeposit(@Body requestBody: RequestBody): Observable<RemoteCashDepositResponse>

    @XML
    @POST("RegisterInvoiceRequest/FiscalizationService.wsdl")
    @Headers("Accept: text/xml", "No-Authentication: true")
    fun fiscalReceipt(@Body requestBody: RequestBody): Observable<RemoteFiscalizeReceiptResponse>


}