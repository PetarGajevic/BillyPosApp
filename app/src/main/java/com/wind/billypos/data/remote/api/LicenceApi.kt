package com.wind.billypos.data.remote.api

import com.wind.billypos.data.remote.model.RemoteLicenceResponse
import io.reactivex.Maybe
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.POST

interface LicenceApi {

    @POST("v1/license/check")
    fun checkLicence(@Body requestBody: RequestBody): Maybe<RemoteLicenceResponse>

}