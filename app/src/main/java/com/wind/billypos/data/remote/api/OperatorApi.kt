package com.wind.billypos.data.remote.api

import com.wind.billypos.data.remote.model.RemoteConfigurationResponse
import com.wind.billypos.data.remote.model.operator.RemoteOperator
import com.wind.billypos.data.remote.model.operator.RemoteOperatorResponse
import io.reactivex.Maybe
import retrofit2.http.*

interface OperatorApi {

    @POST("v1/user")
    fun sendOperator(@Body operator: RemoteOperator): Maybe<RemoteOperatorResponse>

    @PUT("v1/user/{id}")
    fun updateOperator(
        @Body operator: RemoteOperator,
        @Path("id") id: Long?
    ): Maybe<RemoteOperatorResponse>

}