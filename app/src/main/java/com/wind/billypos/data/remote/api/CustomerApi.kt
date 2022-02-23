package com.wind.billypos.data.remote.api

import com.wind.billypos.data.remote.model.customer.RemoteCustomer
import com.wind.billypos.data.remote.model.customer.RemoteCustomerResponse
import io.reactivex.Maybe
import retrofit2.http.Body
import retrofit2.http.POST

interface CustomerApi {

    @POST("v1/customers")
    fun sendCustomer(@Body customer: RemoteCustomer): Maybe<RemoteCustomerResponse>
}