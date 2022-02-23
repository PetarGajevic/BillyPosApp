package com.wind.billypos.data.remote.api

import com.wind.billypos.data.remote.model.RemoteConfiguration
import com.wind.billypos.data.remote.model.RemoteConfigurationResponse
import com.wind.billypos.data.remote.model.RemoteLoginRequest
import com.wind.billypos.data.remote.model.RemoteLoginResponse
import io.reactivex.Maybe
import io.reactivex.Observable
import retrofit2.http.*

interface LoginApi {

    @POST("auth")
    @Headers( "No-Authentication: true")
    fun login(@Body login: RemoteLoginRequest): Maybe<RemoteLoginResponse?>


    @GET("v1/configuration")
    fun configuration(@Header("Authorization") authToken: String): Maybe<RemoteConfigurationResponse>
}