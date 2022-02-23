package com.wind.billypos.data.remote.model.entity

import com.google.gson.annotations.SerializedName
import com.wind.billypos.data.remote.model.customer.RemoteCustomer

data class RemoteEntityPointResponse(
    @SerializedName("status") val status: String?,
    @SerializedName("data") val data: List<RemoteEntityPoint>,
    @SerializedName("message") val message: String?,
    @SerializedName("requestStarted") val requestStarted: String?,
    @SerializedName("requestFinished") val requestFinished: String?,
    @SerializedName("requestTime") val requestTime: String?
)